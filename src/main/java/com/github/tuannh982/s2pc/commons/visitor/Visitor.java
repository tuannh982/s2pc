package com.github.tuannh982.s2pc.commons.visitor;

public interface Visitor<T, R> {
    R visit(T o);
}
