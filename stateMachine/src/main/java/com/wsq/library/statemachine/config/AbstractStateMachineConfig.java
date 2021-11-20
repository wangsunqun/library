package com.wsq.library.statemachine.config;

/**
 * @author wsq
 * 2021/4/24 15:32
 */

public abstract class AbstractStateMachineConfig<S extends Enum<S>, E extends Enum<E>> extends AbstractChoiceConfig<S, E> {
    public abstract AbstractStateMachineConfig<S, E> build();
}
