package com.wsq.library.util.common.stateMachine;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractAction<E extends Enum<E>, S extends Enum<S>> {
    private Event<E> event;

    private State<S, E> currentState;
    private State<S, E> nextState;

    public AbstractAction(Event<E> event, State<S, E> currentState, State<S, E> nextState) {
        this.event = event;
        this.currentState = currentState;
        this.nextState = nextState;
    }

    public ActionResult doHandle() {
        log.info("开始执行状态任务, event:{}, currentState:{}, nextState:{}", event, currentState, nextState);
        Object data = exec(currentState);
        return new ActionResult(nextState, data);
    }

    public abstract Object exec(State<S, E> currentState);
}

