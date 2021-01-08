package com.tuannh.s2pc.server.role;

import com.tuannh.s2pc.dto.rpc.Command;
import com.tuannh.s2pc.dto.rpc.CommandResponse;
import com.tuannh.s2pc.server.Server;
import com.tuannh.s2pc.server.ServerData;
import com.tuannh.s2pc.server.transaction.TransactionState;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

@Log4j2
public class Replica extends ServerRole {

    public Replica(Server server, ServerData data) {
        super(server, data);
    }

    @Override
    public ServerRoleType role() {
        return ServerRoleType.REPLICA;
    }

    @Override
    public void sendCommand(String clientCmd) {
        log.error("{} | REPLICA node could not handle ClientCommand", server.getId());
    }

    @Override
    protected boolean inTransaction() {
        // replica dont need active variable
        return transaction != null && transaction.getState() != TransactionState.COMMIT && transaction.getState() != TransactionState.ROLLBACK;
    }

    @Override
    protected boolean transactionDone(boolean success) {
        // replica dont need active variable
        if (success) {
            return transaction.getState() == TransactionState.COMMIT;
        } else {
            return transaction.getState() == TransactionState.ROLLBACK;
        }
    }

    @Override
    public boolean handle(Command message) {
        if (!inTransaction()) {
            if (StringUtils.equals(message.getCmd(), TransactionState.PREPARE.getValue())) {
                final boolean next = true; // assume it always success
                if (next) {
                    newTransaction(message.getTransactionId());
                    data.getLogs().add(message.getData());
                    server.getNetwork().sendMsg(server.getId(), message.getFrom(), new CommandResponse(
                            server.getId(),
                            transaction.getTransactionId(),
                            TransactionState.PREPARE.getValue(),
                            true
                    ));
                } else {
                    server.getNetwork().sendMsg(server.getId(), message.getFrom(), new CommandResponse(
                            server.getId(),
                            transaction.getTransactionId(),
                            TransactionState.PREPARE.getValue(),
                            false
                    ));
                }
            }
        } else {
            if (StringUtils.equals(message.getTransactionId(), transaction.getTransactionId())) {
                final boolean next = true; // assume it always success
                TransactionState nextState = null;
                if (next) {
                    nextState = transaction.getNextState(true);
                } else {
                    nextState = transaction.getNextState(false);
                }
                TransactionState messageState = TransactionState.from(message.getCmd());
                if (nextState == messageState) {
                    transaction.nextState(next);
                    if (transactionDone(true)) {
                        data.getCommitIndex().incrementAndGet(); // commit
                    } else if (transactionDone(false)) {
                        data.getLogs().removeLast(); // rollback
                    }
                    server.getNetwork().sendMsg(server.getId(), message.getFrom(), new CommandResponse(
                            server.getId(),
                            transaction.getTransactionId(),
                            message.getCmd(),
                            true
                    ));
                    if (!inTransaction()) {
                        transaction = null; // clear transaction
                    }
                } else {
                    server.getNetwork().sendMsg(server.getId(), message.getFrom(), new CommandResponse(
                            server.getId(),
                            transaction.getTransactionId(),
                            message.getCmd(),
                            false
                    ));
                }
            } else {
                server.getNetwork().sendMsg(server.getId(), message.getFrom(), new CommandResponse(
                        server.getId(),
                        transaction.getTransactionId(),
                        message.getCmd(),
                        false
                ));
            }
        }
        return true;
    }

    @Override
    public boolean handle(CommandResponse message) {
        log.error("{} | REPLICA node could not handle CommandResponse", server.getId());
        return false;
    }
}
