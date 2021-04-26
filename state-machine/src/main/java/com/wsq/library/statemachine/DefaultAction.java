package com.wsq.library.statemachine;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class DefaultAction<S extends Enum<S>, E extends Enum<E>> {
    private StateMachineContext<S, E> context;

    public DefaultAction(StateMachineContext<S, E> context) {
        this.context = context;
    }

    public ActionResult doHandle() {
        log.info("开始执行状态任务, event:{}, source:{}, target:{}",
                context.getEvent(), context.getSource(), context.getTarget());
        Object data = exec(context.getSource(), context.getEvent());
        return new ActionResult(context.getTarget(), data);
    }

    public Object exec(S source, Event<E> event) {
        return null;
    }
}