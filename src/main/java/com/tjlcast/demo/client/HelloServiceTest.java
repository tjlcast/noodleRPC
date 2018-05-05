package com.tjlcast.demo.client;

import com.tjlcast.demo.common.HelloService;
import com.tjlcast.server.RpcProxy;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by tangjialiang on 2018/5/5.
 *
 * RPC 的客户端
 */

public class HelloServiceTest {

    @Autowired
    private RpcProxy rpcProxy ;

    public void helloTest() {
        HelloService helloService = rpcProxy.create(HelloService.class);

        String world = helloService.hello("world");
    }
}