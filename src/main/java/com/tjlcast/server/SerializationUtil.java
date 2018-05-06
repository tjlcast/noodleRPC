package com.tjlcast.server;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tangjialiang on 2018/5/3.
 *
 * base on protostuff to seria obj.
 *
 * obj 2 byte[]
 */
public class SerializationUtil {

    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>() ;

    private static Objenesis objenesis = new ObjenesisStd(true) ;

    private SerializationUtil() {
    }

    private static <T> Schema<T> getSchema(Class<T> cls) {
        Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(cls) ;
            if (schema != null) {
                cachedSchema.put(cls, schema) ;
            }
        }
        return schema ;
    }

    public static <T> byte[] serialize(T obj) {
        Class<T> cls = (Class<T>) obj.getClass() ;
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

        try {
            Schema<T> schema = (Schema<T>) getSchema(cls);
            byte[] bytes = ProtobufIOUtil.toByteArray(obj, schema, buffer);
            return bytes ;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e) ;
        } finally {
            buffer.clear() ;
        }
    }

    public static <T> T deserialize(byte[] data, Class<T> cls) {
        try {
            T message = objenesis.newInstance(cls) ;
            Schema<T> schema = (Schema<T>) getSchema(cls);
            ProtobufIOUtil.mergeFrom(data, message, schema);
            return message ;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e) ;
        }
    }
}
