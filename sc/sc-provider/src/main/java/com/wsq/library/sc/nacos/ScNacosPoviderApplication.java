package com.wsq.library.sc.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableDiscoveryClient
@SpringBootApplication
public class ScNacosPoviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScNacosPoviderApplication.class, args);
    }

    @RequestMapping("t")
    public String test() {
        return "a";
    }
}
