package com.wsq.library.statemachine;

import cn.hutool.core.map.MapUtil;
import com.wsq.library.statemachine.common.ActionResult;
import com.wsq.library.statemachine.common.DefaultAction;
import com.wsq.library.statemachine.common.Event;
import com.wsq.library.statemachine.config.AbstractConfig;
import com.wsq.library.statemachine.context.AbstractContext;
import com.wsq.library.statemachine.context.ChoiceContext;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 本状态机目标是轻量级，所以不涉及缓存和持久化
 * 使用者可以结合自身需求结合ThreadLocal或者持久化手段进行保存状态机
 * <p>
 * 使用样例详见本类结尾
 *
 * @author wsq
 * 2021/4/22 19:37
 */
@Data
public class StateMachine<S extends Enum<S>, E extends Enum<E>> {
    private S currentState;

    private Map<S, Map<E, AbstractContext<S, E>>> map = new HashMap<>();

    private static Map<String, AbstractConfig<?, ?>> configMap = new LinkedHashMap<>();

    static {
        ServiceLoader<AbstractConfig> configs = ServiceLoader.load(AbstractConfig.class);

        for (AbstractConfig<?, ?> config : configs) {
            AbstractConfig<?, ?> configObj = config.build();
            configMap.put(configObj.getStateMachineName(), configObj);
        }
    }

    @SuppressWarnings("unchecked")
    public StateMachine(String stateMachineName, S state) {
        if (StringUtils.isBlank(stateMachineName)) {
            throw new RuntimeException("stateMachineName miss");
        }

        if (MapUtil.isEmpty(configMap)) {
            throw new RuntimeException("cannot find config");
        }

        AbstractConfig<?, ?> config = configMap.get(stateMachineName);
        if (Objects.isNull(config)) {
            throw new RuntimeException("cannot find config by: " + stateMachineName);
        }

        config.getContextList().forEach(context -> map.computeIfAbsent((S) context.getSource(),
                key -> new HashMap<E, AbstractContext<S, E>>() {{
                    put((E) context.getEvent().getEvent(), (AbstractContext<S, E>) context);
                }}));

        this.currentState = state == null ? (S) config.getContextList().get(0).getSource() : state;

    }

    public ActionResult publish(Event<E> event) {
        Map<E, AbstractContext<S, E>> contextMap = map.get(currentState);

        if (MapUtil.isNotEmpty(contextMap)) {
            AbstractContext<S, E> context = contextMap.get(event.getEvent());

            if (Objects.isNull(context)) {
                return null;
            }

            DefaultAction<S, E> action = context.getAction();

            switch (context.getContextEnum()) {
                case TRANSITION:
                    break;
                case CHOICE:
                    ChoiceContext<S, E> choiceContext = (ChoiceContext<S, E>) context;

                    S targetState;

                    if (choiceContext.getSupplier().get()) {
                        targetState = choiceContext.getYesTarget();
                        action = choiceContext.getYesAction();
                    } else {
                        targetState = choiceContext.getNoTarget();
                        action = choiceContext.getNoAction();
                    }

                    choiceContext.setTarget(targetState);
                    break;
            }


            if (Objects.nonNull(action)) {
                ActionResult actionResult = action.doHandle();
                this.currentState = actionResult.isStatus() ? context.getTarget() : context.getSource();
                return actionResult;
            } else {
                this.currentState = context.getTarget();
            }
        }

        return ActionResult.success();
    }
}
