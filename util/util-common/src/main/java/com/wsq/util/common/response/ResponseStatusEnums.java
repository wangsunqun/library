package com.wsq.util.common.response;

import lombok.AllArgsConstructor;

/**
 * 返回错误码，服务端报错的错误码由两部分组成，两个数字的服务ID和6个数字的错误码
 *
 * @author wsq
 * 2021/1/3 14:36
 */
@AllArgsConstructor
public enum ResponseStatusEnums {
    /*
     * 系统错误码
     * 错误码 0 ~ 999 为系统使用，为HTTP协议层错误码。若网关后端服务有任何页面、访问错误则返回此类错误码
     */
    OK(200, "请求执行成功"),
    MULTIPLE_CHOICES(3, "被请求的资源有一系列可供选择的回馈信息，每个都有自己特定的地址和浏览器驱动的商议信息。用户或浏览器能够自行选择一个首选的地址进行重定向。"),
    FOUND(302, "请求的资源现在临时从不同的 URI 响应请求。由于这样的重定向是临时的，客户端应当继续向原有地址发送以后的请求"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "认证未通过"),
    FORBIDDEN(403, "拒绝访问页面"),
    NOT_FOUND(404, "页面无法找到"),
    METHOD_NOT_ALLOWED(405, "请求的方法不能被用于请求的资源"),
    NOT_ACCEPTABLE(406, "无法达成请求资源所需的条件"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    NOT_IMPLEMENTED(501, "请求的方法不被服务器支持"),
    BAD_GATEWAY(502, "网关错误"),
    SERVICE_UNAVAILABLE(503, "服务器不可用"),
    GATEWAY_TIMEOUT(504, "网关超时"),
    HTTP_VERSION_NOT_SUPPORTED(505, "服务器不支持请求中的HTTP协议版本");

    /**
     * 通用错误码
     * 错误码 1000 ~ 9999 为业务系统通用错误码，该错误码常见于各种中间件错误、底层库错误等。
     */


    /**
     * 业务错误码
     * 业务错误码从10000 ~ 999999，各业务可根据具体需要进行分配。
     */

    public int code;
    public String desc;
}
