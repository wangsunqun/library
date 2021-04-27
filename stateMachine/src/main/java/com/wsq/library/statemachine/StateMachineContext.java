package com.wsq.library.statemachine;

import lombok.Data;

/**
 * @author wsq
 * 2021/4/24 22:40
 */
@Data
public class StateMachineContext<S extends Enum<S>, E extends Enum<E>> {
    private S source;
    private Event<E> event;

    private S target;
    private DefaultAction<S, E> action;
}
