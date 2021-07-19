package com.github.tuannh982.s2pc.dto.rpc;

import com.github.tuannh982.s2pc.dto.BaseMessage;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public abstract class BaseRpc extends BaseMessage {
    private final String from;

    protected BaseRpc(String from) {
        this.from = from;
    }
}
