package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class ServiceRegistrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceRegistrationApplication.class, args);
    }

    @RestController
    class EchoController {

        @Value("prefix")
        private String prefix;

        @RequestMapping(value = "/echo/{string}", method = RequestMethod.GET)
        public String echo(@PathVariable String string) {
            log.info("{} {}", prefix, string);
            return prefix + " " + string;
        }
    }

}
