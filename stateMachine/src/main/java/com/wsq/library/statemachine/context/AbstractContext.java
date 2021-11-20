package com.wsq.library.statemachine.context;

import com.wsq.library.statemachine.common.DefaultAction;
import com.wsq.library.statemachine.common.Event;
import lombok.Data;

/**
 * @author wsq
 * @Description
 * @date 2021/11/20 18:28
 */
@Data
public abstract class AbstractContext<S extends Enum<S>, E extends Enum<E>> {
    private ContextEnum contextEnum;
    private S source;
    private Event<E> event;
    private S target;
    private DefaultAction<S, E> action;
}
