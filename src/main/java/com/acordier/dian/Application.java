package com.acordier.dian;

import com.acordier.dian.kubernetes.KongProxyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootApplication
public class Application {

    private final KongProxyService kongProxyService;

    @Autowired
    public Application(KongProxyService kongProxyService) {
        this.kongProxyService = kongProxyService;
    }

    @PostConstruct
    private void init() {
        kongProxyService.initialize();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
