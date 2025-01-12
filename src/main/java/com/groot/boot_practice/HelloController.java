package com.groot.boot_practice;

import java.util.Objects;

public class HelloController {
    public String hello(String name) {
        HelloService helloService = new HelloService();
        return helloService.sayHello(Objects.requireNonNull(name));
    }
}