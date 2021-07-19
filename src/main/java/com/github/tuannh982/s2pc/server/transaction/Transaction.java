package com.github.tuannh982.s2pc.server.transaction;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class Transaction { // FSM-like class
    private final String transactionId;
    private TransactionState state = TransactionState.PREPARE;
    private volatile boolean active = true;

    public Transaction(String transactionId) {
        this.transactionId = transactionId;
    }

    public boolean active() {
        return active;
    }

    public TransactionState getNextState(boolean success) {
        if (success) {
            switch (state) {
                case PREPARE:
                    return TransactionState.COMMIT;
                case COMMIT:
                case ROLLBACK:
                    return state;
                default:
                    return state;
            }
        } else {
            switch (state) {
                case PREPARE:
                    return TransactionState.ROLLBACK;
                case COMMIT:
                case ROLLBACK:
                    return state;
                default:
                    return state;
            }
        }
    }

    public boolean getNextActive(boolean success) {
        switch (state) {
            case COMMIT:
            case ROLLBACK:
                return false;
            default:
        }
        return true;
    }

    public void nextState(boolean success) {
        TransactionState newState = getNextState(success);
        boolean newActive = getNextActive(success);
        state = newState;
        active = newActive;
    }
}
