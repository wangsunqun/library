package com.wsq.library.statemachine;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author wsq
 * 2021/4/24 15:32
 */
@Data
public abstract class AbstractConfig<S extends Enum<S>, E extends Enum<E>> {
    private String stateMachineName;
    private Map<String, StateMachineContext<S, E>> contextMap = new HashMap<>();

    protected abstract AbstractConfig<S, E> build();

    protected AbstractConfig<S, E> stateMachineName(String name) {
        setStateMachineName(name);
        return this;
    }

    protected AbstractConfig<S, E> source(S state) {
        StateMachineContext<S, E> context = getContext();
        context.setSource(state);

        return this;
    }

    protected AbstractConfig<S, E> event(E event) {
        StateMachineContext<S, E> context = getContext();
        context.setEvent(new Event<>(event));

        return this;
    }

    protected AbstractConfig<S, E> target(S state) {
        StateMachineContext<S, E> context = getContext();
        context.setTarget(state);

        return this;
    }

    protected AbstractConfig<S, E> action(DefaultAction<S, E> action) {
        StateMachineContext<S, E> context = getContext();
        action.setContext(context);
        context.setAction(action);

        return this;
    }

    protected Choise choise(Function<Event<E>, Boolean> function) {
        StateMachineContext<S, E> context = getContext();

        return new Choise(function.apply(context.getEvent()), this);
    }

    private StateMachineContext<S, E> getContext() {
        return contextMap.computeIfAbsent(stateMachineName, k -> new StateMachineContext<>());
    }

    @Data
    @AllArgsConstructor
    public class Choise {
        private boolean result;
        AbstractConfig<S, E> config;

        public Choise yes(DefaultAction<S, E> action) {
            if (result && Objects.nonNull(action)) {
                StateMachineContext<S, E> context = config.getContext();
                context.setAction(action);
            }

            return this;
        }

        public Choise no(DefaultAction<S, E> action) {
            if (!result && Objects.nonNull(action)) {
                StateMachineContext<S, E> context = config.getContext();
                context.setAction(action);
            }

            return this;
        }

        public AbstractConfig<S, E> end() {
            return config;
        }
    }
}
