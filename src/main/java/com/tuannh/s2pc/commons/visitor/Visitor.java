package com.tuannh.s2pc.commons.visitor;

public interface Visitor<T, R> {
    R visit(T o);
}
