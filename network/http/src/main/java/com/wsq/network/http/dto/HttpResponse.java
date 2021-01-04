package com.wsq.network.http.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * http返回实体类
 *
 * @author wsq
 * 2021/1/3 11:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpResponse<T> {
    private Integer httpStatus;
    private T body;
    private Long execTime;
    private String errMsg;
}
