package com.wsq.util.common.response;

import com.wsq.util.common.config.ConfigUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import static com.wsq.util.common.response.ResponseStatusEnums.INTERNAL_SERVER_ERROR;
import static com.wsq.util.common.response.ResponseStatusEnums.OK;

/**
 * 统一返回体
 *
 * @author wsq
 * 2021/1/3 12:04
 */
@Data
@AllArgsConstructor
public class Result<T> {
    private int code;
    private T data;
    private String message;

    private static int serviceId;

    static {
        Integer serviceIdInt = ConfigUtils.getInt("service.id");
        if (serviceIdInt == null) {
            throw new RuntimeException("service id 不能为空!!!");
        }

        serviceId = serviceIdInt;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(OK.code, data, null);
    }

    public static <T> Result<T> fail(String message) {
        return fail(INTERNAL_SERVER_ERROR.code, message);
    }

    public static <T> Result<T> fail(ResponseStatusEnums statusEnums) {
        return fail(statusEnums.code, statusEnums.desc);
    }

    // code 必须小于等于6位，如果不足6位会自动前面补零
    public static <T> Result<T> fail(int code, String message) {
        if (code > 999999) {
            throw new IllegalArgumentException("code 必须小于6位");
        }

        return new Result<>(Integer.parseInt(serviceId + String.format("%06d", code)), null, message);
    }
}
