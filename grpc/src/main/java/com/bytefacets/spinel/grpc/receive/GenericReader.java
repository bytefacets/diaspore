// SPDX-FileCopyrightText: Copyright (c) 2025 Byte Facets
// SPDX-License-Identifier: MIT
package com.bytefacets.spinel.grpc.receive;

import static java.util.Objects.requireNonNull;

import com.bytefacets.spinel.grpc.codec.ObjectDecoderRegistry;
import com.bytefacets.spinel.grpc.proto.DataUpdate;
import com.bytefacets.spinel.grpc.proto.GenericData;
import com.bytefacets.spinel.grpc.proto.ResponseType;
import com.bytefacets.spinel.schema.FieldList;
import com.bytefacets.spinel.schema.GenericWritableField;
import com.bytefacets.spinel.schema.SchemaField;
import com.google.protobuf.ByteString;
import java.util.BitSet;

final class GenericReader implements TypeReader {
    private FieldList fields;
    private BitSet changedFieldIds;

    @Override
    public void setContext(final FieldList fields, final BitSet changedFieldIds) {
        this.fields = requireNonNull(fields, "fields");
        this.changedFieldIds = requireNonNull(changedFieldIds, "changedFieldIds");
    }

    @Override
    public void read(final DataUpdate msg, final ResponseType op) {
        if (msg.getGenericDataCount() != 0) {
            for (int d = 0, len = msg.getGenericDataCount(); d < len; d++) {
                final var data = msg.getGenericData(d);
                if (op.equals(ResponseType.RESPONSE_TYPE_CHG)) {
                    changedFieldIds.set(data.getFieldId());
                }
                readField(msg, data);
            }
        }
    }

    private void readField(final DataUpdate msg, final GenericData data) {
        final int fieldId = data.getFieldId();
        final SchemaField schemaField = fields.fieldAt(fieldId);
        final var field = (GenericWritableField) schemaField.field();
        final int rowCt = msg.getRowsCount();
        for (int i = 0; i < rowCt; i++) {
            final ByteString encoded = data.getValues(i);
            final Object value = ObjectDecoderRegistry.decode(encoded);
            field.setValueAt(msg.getRows(i), value);
        }
    }
}
