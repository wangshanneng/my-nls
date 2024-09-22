package com.zhiku.nls.business;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;

@Slf4j
@SpringBootApplication
public class BusinessApplication {

    public static void main(String[] args) {

        ConfigurableEnvironment environment = SpringApplication.run(BusinessApplication.class, args).getEnvironment();

        log.info("----------启动成功！----------");
        log.info("访问地址: \thttp://localhost:{}{}", environment.getProperty("server.port"), environment.getProperty("server.servlet.context-path"));
        log.info("----------------------------");
    }

}
