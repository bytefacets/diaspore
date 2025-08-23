// SPDX-FileCopyrightText: Copyright (c) 2025 Byte Facets
// SPDX-License-Identifier: MIT
package com.bytefacets.spinel.grpc.send;

import java.nio.ByteBuffer;

/**
 * @see com.bytefacets.spinel.grpc.codec.ObjectEncoder
 */
public interface BufferSupplier {

    default ByteBuffer beginSystemType(byte systemTypeId, int lengthOfValue) {
        throw new UnsupportedOperationException("beginSystemType not supported");
    }

    /**
     * For implementers of ObjectEncoder, this will return you a positioned and sized ByteBuffer
     *
     * @param userTypeId your type identifier
     * @param lengthOfValue the length you need for encoding your value
     * @return a ByteBuffer in which the encoding should be written
     */
    ByteBuffer beginUserType(byte userTypeId, int lengthOfValue);
}
