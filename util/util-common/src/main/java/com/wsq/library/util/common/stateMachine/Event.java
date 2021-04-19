package com.wsq.library.util.common.stateMachine;

import java.util.HashMap;

/**
 * 事件类
 * (继承hashmap是为了可以传递业务参数)
 */
public class Event<E extends Enum<E>> extends HashMap<String, Object> {
    private E event;

    public Event(E event) {
        this.event = event;
    }
}
