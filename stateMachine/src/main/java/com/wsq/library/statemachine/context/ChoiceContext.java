package com.wsq.library.statemachine.context;

import com.wsq.library.statemachine.common.DefaultAction;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.function.Supplier;

/**
 * @author wsq
 * 2021/4/24 22:40
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChoiceContext<S extends Enum<S>, E extends Enum<E>> extends AbstractContext<S, E> {
    private Supplier<Boolean> supplier;
    private S yesTarget;
    private S noTarget;
    private DefaultAction<S, E> yesAction;
    private DefaultAction<S, E> noAction;
}