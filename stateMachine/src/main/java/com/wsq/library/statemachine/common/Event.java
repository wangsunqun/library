package com.wsq.library.statemachine.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;

/**
 * 事件类
 * (继承hashmap是为了可以传递业务参数)
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Event<E extends Enum<E>> extends HashMap<String, Object> {
    private E event;

    public Event(E event) {
        this.event = event;
    }

    @Override
    public Event<E> put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
