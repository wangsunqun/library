package com.wsq.library.util.common.stateMachine;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Setter
@Getter
public class State<S extends Enum<S>> {
    // 状态
    private S state;

    // 该状态下可以执行的任务
    private List<AbstractAction> actionList;

    public State(S state, AbstractAction... actionList) {
        this.state = state;
        this.actionList = Stream.of(actionList).collect(Collectors.toList());
    }

    public void addAction(AbstractAction action) {
        actionList.add(action);
    }
}