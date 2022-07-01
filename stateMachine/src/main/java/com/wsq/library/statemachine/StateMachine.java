package com.wsq.library.statemachine;

import cn.hutool.core.map.MapUtil;
import com.wsq.library.statemachine.common.ActionResult;
import com.wsq.library.statemachine.common.DefaultAction;
import com.wsq.library.statemachine.common.Event;
import com.wsq.library.statemachine.config.AbstractConfig;
import com.wsq.library.statemachine.context.AbstractContext;
import com.wsq.library.statemachine.context.ChoiceContext;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 本状态机目标是轻量级，所以不涉及缓存和持久化
 * 使用者可以结合自身需求结合ThreadLocal或者持久化手段进行保存状态机
 * <p>
 * 使用样例详见本类结尾
 *
 * @author wsq
 * 2021/4/22 19:37
 */
@Data
public class StateMachine<S extends Enum<S>, E extends Enum<E>> {
    private S currentState;

    private Map<S, Map<E, AbstractContext<S, E>>> map = new HashMap<>();

    private static Map<String, AbstractConfig<?, ?>> configMap = new LinkedHashMap<>();

    static {
        ServiceLoader<AbstractConfig> configs = ServiceLoader.load(AbstractConfig.class);

        for (AbstractConfig<?, ?> config : configs) {
            AbstractConfig<?, ?> configObj = config.build();
            configMap.put(configObj.getStateMachineName(), configObj);
        }
    }

    @SuppressWarnings("unchecked")
    public StateMachine(String stateMachineName, S state) {
        if (StringUtils.isBlank(stateMachineName)) {
            throw new RuntimeException("stateMachineName miss");
        }

        if (MapUtil.isEmpty(configMap)) {
            throw new RuntimeException("cannot find config");
        }

        AbstractConfig<?, ?> config = configMap.get(stateMachineName);
        if (Objects.isNull(config)) {
            throw new RuntimeException("cannot find config by: " + stateMachineName);
        }

        config.getContextList().forEach(context -> map.computeIfAbsent((S) context.getSource(),
                key -> new HashMap<E, AbstractContext<S, E>>() {{
                    put((E) context.getEvent().getEvent(), (AbstractContext<S, E>) context);
                }}));

        this.currentState = state == null ? (S) config.getContextList().get(0).getSource() : state;

    }

    public ActionResult publish(Event<E> event) {
        Map<E, AbstractContext<S, E>> contextMap = map.get(currentState);

        if (MapUtil.isNotEmpty(contextMap)) {
            AbstractContext<S, E> context = contextMap.get(event.getEvent());

            if (Objects.isNull(context)) {
                return null;
            }

            DefaultAction<S, E> action = context.getAction();

            switch (context.getContextEnum()) {
                case TRANSITION:
                    break;
                case CHOICE:
                    ChoiceContext<S, E> choiceContext = (ChoiceContext<S, E>) context;

                    S targetState;

                    if (choiceContext.getSupplier().get()) {
                        targetState = choiceContext.getYesTarget();
                        action = choiceContext.getYesAction();
                    } else {
                        targetState = choiceContext.getNoTarget();
                        action = choiceContext.getNoAction();
                    }

                    choiceContext.setTarget(targetState);
                    break;
            }


            if (Objects.nonNull(action)) {
                ActionResult actionResult = action.doHandle();
                this.currentState = actionResult.isStatus() ? context.getTarget() : context.getSource();
                return actionResult;
            } else {
                this.currentState = context.getTarget();
            }
        }

        return ActionResult.success();
    }
}

// 使用样例
//public class Main {
//
//    /*
//     * 使用示例，业务流程图见readme
//     */
//    public static void main(String[] args) {
//        StateMachine<com.netease.shot.exam.stateMachine.test.Main.StateEnum, com.netease.shot.exam.stateMachine.test.Main.EventEnum> machine = new StateMachine<>("wsq", BLANK_FORM);
//
//        ActionResult publish1 = machine.publish(new Event<>(WRITE));
//        if (Objects.nonNull(publish1))
//            System.out.println("1发布write事件，结果: 当前状态: " + publish1.getNextState().name() + ", 返回: " + publish1.getData());
//
//        ActionResult publish2 = machine.publish(new Event<>(CHECK));
//        if (Objects.nonNull(publish2))
//            System.out.println("2发布check事件，结果: 当前状态: " + publish2.getNextState().name() + ", 返回: " + publish2.getData());
//
//
//        ActionResult publish3 = machine.publish(new Event<>(DEAL));
//        if (Objects.nonNull(publish3))
//            System.out.println("3发布deal事件，结果: 当前状态: " + publish3.getNextState().name() + ", 返回: " + publish3.getData());
//
//
//        ActionResult publish4 = machine.publish(new Event<>(SUBMIT));
//        if (Objects.nonNull(publish4))
//            System.out.println("4发布submit事件，结果: 当前状态: " + publish4.getNextState().name() + ", 返回: " + publish4.getData());
//
//
//        ActionResult publish5 = machine.publish(new Event<>(CHECK));
//        if (Objects.nonNull(publish5))
//            System.out.println("5发布check事件，结果: 当前状态: " + publish5.getNextState().name() + ", 返回: " + publish5.getData());
//
//        ActionResult publish6 = machine.publish(new Event<>(SUBMIT));
//        if (Objects.nonNull(publish6))
//            System.out.println("6发布submit事件，结果: 当前状态: " + publish6.getNextState().name() + ", 返回: " + publish6.getData());
//
//        System.out.println();
//    }
//
//    @AllArgsConstructor
//    public enum EventEnum {
//        WRITE, // 填写
//        CHECK,//校验
//        DEAL,//处理
//        SUBMIT // 提交
//    }
//
//    @AllArgsConstructor
//    public enum StateEnum {
//        BLANK_FORM, // 空白表单
//        FULL_FORM, // 填写完表单
//        DEAL_FORM,//待处理表单
//        CONFIRM_FORM, // 校验完表单
//        SUCCESS_FORM,// 成功表单
//        FAILED_FORM//失败表单
//    }
//
//    int i = 0;
//    public boolean check() {
//        if (i == 0) {
//            i++;
//            return false;
//        }
//        return true;
//    }
//
//    public boolean deal() {
//        return true;
//    }
//
//    public static class StateMachineConfig extends AbstractConfig<com.netease.shot.exam.stateMachine.test.Main.StateEnum, com.netease.shot.exam.stateMachine.test.Main.EventEnum> {
//        com.netease.shot.exam.stateMachine.test.Main m = new com.netease.shot.exam.stateMachine.test.Main();
//
//        @Override
//        public AbstractConfig<com.netease.shot.exam.stateMachine.test.Main.StateEnum, com.netease.shot.exam.stateMachine.test.Main.EventEnum> build() {
//            return stateMachineName("wsq").
//                    transition(BLANK_FORM, WRITE, FULL_FORM, new DefaultAction<com.netease.shot.exam.stateMachine.test.Main.StateEnum, com.netease.shot.exam.stateMachine.test.Main.EventEnum>() {
//                        @Override
//                        public Object exec(com.netease.shot.exam.stateMachine.test.Main.StateEnum source, Event<com.netease.shot.exam.stateMachine.test.Main.EventEnum> event, com.netease.shot.exam.stateMachine.test.Main.StateEnum target) {
//                            return "blank_form -> write -> full_form";
//                        }
//                    }).
//                    choice(FULL_FORM, CHECK, m::check).
//                    yes(CONFIRM_FORM, new DefaultAction<com.netease.shot.exam.stateMachine.test.Main.StateEnum, com.netease.shot.exam.stateMachine.test.Main.EventEnum>() {
//                        @Override
//                        public Object exec(com.netease.shot.exam.stateMachine.test.Main.StateEnum source, Event<com.netease.shot.exam.stateMachine.test.Main.EventEnum> event, com.netease.shot.exam.stateMachine.test.Main.StateEnum target) {
//                            return "full_form -> check -> confirm_form";
//                        }
//                    }).
//                    no(DEAL_FORM, new DefaultAction<com.netease.shot.exam.stateMachine.test.Main.StateEnum, com.netease.shot.exam.stateMachine.test.Main.EventEnum>() {
//                        @Override
//                        public Object exec(com.netease.shot.exam.stateMachine.test.Main.StateEnum source, Event<com.netease.shot.exam.stateMachine.test.Main.EventEnum> event, com.netease.shot.exam.stateMachine.test.Main.StateEnum target) {
//                            return "full_form -> check -> deal_form";
//                        }
//                    }).
//                    end().
//                    transition(CONFIRM_FORM, SUBMIT, SUCCESS_FORM, new DefaultAction<com.netease.shot.exam.stateMachine.test.Main.StateEnum, com.netease.shot.exam.stateMachine.test.Main.EventEnum>() {
//                        @Override
//                        public Object exec(com.netease.shot.exam.stateMachine.test.Main.StateEnum source, Event<com.netease.shot.exam.stateMachine.test.Main.EventEnum> event, com.netease.shot.exam.stateMachine.test.Main.StateEnum target) {
//                            return "confirm_form -> submit -> success_form";
//                        }
//                    }).
//                    choice(DEAL_FORM, DEAL, m::deal).
//                    yes(FULL_FORM, new DefaultAction<com.netease.shot.exam.stateMachine.test.Main.StateEnum, com.netease.shot.exam.stateMachine.test.Main.EventEnum>() {
//                        @Override
//                        public Object exec(com.netease.shot.exam.stateMachine.test.Main.StateEnum source, Event<com.netease.shot.exam.stateMachine.test.Main.EventEnum> event, com.netease.shot.exam.stateMachine.test.Main.StateEnum target) {
//                            return "deal_form -> deal -> full_form";
//                        }
//                    }).
//                    no(FAILED_FORM, new DefaultAction<com.netease.shot.exam.stateMachine.test.Main.StateEnum, com.netease.shot.exam.stateMachine.test.Main.EventEnum>() {
//                        @Override
//                        public Object exec(com.netease.shot.exam.stateMachine.test.Main.StateEnum source, Event<com.netease.shot.exam.stateMachine.test.Main.EventEnum> event, com.netease.shot.exam.stateMachine.test.Main.StateEnum target) {
//                            return "deal_form -> deal -> failed_form";
//                        }
//                    }).
//                    end();
//        }
//    }
//}
