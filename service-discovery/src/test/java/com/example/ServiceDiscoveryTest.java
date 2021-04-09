package com.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.BDDAssertions.then;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ServiceDiscoveryApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServiceDiscoveryTest {

    private ConfigurableApplicationContext application1;
    private ConfigurableApplicationContext application2;
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    void startApps() {
        application1 = startApp(8001, "Hello");
        application2 = startApp(8002, "Hi");
    }

    @Test
    public void loadbalance() {
        ResponseEntity<String> resp = testRestTemplate.getForEntity("http://localhost:" + port + "/echo/hj", String.class);
        ResponseEntity<String> resp2 = testRestTemplate.getForEntity("http://localhost:" + port + "/echo/hj", String.class);

        List<String> rets = Arrays.asList("Hello hj", "Hi hj");
        if(resp.getBody().equals(rets.get(0))) {
            then(resp2.getBody()).isEqualTo(rets.get(1));
        } else {
            then(resp2.getBody()).isEqualTo(rets.get(0));
        }
    }

    @AfterEach
    void closeApps() {
        application1.close();
        application2.close();
    }

    private ConfigurableApplicationContext startApp(int port, String prefix) {
        return new SpringApplicationBuilder().sources(TestApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .run("--server.port=" + port,
                    "--prefix=" + prefix,
                    "--spring.application.name=my-service",
                    "--spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848",
                    "--logging.level.root=info",
                    "--spring.jmx.enabled=false");
    }

    @Configuration
    @EnableAutoConfiguration
    @RestController
    static class TestApplication {

        @Value("${prefix}")
        private String prefix;

        @RequestMapping(value = "/echo/{string}", method = RequestMethod.GET)
        public String echo(@PathVariable String string) {
            return prefix + " " + string;
        }

    }

}
