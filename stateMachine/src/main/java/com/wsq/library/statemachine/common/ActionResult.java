package com.wsq.library.statemachine.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActionResult {
    private boolean status = true;
    private Object data;

    public static ActionResult success() {
        return new ActionResult();
    }

    public static ActionResult success(Object data) {
        ActionResult actionResult = new ActionResult();
        actionResult.setData(data);
        return actionResult;
    }

    public static ActionResult error() {
        ActionResult actionResult = new ActionResult();
        actionResult.setStatus(false);
        return actionResult;
    }
}
