package com.bytefacets.diaspore.grpc.send;

import com.bytefacets.diaspore.schema.TypeId;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

public final class ObjectEncoderRegistry {
    private static final Map<Class<?>, ObjectEncoder> DEFAULT_REGISTRY = new HashMap<>();

    static {
        DEFAULT_REGISTRY.put(Boolean.class, ObjectEncoderRegistry::encodeBool);
        DEFAULT_REGISTRY.put(Byte.class, ObjectEncoderRegistry::encodeByte);
        DEFAULT_REGISTRY.put(Short.class, ObjectEncoderRegistry::encodeShort);
        DEFAULT_REGISTRY.put(Character.class, ObjectEncoderRegistry::encodeChar);
        DEFAULT_REGISTRY.put(Integer.class, ObjectEncoderRegistry::encodeInteger);
        DEFAULT_REGISTRY.put(Long.class, ObjectEncoderRegistry::encodeLong);
        DEFAULT_REGISTRY.put(Float.class, ObjectEncoderRegistry::encodeFloat);
        DEFAULT_REGISTRY.put(Double.class, ObjectEncoderRegistry::encodeDouble);
        DEFAULT_REGISTRY.put(String.class, ObjectEncoderRegistry::encodeString);
    }

    private ObjectEncoderRegistry() {}

    public static void register(final Class<?> type, final ObjectEncoder encoder) {
        DEFAULT_REGISTRY.put(type, encoder);
    }

    @Nullable
    static ObjectEncoder lookup(@Nullable final Object value) {
        if (value == null) {
            return ObjectEncoderRegistry::encodeNull;
        } else {
            return DEFAULT_REGISTRY.get(value.getClass());
        }
    }

    static void encodeNull(final BufferSupplier buffer, final Object ignored) {
        buffer.beginSystemType((byte) 0, (short) 0);
    }

    private static void encodeBool(final BufferSupplier buffer, final Object value) {
        buffer.beginSystemType(TypeId.Bool, 1).put((byte) (((Boolean) value) ? 1 : 0));
    }

    private static void encodeByte(final BufferSupplier buffer, final Object value) {
        buffer.beginSystemType(TypeId.Byte, 1).put((Byte) value);
    }

    private static void encodeShort(final BufferSupplier buffer, final Object value) {
        buffer.beginSystemType(TypeId.Short, 2).putShort((Short) value);
    }

    private static void encodeChar(final BufferSupplier buffer, final Object value) {
        buffer.beginSystemType(TypeId.Char, 2).putChar((Character) value);
    }

    private static void encodeInteger(final BufferSupplier buffer, final Object value) {
        buffer.beginSystemType(TypeId.Int, 4).putInt((Integer) value);
    }

    private static void encodeLong(final BufferSupplier buffer, final Object value) {
        buffer.beginSystemType(TypeId.Long, 8).putLong((Long) value);
    }

    private static void encodeFloat(final BufferSupplier buffer, final Object value) {
        buffer.beginSystemType(TypeId.Float, 4).putFloat((Float) value);
    }

    private static void encodeDouble(final BufferSupplier buffer, final Object value) {
        buffer.beginSystemType(TypeId.Double, 8).putDouble((Double) value);
    }

    private static void encodeString(final BufferSupplier buffer, final Object value) {
        final var string = value.toString();
        buffer.beginSystemType(TypeId.String, string.length())
                .put(string.getBytes(StandardCharsets.UTF_8));
    }
}
