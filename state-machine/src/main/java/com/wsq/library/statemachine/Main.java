package com.wsq.library.statemachine;

import lombok.AllArgsConstructor;


/**
 * @author wsq
 * 2021/4/22 13:49
 */
public class Main {
    public static void main(String[] args) {
        StateMachine<Main.StateEnum, Main.EventEnum> stateMachine = new StateMachine<>();
        StateMachine<Main.StateEnum, Main.EventEnum> machine = stateMachine.build("wsq", null);

        ActionResult publish = machine.publish(new Event<>(Main.EventEnum.PASS));

        System.out.println();
    }

    @AllArgsConstructor
    public enum EventEnum {
        PASS(1),
        UN_PASS(2);

        public int id;
    }

    @AllArgsConstructor
    public enum StateEnum {
        INIT(1),
        SUCCESS(2),
        FAIL(3);

        public int id;
    }

    public static class Config extends AbstractConfig<StateEnum, EventEnum> {

        @Override
        protected AbstractConfig<StateEnum, EventEnum> build() {
            return stateMachineName("wsq").
                    source(StateEnum.INIT).
                    event(EventEnum.PASS).
                    target(StateEnum.SUCCESS).
                    choise(event -> true).
                    yes(null).
                    no(null).
                    end();
        }
    }
}
