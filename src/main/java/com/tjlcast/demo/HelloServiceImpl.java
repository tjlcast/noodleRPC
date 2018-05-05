package com.tjlcast.demo;

import com.tjlcast.server.RpcService;

/**
 * Created by tangjialiang on 2018/5/2.
 */
@RpcService(HelloService.class)
public class HelloServiceImpl implements HelloService {

    public String hello(String name) {
        return "hello " + name ;
    }
}
