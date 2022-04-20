package com.wsq.library.pay.core;

import com.wsq.library.pay.core.enums.PayTypeEnum;

import java.util.HashMap;
import java.util.Map;

public class PayFactory {
    private static final Map<PayTypeEnum, PayApi> map = new HashMap<>();

    public static PayApi get(PayTypeEnum payType) {
        return map.get(payType);
    }

    public static void put(PayTypeEnum payType, PayApi payApi) {
        map.put(payType, payApi);
    }
}
