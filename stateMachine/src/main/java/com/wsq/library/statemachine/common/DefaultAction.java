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

        ActionResult actionResult;
        try {
            actionResult = exec(context.getSource(), context.getEvent(), context.getTarget());
        } catch (Exception e) {
            log.error("状态机任务执行失败, event:{}, source:{}, target:{}",
                    context.getEvent(), context.getSource(), context.getTarget(), e);
            actionResult = new ActionResult();
            actionResult.setStatus(false);
        }

        return actionResult;
    }

    public abstract ActionResult exec(S source, Event<E> event, S target);
}