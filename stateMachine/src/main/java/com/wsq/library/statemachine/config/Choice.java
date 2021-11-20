package com.wsq.library.statemachine.config;

import com.wsq.library.statemachine.common.DefaultAction;
import com.wsq.library.statemachine.context.ChoiceContext;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

/**
 * @author wsq
 * @Description
 * @date 2021/11/20 21:10
 */
@Data
@AllArgsConstructor
public class Choice<S extends Enum<S>, E extends Enum<E>> {
    private AbstractConfig<S, E> config;
    private ChoiceContext<S, E> context;

    public Choice<S, E> yes(S targetState) {
        return yes(targetState, null);
    }

    public Choice<S, E> yes(S targetState, DefaultAction<S, E> action) {
        context.setYesTarget(targetState);

        if (Objects.nonNull(action)) {
            action.setContext(context);
            context.setYesAction(action);
        }

        return this;
    }

    public Choice<S, E> no(S targetState) {
        return no(targetState, null);
    }

    public Choice<S, E> no(S targetState, DefaultAction<S, E> action) {
        context.setNoTarget(targetState);

        if (Objects.nonNull(action)) {
            action.setContext(context);
            context.setNoAction(action);
        }

        return this;
    }

    public AbstractConfig<S, E> end() {
        return config;
    }
}
