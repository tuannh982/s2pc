package com.tuannh.s2pc.dto.rpc;

import com.tuannh.s2pc.commons.visitor.Visitor;

@SuppressWarnings("java:S1905")
public interface RpcVisitor extends Visitor<BaseRpc, Boolean> {
    @Override
    default Boolean visit(BaseRpc o) {
        boolean b = preHandle(o);
        if (!b) return false;
        // handle here
        if (!b) return false;
        postHandle(o);
        return true;
    }

    default boolean preHandle(BaseRpc o) {
        return true;
    }
    default void postHandle(BaseRpc o) {}
    // boolean handle(ExampleRpc message);
}
