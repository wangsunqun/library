package com.wsq.library.statemachine.common;

import com.wsq.library.statemachine.context.AbstractContext;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor
public abstract class DefaultAction<S extends Enum<S>, E extends Enum<E>> {
    private AbstractContext<S, E> context;

    public DefaultAction(AbstractContext<S, E> context) {
        this.context = context;
    }

    public ActionResult doHandle() {
        log.info("开始执行状态任务, event:{}, source:{}, target:{}",
                context.getEvent(), context.getSource(), context.getTarget());
        Object data = exec(context.getSource(), context.getEvent(), context.getTarget());
        return new ActionResult(context.getSource(), context.getTarget(), data);
    }

    public abstract Object exec(S source, Event<E> event, S target);
}