package com.bytefacets.diaspore.grpc;

import static com.bytefacets.diaspore.grpc.receive.ObjectDecoderAccess.decode;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

import com.bytefacets.diaspore.grpc.send.ObjectEncoderAccess;
import com.google.protobuf.ByteString;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class ObjectCodecTest {
    private final ObjectEncoderAccess encoder = new ObjectEncoderAccess();

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void shouldEncodeBool(final Boolean value) {
        final ByteString encoded = encoder.encode(value);
        assertThat(decode(encoded), equalTo(value));
    }

    @ParameterizedTest
    @ValueSource(bytes = {Byte.MIN_VALUE, Byte.MAX_VALUE, 0, -1, 27})
    void shouldEncodeByte(final byte value) {
        final ByteString encoded = encoder.encode(value);
        assertThat(decode(encoded), equalTo(value));
    }

    @ParameterizedTest
    @ValueSource(shorts = {Short.MIN_VALUE, Short.MAX_VALUE, 0, -1, 27})
    void shouldEncodeShort(final short value) {
        final ByteString encoded = encoder.encode(value);
        assertThat(decode(encoded), equalTo(value));
    }

    @ParameterizedTest
    @ValueSource(chars = {Character.MIN_VALUE, Character.MAX_VALUE, 0, 'a', '\r'})
    void shouldEncodeChar(final char value) {
        final ByteString encoded = encoder.encode(value);
        assertThat(decode(encoded), equalTo(value));
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, Integer.MAX_VALUE, 0, -1, 27})
    void shouldEncodeInt(final int value) {
        final ByteString encoded = encoder.encode(value);
        assertThat(decode(encoded), equalTo(value));
    }

    @ParameterizedTest
    @ValueSource(longs = {Long.MIN_VALUE, Long.MAX_VALUE, 0, -1, 27})
    void shouldEncodeLong(final long value) {
        final ByteString encoded = encoder.encode(value);
        assertThat(decode(encoded), equalTo(value));
    }

    @ParameterizedTest
    @ValueSource(floats = {Float.MIN_VALUE, Float.MAX_VALUE, 0, -1, 27.2f})
    void shouldEncodeFloat(final float value) {
        final ByteString encoded = encoder.encode(value);
        assertThat(decode(encoded), equalTo(value));
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.MIN_VALUE, Double.MAX_VALUE, 0, -1, 27.2})
    void shouldEncodeDouble(final double value) {
        final ByteString encoded = encoder.encode(value);
        assertThat(decode(encoded), equalTo(value));
    }

    @ParameterizedTest
    @MethodSource("modelStrings")
    void shouldEncodeString(final String value) {
        final ByteString encoded = encoder.encode(value);
        assertThat(decode(encoded), equalTo(value));
    }

    @Test
    void shouldEncodeNull() {
        final ByteString encoded = encoder.encode(null);
        assertThat(decode(encoded), nullValue());
    }

    private static Stream<Arguments> modelStrings() {
        final StringBuilder sb = new StringBuilder(50);
        return IntStream.of(0, 1, 18, 45)
                .mapToObj(
                        len -> {
                            sb.setLength(0);
                            for (int i = 0; i < len; i++) {
                                sb.append((char) (('a' + i) % 26));
                            }
                            return sb.toString();
                        })
                .map(Arguments::of);
    }
}
