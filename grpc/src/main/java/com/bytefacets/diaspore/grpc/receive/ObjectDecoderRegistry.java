package com.bytefacets.diaspore.grpc.receive;

import com.bytefacets.collections.types.Pack;
import com.bytefacets.diaspore.schema.TypeId;
import com.google.protobuf.ByteString;
import io.grpc.netty.shaded.io.netty.util.collection.ShortObjectHashMap;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public final class ObjectDecoderRegistry {
    private static final ShortObjectHashMap<ObjectDecoder> registry = new ShortObjectHashMap<>();

    static {
        registry.put(sysKey(TypeId.Bool), ObjectDecoderRegistry::decodeBool);
        registry.put(sysKey(TypeId.Byte), ObjectDecoderRegistry::decodeByte);
        registry.put(sysKey(TypeId.Short), ObjectDecoderRegistry::decodeShort);
        registry.put(sysKey(TypeId.Char), ObjectDecoderRegistry::decodeChar);
        registry.put(sysKey(TypeId.Int), ObjectDecoderRegistry::decodeInt);
        registry.put(sysKey(TypeId.Long), ObjectDecoderRegistry::decodeLong);
        registry.put(sysKey(TypeId.Float), ObjectDecoderRegistry::decodeFloat);
        registry.put(sysKey(TypeId.Double), ObjectDecoderRegistry::decodeDouble);
        registry.put(sysKey(TypeId.String), ObjectDecoderRegistry::decodeString);
    }

    private ObjectDecoderRegistry() {}

    public void register(final byte userTypeId, final ObjectDecoder decoder) {
        registry.put(key(TypeId.Generic, userTypeId), decoder);
    }

    static Object decode(final ByteString encoded) {
        final byte systemTypeId = encoded.byteAt(0);
        final byte userTypeId = encoded.byteAt(1);
        final ObjectDecoder decoder = registry.get(key(systemTypeId, userTypeId));
        if (decoder == null) {
            return null;
        } else {
            final ByteBuffer buffer = encoded.asReadOnlyByteBuffer();
            buffer.position(buffer.position() + 2);
            return decoder.decode(buffer);
        }
    }

    private static short sysKey(final byte systemType) {
        return Pack.packToShort((byte) 0, systemType);
    }

    private static short key(final byte systemType, final byte userType) {
        return Pack.packToShort(userType, systemType);
    }

    private static Object decodeBool(final ByteBuffer buffer) {
        return buffer.get() == 1;
    }

    private static Object decodeByte(final ByteBuffer buffer) {
        return buffer.get();
    }

    private static Object decodeShort(final ByteBuffer buffer) {
        return buffer.getShort();
    }

    private static Object decodeChar(final ByteBuffer buffer) {
        return buffer.getChar();
    }

    private static Object decodeInt(final ByteBuffer buffer) {
        return buffer.getInt();
    }

    private static Object decodeLong(final ByteBuffer buffer) {
        return buffer.getLong();
    }

    private static Object decodeFloat(final ByteBuffer buffer) {
        return buffer.getFloat();
    }

    private static Object decodeDouble(final ByteBuffer buffer) {
        return buffer.getDouble();
    }

    private static Object decodeString(final ByteBuffer buffer) {
        final byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
