package com.wsq.library.util.common.stateMachine;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActionResult {
    private Enum<?> nextState;
    private Object data;
}
