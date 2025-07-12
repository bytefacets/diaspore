package com.bytefacets.diaspore.grpc.send;

import com.google.protobuf.ByteString;

public final class ObjectEncoderAccess {
    private final ObjectEncoderImpl encoder = new ObjectEncoderImpl();

    public ByteString encode(final Object value) {
        return encoder.encode(value);
    }
}
