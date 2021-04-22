package com.wsq.library.util.common.stateMachine;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;

/**
 * 事件类
 * (继承hashmap是为了可以传递业务参数)
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Event extends HashMap<String, Object> {
    private Enum<?> event;

    public Event(Enum<?> event) {
        this.event = event;
    }
}
