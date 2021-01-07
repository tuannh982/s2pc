package com.tuannh.s2pc.dto.rpc;

import com.tuannh.s2pc.commons.visitor.Visitor;

@SuppressWarnings("java:S1905")
public interface RpcVisitor extends Visitor<BaseRpc, Boolean> {
    @Override
    default Boolean visit(BaseRpc o) {
        boolean b = preHandle(o);
        if (!b) return false;
        if (o instanceof Command) {
            b = handle((Command) o);
        } else if (o instanceof CommandResponse) {
            b = handle((CommandResponse) o);
        }
        if (!b) return false;
        postHandle(o);
        return true;
    }

    default boolean preHandle(BaseRpc o) {
        return true;
    }
    default void postHandle(BaseRpc o) {}
    boolean handle(Command message);
    boolean handle(CommandResponse message);
}
