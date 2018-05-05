package com.tjlcast.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tangjialiang on 2018/5/4.
 */
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcClient.class) ;

    private String host ;
    private int port ;

    private RpcResponse response ;

    private final Object obj = new Object() ;

    public RpcClient(String host, int port) {
        this.host = host ;
        this.port = port ;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) throws Exception {
        this.response = response ;

        synchronized (obj) {
            obj.notifyAll(); // 收到响应，唤醒线程
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("client caught exception", cause);
        ctx.close() ;
    }


    public RpcResponse send(RpcRequest request) throws Exception {
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new RpcEncoder(RpcRequest.class)) // 将rpc请求进行编码
                                    .addLast(new RpcDecoder(RpcResponse.class)) // 将rpc响应进行解码
                                    .addLast(RpcClient.this) ; // 使用RpcClient发送
                        }
                    })
                    .option(ChannelOption.SO_KEEPALIVE, true) ;

            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().writeAndFlush(request).sync() ;

            synchronized (obj) {
                obj.wait(); // 未收到响应，使线程等待
            }

            if (response != null) {
                future.channel().closeFuture().sync();
            }
            return response;
        } finally {
            group.shutdownGracefully() ;
        }
    }
}
