package com.tuannh.s2pc.server.role;

import com.tuannh.s2pc.api.ClientInterface;
import com.tuannh.s2pc.dto.rpc.RpcVisitor;
import com.tuannh.s2pc.server.Server;
import com.tuannh.s2pc.server.ServerData;
import com.tuannh.s2pc.server.transaction.Transaction;
import com.tuannh.s2pc.server.transaction.TransactionState;

public abstract class ServerRole implements RpcVisitor, ClientInterface {
    protected final Server server;
    protected final ServerData data;
    protected Transaction transaction;

    protected ServerRole(Server server, ServerData data) {
        this.server = server;
        this.data = data;
    }

    public abstract ServerRoleType role();

    protected void newTransaction(String id) {
        transaction = new Transaction(id);
    }

    protected boolean inTransaction() {
        return transaction != null && transaction.active();
    }

    protected boolean transactionDone(boolean success) {
        if (success) {
            return !transaction.active() && transaction.getState() == TransactionState.COMMIT;
        } else {
            return !transaction.active() && transaction.getState() == TransactionState.ROLLBACK;
        }
    }
}
