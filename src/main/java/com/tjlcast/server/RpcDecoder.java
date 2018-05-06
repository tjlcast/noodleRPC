package com.tjlcast.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by tangjialiang on 2018/5/3.
 */
public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> genericClass ;

    public RpcDecoder(Class<?> genericClass) {
        this.genericClass = genericClass ;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 首先获取到数据的长度
        if (in.readableBytes() < 4) {
            // 等待数据头(长度)
            return ;
        }
        in.markReaderIndex() ;
        int dataLength = in.readInt() ;
        if (dataLength < 0) {
            ctx.close() ;
        }
        // 等待数据对象全部收到
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex() ;
            return ;
        }
        // 数据齐全可以读取
        byte[] data = new byte[dataLength];
        in.readBytes(data) ;

        Object obj = SerializationUtil.deserialize(data, genericClass);
        out.add(obj);
    }
}
