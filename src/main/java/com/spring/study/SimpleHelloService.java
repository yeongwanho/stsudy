package com.spring.study;

public class SimpleHelloService implements HelloService {
    @Override
    public String sayHello(String name){
        return name;
    }
}
