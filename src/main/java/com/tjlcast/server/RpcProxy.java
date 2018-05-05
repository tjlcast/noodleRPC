package com.tjlcast.server;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * Created by tangjialiang on 2018/5/4.
 */
public class RpcProxy {

    private String serverAddress ;
    private ServiceDiscovery serviceDiscovery ;

    public RpcProxy(String serverAddress) {
        this.serverAddress = serverAddress ;
    }

    public RpcProxy(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery ;
    }

    @SuppressWarnings("unchecked")
    public<T> T create(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        RpcRequest rpcRequest = new RpcRequest() ;
                        rpcRequest.setRequestId(UUID.randomUUID().toString()) ;
                        rpcRequest.setClassName(method.getDeclaringClass().getName());
                        rpcRequest.setMethodName(method.getName());
                        rpcRequest.setParameterTypes(method.getParameterTypes());
                        rpcRequest.setParameters(args);

                        if (serverAddress != null) {
                            serverAddress = serviceDiscovery.discover() ; // 服务发现
                        }

                        String[] array = serverAddress.split(":") ;
                        String host = array[0] ;
                        int port = Integer.parseInt(array[1]) ;

                        RpcClient client = new RpcClient(host, port) ;
                        RpcResponse response = client.send(rpcRequest);

                        if (response.isError()) {
                            throw response.getError() ;
                        } else {
                            return response.getResult() ;
                        }
                    }
                }
        ) ;
    }
}
