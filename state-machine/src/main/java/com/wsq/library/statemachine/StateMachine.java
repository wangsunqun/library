package com.wsq.library.statemachine;

import lombok.Data;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
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

    public StateMachine<S, E> build(String name, S state) {
        if (MapUtils.isEmpty(configMap)) {
            throw new RuntimeException("cannot find config");
        }

        AbstractConfig<?, ?> config = StringUtils.isBlank(name) ? configMap.entrySet().iterator().next().getValue() : configMap.get(name);
        if (Objects.isNull(config)) {
            throw new RuntimeException("cannot find config by: " + name);
        }

        config.getContextMap().forEach((k, v) -> {
            Map<E, StateMachineContext<S, E>> contextMap = map.computeIfAbsent((S) v.getSource(), key -> new HashMap<>());
            contextMap.put((E) v.getEvent().getEvent(), (StateMachineContext<S, E>) v);
        });

        this.currentState = state == null ? (S) config.getContextMap().get(0).getSource() : state;

        return this;
    }

    public ActionResult publish(Event<E> event) {
        Map<E, StateMachineContext<S, E>> contextMap = map.get(currentState);

        if (MapUtils.isNotEmpty(contextMap)) {
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
