package com.github.tuannh982.s2pc.commons.visitor;

public interface Acceptor<T, R> {
    R accept(Visitor<T, R> visitor);
}
