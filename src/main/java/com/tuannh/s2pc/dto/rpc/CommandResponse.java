package com.tuannh.s2pc.dto.rpc;

import lombok.Getter;
import lombok.ToString;

@ToString(callSuper = true)
@Getter
public class CommandResponse extends BaseRpc {
    private final String transactionId;
    private final String cmd;
    private final boolean status;

    public CommandResponse(String from, String transactionId, String cmd, boolean status) {
        super(from);
        this.transactionId = transactionId;
        this.cmd = cmd;
        this.status = status;
    }
}
