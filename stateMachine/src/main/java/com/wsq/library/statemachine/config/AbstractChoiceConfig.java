package com.wsq.library.statemachine.config;

import com.wsq.library.statemachine.DefaultAction;
import com.wsq.library.statemachine.Event;
import com.wsq.library.statemachine.StateMachineContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author wsq
 * @Description
 * @date 2021/11/17 15:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractChoiceConfig<S extends Enum<S>, E extends Enum<E>> extends AbstractConfig<S, E> {
    private boolean result;

    public AbstractChoiceConfig<S, E> choice(S sourceState, Event<E> event, Supplier<Boolean> supplier) {
        StateMachineContext<S, E> context = getContext();
        context.setSource(sourceState);
        context.setEvent(event);
        this.result = supplier.get();
        return this;
    }

    public AbstractChoiceConfig<S, E> yes(AbstractConfig<S, E> config) {
        if (result) {
            List<StateMachineContext<S, E>> contextList = config.getContextList();
            if (CollectionUtils.isEmpty(contextList)) {
                throw new RuntimeException("yes 分支后续逻辑为空");
            }

            List<StateMachineContext<S, E>> machineContexts = super.getContextList();
            StateMachineContext<S, E> currentContext = machineContexts.get(machineContexts.size() - 1);
            currentContext.setTarget(contextList.get(0).getTarget());

            machineContexts.addAll(contextList);
        }

        return this;
    }

    public AbstractChoiceConfig<S, E> no(AbstractConfig<S, E> config) {
        if (!result) {
            List<StateMachineContext<S, E>> contextList = config.getContextList();
            if (CollectionUtils.isEmpty(contextList)) {
                throw new RuntimeException("no 分支后续逻辑为空");
            }

            List<StateMachineContext<S, E>> machineContexts = super.getContextList();
            StateMachineContext<S, E> currentContext = machineContexts.get(machineContexts.size() - 1);
            currentContext.setTarget(contextList.get(0).getTarget());

            machineContexts.addAll(contextList);
        }

        return this;
    }
}
