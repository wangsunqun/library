package com.wsq.library.statemachine;

import cn.hutool.core.map.MapUtil;
import com.wsq.library.statemachine.config.AbstractStateMachineConfig;
import lombok.Data;

import java.util.*;

/**
 * 本状态机目标是轻量级，所以不涉及缓存和持久化
 * 使用者可以结合自身需求结合ThreadLocal或者持久化手段进行保存状态机
 *
 * @author wsq
 * 2021/4/22 19:37
 */
@Data
public class StateMachine<S extends Enum<S>, E extends Enum<E>> {
    private S currentState;

    private Map<S, Map<E, StateMachineContext<S, E>>> map = new HashMap<>();

    private static Map<String, AbstractStateMachineConfig<?, ?>> configMap = new LinkedHashMap<>();

    static {
        ServiceLoader<AbstractStateMachineConfig> configs = ServiceLoader.load(AbstractStateMachineConfig.class);

        for (AbstractStateMachineConfig<?, ?> config : configs) {
            AbstractStateMachineConfig<?, ?> configObj = config.build();
            configMap.put(configObj.getStateMachineName(), configObj);
        }
    }

    public StateMachine(String configName, S state) {
        if (MapUtil.isEmpty(configMap)) {
            throw new RuntimeException("cannot find config");
        }

        AbstractStateMachineConfig<?, ?> config = configMap.get(configName);
        if (Objects.isNull(config)) {
            throw new RuntimeException("cannot find config by: " + configName);
        }

        config.getContextList().forEach(context -> map.computeIfAbsent((S) context.getSource(),
                key -> new HashMap<E, StateMachineContext<S, E>>() {{
                    put((E) context.getEvent().getEvent(), (StateMachineContext<S, E>) context);
                }}));

        this.currentState = state == null ? (S) config.getContextList().get(0).getSource() : state;

    }

    public ActionResult publish(Event<E> event) {
        Map<E, StateMachineContext<S, E>> contextMap = map.get(currentState);

        if (MapUtil.isNotEmpty(contextMap)) {
            StateMachineContext<S, E> context = contextMap.get(event.getEvent());

            if (Objects.nonNull(context)) {
                DefaultAction<S, E> action = context.getAction();
                if (Objects.nonNull(action)) {
                    return action.doHandle();
                }
            }
        }

        return null;
    }
}
