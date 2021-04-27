package com.wsq.library.statemachine;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
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

    private static Map<String, AbstractConfig<?, ?>> configMap = new LinkedHashMap<>();

    static {
        ServiceLoader<AbstractConfig> configs = ServiceLoader.load(AbstractConfig.class);

        for (AbstractConfig<?, ?> config : configs) {
            AbstractConfig<?, ?> configObj = config.build();
            configMap.put(configObj.getStateMachineName(), configObj);
        }
    }

    public StateMachine(String configName, S state) {
        if (MapUtil.isEmpty(configMap)) {
            throw new RuntimeException("cannot find config");
        }

        AbstractConfig<?, ?> config = StrUtil.isBlank(configName) ? configMap.entrySet().iterator().next().getValue() : configMap.get(configName);
        if (Objects.isNull(config)) {
            throw new RuntimeException("cannot find config by: " + configName);
        }

        config.getContextMap().forEach((k, v) -> {
            Map<E, StateMachineContext<S, E>> contextMap = map.computeIfAbsent((S) v.getSource(), key -> new HashMap<>());
            contextMap.put((E) v.getEvent().getEvent(), (StateMachineContext<S, E>) v);
        });

        this.currentState = state == null ? (S) config.getContextMap().get(0).getSource() : state;

    }

    public ActionResult publish(Event<E> event) {
        Map<E, StateMachineContext<S, E>> contextMap = map.get(currentState);

        if (MapUtil.isNotEmpty(contextMap)) {
            StateMachineContext<S, E> context = contextMap.get(event.getEvent());

            if (Objects.nonNull(context)) {
                DefaultAction<S, E> action = context.getAction();
                if (Objects.isNull(action)) {
                    action = new DefaultAction<>(context);
                }

                return action.doHandle();
            }
        }

        return null;
    }
}
