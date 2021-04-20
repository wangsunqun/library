package com.wsq.library.util.common.stateMachine;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class State<S extends Enum<S>, E extends Enum<E>> {
    // 状态
    private S state;

    // 该状态下可以执行的任务
    private Map<Event<E>, AbstractAction<E, S>> actionMap;

    public State(S state, Map<Event<E>, AbstractAction<E, S>> actionMap) {
        this.state = state;
        this.actionMap = actionMap;
    }

    public void addAction(Event<E> event, AbstractAction<E, S> action) {
        actionMap.put(event, action);
    }
}