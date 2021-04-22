package com.wsq.library.util.common.stateMachine;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class State {
    // 状态
    private Enum<?> state;

    // 该状态下可以执行的任务
    private Map<Enum<?>, DefaultAction> actionMap = new HashMap<>();

    public State(Enum<?> state) {
        this.state = state;
    }

    public void addAction(Enum<?> event, DefaultAction action) {
        action.setEvent(event);
        action.setCurrentState(state);
        actionMap.put(event, action);
    }
}