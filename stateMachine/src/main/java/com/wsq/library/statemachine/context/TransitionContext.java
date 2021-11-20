package com.wsq.library.statemachine.context;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wsq
 * 2021/4/24 22:40
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TransitionContext<S extends Enum<S>, E extends Enum<E>> extends AbstractContext<S, E> {
}
