package com.tuannh.s2pc.server;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum TransactionState {
    NONE("none"),
    PREPARE("prepare"),
    COMMIT("commit"),
    ROLLBACK("rollback");

    private final String value;

    private static final Map<String, TransactionState> mx;
    static {
        Map<String, TransactionState> mxx = new HashMap<>();
        for (TransactionState s : TransactionState.values()) {
            mxx.put(s.getValue(), s);
        }
        mx = Collections.unmodifiableMap(mxx);
    }

    public static TransactionState from(String s) {
        TransactionState ret = mx.get(s);
        if (ret == null) throw new IllegalStateException("state not defined");
        return ret;
    }
}
