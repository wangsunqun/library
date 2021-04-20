package com.wsq.library.util.common.stateMachine;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActionResult {
    private State nextState;
    private Object data;
}
