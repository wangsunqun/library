package com.wsq.library.sc.nacos;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author wsq
 * @Description
 * @date 2022/6/19 16:44
 */
@FeignClient("sc-provider")
public interface Feign {
    @RequestMapping("t")
    String test();
}
