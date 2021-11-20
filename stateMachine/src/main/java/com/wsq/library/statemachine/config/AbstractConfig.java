package com.wsq.library.statemachine.config;

import com.wsq.library.statemachine.common.DefaultAction;
import com.wsq.library.statemachine.common.Event;
import com.wsq.library.statemachine.context.AbstractContext;
import com.wsq.library.statemachine.context.ChoiceContext;
import com.wsq.library.statemachine.context.ContextEnum;
import com.wsq.library.statemachine.context.TransitionContext;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static com.wsq.library.statemachine.context.ContextEnum.CHOICE;
import static com.wsq.library.statemachine.context.ContextEnum.TRANSITION;

/**
 * @author wsq
 * @Description
 * @date 2021/11/17 15:42
 */
@Data
public abstract class AbstractConfig<S extends Enum<S>, E extends Enum<E>> {
    private String stateMachineName;
    private List<AbstractContext<S, E>> contextList = new ArrayList<>();

    //============================= 全局配置 =============================//
    protected AbstractConfig<S, E> stateMachineName(String name) {
        setStateMachineName(name);
        return this;
    }

    //============================= 普通配置 =============================//
    public AbstractConfig<S, E> transition(S sourceState, E event, S targetState) {
        return transition(sourceState, event, targetState, null);
    }

    public AbstractConfig<S, E> transition(S sourceState, Event<E> event, S targetState) {
        return transition(sourceState, event, targetState, null);
    }

    public AbstractConfig<S, E> transition(S sourceState, E event, S targetState, DefaultAction<S, E> action) {
        TransitionContext<S, E> context = (TransitionContext<S, E>) getContext(TRANSITION);
        context.setSource(sourceState);
        context.setEvent(new Event<>(event));
        context.setTarget(targetState);

        if (Objects.nonNull(action)) {
            action.setContext(context);
            context.setAction(action);
        }

        return this;
    }

    public AbstractConfig<S, E> transition(S sourceState, Event<E> event, S targetState, DefaultAction<S, E> action) {
        TransitionContext<S, E> context = (TransitionContext<S, E>) getContext(TRANSITION);
        context.setSource(sourceState);
        context.setEvent(event);
        context.setTarget(targetState);

        if (Objects.nonNull(action)) {
            action.setContext(context);
            context.setAction(action);
        }

        return this;
    }

    //============================= 选择配置 =============================//
    public Choice<S, E> choice(S sourceState, E event, Supplier<Boolean> supplier) {
        return choice(sourceState, new Event<>(event), supplier);
    }

    public Choice<S, E> choice(S sourceState, Event<E> event, Supplier<Boolean> supplier) {
        ChoiceContext<S, E> context = (ChoiceContext<S, E>) getContext(CHOICE);
        context.setSource(sourceState);
        context.setEvent(event);
        context.setSupplier(supplier);

        return new Choice<>(this, context);
    }

    protected AbstractContext<S, E> getContext(ContextEnum contextEnum) {
        AbstractContext<S, E> context = null;

        switch (contextEnum) {
            case TRANSITION:
                context = new TransitionContext<>();
                context.setContextEnum(TRANSITION);
                break;
            case CHOICE:
                context = new ChoiceContext<>();
                context.setContextEnum(CHOICE);
                break;
        }

        contextList.add(context);
        return context;
    }

    public abstract AbstractConfig<S, E> build();
}
