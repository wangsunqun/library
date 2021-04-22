package com.wsq.library.util.common.stateMachine;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class DefaultAction {
    private Enum<?> event;

    private Enum<?> currentState;
    private Enum<?> nextState;

    public DefaultAction(Enum<?> nextState) {
        this.nextState = nextState;
    }

    public ActionResult doHandle() {
        log.info("开始执行状态任务, event:{}, currentState:{}, nextState:{}", event, currentState, nextState);
        Object data = exec(currentState);
        return new ActionResult(nextState, data);
    }

    public Object exec(Enum<?> currentState) {
        return null;
    }
}