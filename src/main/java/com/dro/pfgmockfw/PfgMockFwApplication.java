package com.dro.pfgmockfw;

import com.dro.pfgmockfw.config.MockFwProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(MockFwProperties.class)
public class PfgMockFwApplication {

    public static void main(String[] args) {
        SpringApplication.run(PfgMockFwApplication.class, args);
    }

}
