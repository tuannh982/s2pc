package com.tuannh.s2pc.dto.rpc;

import lombok.Getter;
import lombok.ToString;

@ToString(callSuper = true)
@Getter
public class Command extends BaseRpc {
    private final String transactionId;
    private final String cmd;
    private final String data;

    public Command(String from, String transactionId, String cmd, String data) {
        super(from);
        this.transactionId = transactionId;
        this.cmd = cmd;
        this.data = data;
    }
}
