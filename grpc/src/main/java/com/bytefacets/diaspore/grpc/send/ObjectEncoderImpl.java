package com.bytefacets.diaspore.grpc.send;

import com.bytefacets.diaspore.schema.TypeId;
import com.google.protobuf.ByteString;
import java.nio.ByteBuffer;

final class ObjectEncoderImpl {
    private static final int HEADER_SIZE = 2;
    private final InternalBufferSupplier bufferSupplier = new InternalBufferSupplier();
    private ByteBuffer buffer = ByteBuffer.allocate(64);

    ByteString encode(final Object value) {
        final ObjectEncoder encoder = ObjectEncoderRegistry.lookup(value);
        if (encoder != null) {
            buffer.clear();
            encoder.encode(bufferSupplier, value);
            return ByteString.copyFrom(buffer.flip());
        } else {
            return ByteString.EMPTY;
        }
    }

    private final class InternalBufferSupplier implements BufferSupplier {
        @Override
        public ByteBuffer beginSystemType(final byte systemTypeId, final int lengthOfValue) {
            ensureSize(lengthOfValue);
            buffer.put(systemTypeId);
            buffer.put((byte) 0);
            return buffer;
        }

        @Override
        public ByteBuffer beginUserType(final byte userTypeId, final int lengthOfValue) {
            ensureSize(lengthOfValue);
            buffer.put(TypeId.Generic);
            buffer.put(userTypeId);
            return buffer;
        }

        private void ensureSize(final int lengthOfValue) {
            final int additionalPlusHeader = lengthOfValue + HEADER_SIZE;
            final int minSize = buffer.position() + additionalPlusHeader;
            if (minSize >= buffer.capacity()) {
                final int nextSize = Math.max(minSize, buffer.capacity() * 2);
                final ByteBuffer newBuffer = ByteBuffer.allocate(nextSize);
                newBuffer.put(buffer.flip());
                buffer = newBuffer;
            }
        }
    }
}
