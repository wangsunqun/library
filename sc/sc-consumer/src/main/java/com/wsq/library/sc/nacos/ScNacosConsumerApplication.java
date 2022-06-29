package com.wsq.library.sc.nacos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@EnableFeignClients
@RestController
@EnableDiscoveryClient
@SpringBootApplication
public class ScNacosConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScNacosConsumerApplication.class, args);
    }

    @Autowired
    private Feign feign;

    @RequestMapping("c")
    public String test() {
        return feign.test();
    }
}
