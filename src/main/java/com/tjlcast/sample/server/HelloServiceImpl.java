package com.tjlcast.sample.server;

import com.tjlcast.sample.common.HelloService;
import com.tjlcast.server.RpcService;

/**
 * Created by tangjialiang on 2018/5/2.
 *
 * 实现 RPC 的接口具体实现
 */
@RpcService(HelloService.class) // 指定远程接口
public class HelloServiceImpl implements HelloService {

    public String hello(String name) {
        return "hello " + name ;
    }
}
