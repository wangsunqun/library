package com.wsq.library.statemachine;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActionResult {
    private Enum<?> nextState;
    private Object data;
}
