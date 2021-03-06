package com.tjlcast.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by tangjialiang on 2018/5/4.
 */
public class RpcEncoder extends MessageToByteEncoder {

    private Class<?> genericClass ;

    public RpcEncoder(Class<?> genericClass) {
        this.genericClass = genericClass ;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        if (genericClass.isInstance(in)) {
            byte[] data = SerializationUtil.serialize(in); // Rpc response 2 bytes
            out.writeInt(data.length) ;
            out.writeBytes(data) ;
        }
    }
}
