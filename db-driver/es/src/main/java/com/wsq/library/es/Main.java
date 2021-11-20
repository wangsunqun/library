package com.wsq.library.es;

import java.util.HashMap;

/**
 * TODO
 *
 * @author wsq
 * 2021/5/13 15:16
 */
public class Main {
    public static void main(String[] args) {
        EsClient.save("ttt", new HashMap<String, Object>(){{
            put("name", "wsq");
            put("age", 123);
        }});
    }
}
