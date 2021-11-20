//package com.wsq.library.statemachine.test;
//
//import com.wsq.library.statemachine.*;
//import com.wsq.library.statemachine.config.AbstractStateMachineConfig;
//import lombok.AllArgsConstructor;
//
//
///**
// * 测试该类需要在resources下建立spi
// *
// * @author wsq
// * 2021/4/22 13:49
// */
//public class Main {
//    public static void main(String[] args) {
//        StateMachine<StateEnum, EventEnum> machine = new StateMachine<>("wsq", StateEnum.INIT);
//
//        ActionResult publish = machine.publish(new Event<>(EventEnum.PASS));
//
//        System.out.println();
//    }
//
//    @AllArgsConstructor
//    public enum EventEnum {
//        START,
//        PAUSED_RESUME,
//        STOP,
//        FINISH,
//        RESET,
//        DESTROY;
//    }
//
//    @AllArgsConstructor
//    public enum StateEnum {
//        READY,
//        RUNNING,
//        PAUSED,
//        STOPPED,
//        FINISHED,
//        NULL;
//    }
//
//    public static class StateMachineConfig extends AbstractStateMachineConfig<StateEnum, EventEnum> {
//
//        @Override
//        protected AbstractStateMachineConfig<StateEnum, EventEnum> build() {
//            return stateMachineName("wsq").
//                    transition(StateEnum.READY, EventEnum.START, StateEnum.RUNNING, new DefaultAction<StateEnum, EventEnum>() {
//                        @Override
//                        public Object exec(StateEnum source, Event<EventEnum> event, StateEnum target) {
//                            return "ready -> start -> running";
//                        }
//                    }).
//
//                    transition(StateEnum.RUNNING, EventEnum.PAUSED_RESUME, StateEnum.PAUSED, new DefaultAction<StateEnum, EventEnum>() {
//                        @Override
//                        public Object exec(StateEnum source, Event<EventEnum> event, StateEnum target) {
//                            return "running -> paused_resume -> paused";
//                        }
//                    }).
//
//                    transition(StateEnum.PAUSED, EventEnum.PAUSED_RESUME, StateEnum.RUNNING, new DefaultAction<StateEnum, EventEnum>() {
//                        @Override
//                        public Object exec(StateEnum source, Event<EventEnum> event, StateEnum target) {
//                            return "paused -> paused_resume -> running";
//                        }
//                    }).
//
//                    transition(StateEnum.RUNNING, new Event<>(EventEnum.STOP).put("flag", true), StateEnum.STOPPED,
//                            new DefaultAction<StateEnum, EventEnum>() {
//                                @Override
//                                public Object exec(StateEnum source, Event<EventEnum> event, StateEnum target) {
//                                    return "running -> paused_resume -> stopped";
//                                }
//                            }).
//                    choice(event -> (Boolean) event.get("flag")).
//                    yes(new DefaultAction<StateEnum, EventEnum>() {
//                        @Override
//                        public Object exec(StateEnum source, Event<EventEnum> event, StateEnum target) {
//                            return null;
//                        }
//                    }).
//                    no(null).
//
//
//                    end();
//        }
//    }
//}
