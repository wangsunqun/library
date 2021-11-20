package com.wsq.library.statemachine.config;

import com.wsq.library.statemachine.DefaultAction;
import com.wsq.library.statemachine.Event;
import com.wsq.library.statemachine.StateMachineContext;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author wsq
 * @Description
 * @date 2021/11/17 15:42
 */
@Data
public abstract class AbstractConfig<S extends Enum<S>, E extends Enum<E>> {
    private String stateMachineName;
    private List<StateMachineContext<S, E>> contextList = new ArrayList<>();

    protected AbstractConfig<S, E> stateMachineName(String name) {
        setStateMachineName(name);
        return this;
    }

    public AbstractConfig<S, E> transition(S sourceState, E event, S targetState) {
        return transition(sourceState, event, targetState, null);
    }

    public AbstractConfig<S, E> transition(S sourceState, Event<E> event, S targetState) {
        return transition(sourceState, event, targetState, null);
    }

    public AbstractConfig<S, E> transition(S sourceState, E event, S targetState, DefaultAction<S, E> action) {
        StateMachineContext<S, E> context = getContext();
        context.setSource(sourceState);
        context.setEvent(new com.wsq.library.statemachine.Event<>(event));
        context.setTarget(targetState);

        if (Objects.nonNull(action)) {
            action.setContext(context);
            context.setAction(action);
        }

        return this;
    }

    public AbstractConfig<S, E> transition(S sourceState, com.wsq.library.statemachine.Event<E> event, S targetState, DefaultAction<S, E> action) {
        StateMachineContext<S, E> context = getContext();
        context.setSource(sourceState);
        context.setEvent(event);
        context.setTarget(targetState);

        if (Objects.nonNull(action)) {
            action.setContext(context);
            context.setAction(action);
        }

        return this;
    }

    protected StateMachineContext<S, E> getContext() {
        StateMachineContext<S, E> context = new StateMachineContext<>();
        contextList.add(context);
        return context;
    }
}
