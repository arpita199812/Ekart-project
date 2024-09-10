package com.reljicd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.method.annotation.WebMvcRegistrations;
import org.springframework.web.servlet.mvc.method.annotation.WebMvcRegistrationsAdapter;

@SpringBootApplication
public class ShoppingCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingCartApplication.class, args);
    }

    @Bean
    public WebMvcRegistrations webMvcRegistrations() {
        return new WebMvcRegistrationsAdapter();
    }
}
