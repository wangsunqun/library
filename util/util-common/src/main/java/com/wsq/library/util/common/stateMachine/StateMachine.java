package com.wsq.library.util.common.stateMachine;

import lombok.Data;

/**
 * @author wsq
 * 2021/4/22 19:37
 */
@Data
public class StateMachine {
    private State state;

    public ActionResult publish(Event event) {
        DefaultAction defaultAction = state.getActionMap().get(event.getEvent());

        if (defaultAction == null) {
            return null;
        }

        return defaultAction.doHandle();
    }
}
