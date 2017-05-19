package com.github.wuzguo.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.github.wuzguo.server.cluster", "com.github.wuzguo.server"})
@SpringBootApplication
public class ServerApplication {

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(final Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static Object getBean(final String name) {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(final String name, final Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }


    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
