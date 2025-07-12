package com.bytefacets.diaspore.grpc.receive;

import com.google.protobuf.ByteString;

public final class ObjectDecoderAccess {
    private ObjectDecoderAccess() {}

    public static Object decode(final ByteString value) {
        return ObjectDecoderRegistry.decode(value);
    }
}
