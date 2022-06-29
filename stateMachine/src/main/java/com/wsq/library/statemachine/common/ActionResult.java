package com.wsq.library.statemachine.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActionResult {
    // 上一个状态
    private Enum<?> lastState;
    // 当前状态
    private Enum<?> currentState;
    private Object data;
}
