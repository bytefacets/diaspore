// SPDX-FileCopyrightText: Copyright (c) 2025 Byte Facets
// SPDX-License-Identifier: MIT
package com.bytefacets.diaspore.transform;

import static java.util.Objects.requireNonNull;

import java.util.function.Supplier;

public final class TransformNodeImpl<T> implements TransformNode<T> {
    private final Supplier<String> nameSupplier;
    private final Supplier<T> operatorSupplier;

    private TransformNodeImpl(
            final Supplier<String> nameSupplier, final Supplier<T> operatorSupplier) {
        this.nameSupplier = requireNonNull(nameSupplier, "nameSupplier");
        this.operatorSupplier = requireNonNull(operatorSupplier, "operatorSupplier");
    }

    public static <T> TransformNode<T> transformNode(
            final Supplier<String> nameSupplier, final Supplier<T> operatorSupplier) {
        return new TransformNodeImpl<>(nameSupplier, operatorSupplier);
    }

    @Override
    public T operator() {
        return operatorSupplier.get();
    }

    @Override
    public String name() {
        return nameSupplier.get();
    }
}
