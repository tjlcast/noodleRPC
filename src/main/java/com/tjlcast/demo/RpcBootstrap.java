package com.tjlcast.demo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by tangjialiang on 2018/5/2.
 */
public class RpcBootstrap {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("spring.xml") ;
    }
}
