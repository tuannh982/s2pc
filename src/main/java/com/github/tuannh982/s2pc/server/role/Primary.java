package com.github.tuannh982.s2pc.server.role;

import com.github.tuannh982.s2pc.dto.rpc.BaseRpc;
import com.github.tuannh982.s2pc.dto.rpc.Command;
import com.github.tuannh982.s2pc.dto.rpc.CommandResponse;
import com.github.tuannh982.s2pc.server.Server;
import com.github.tuannh982.s2pc.server.ServerData;
import com.github.tuannh982.s2pc.server.transaction.TransactionState;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public class Primary extends ServerRole {
    private final AtomicInteger voteCount = new AtomicInteger(0);
    private final AtomicInteger xVoteCount = new AtomicInteger(0);

    public Primary(Server server, ServerData data) {
        super(server, data);
    }

    @Override
    public ServerRoleType role() {
        return ServerRoleType.PRIMARY;
    }

    private void sendMsgToOthers(BaseRpc rpc) {
        for (String neighbor : server.getNeighbors()) {
            server.getNetwork().sendMsg(server.getId(), neighbor, rpc);
        }
    }

    @Override
    public void sendCommand(String clientCmd) {
        if (inTransaction()) {
            log.error("{} | currently in transaction, ignore command", server.getId());
        } else {
            newTransaction(RandomStringUtils.random(15, true, true));
            data.getLogs().add(clientCmd);
            sendMsgToOthers(new Command(
                    server.getId(),
                    transaction.getTransactionId(),
                    TransactionState.PREPARE.getValue(),
                    clientCmd
            )); // PREPARE
        }
    }

    @Override
    public boolean handle(Command message) {
        log.error("{} | PRIMARY node could not handle Command", server.getId());
        return false;
    }

    @Override
    public boolean handle(CommandResponse message) {
        if (!inTransaction()) {
            return true;
        }
        if (message.isStatus()) {
            if (StringUtils.equals(message.getTransactionId(), transaction.getTransactionId())) {
                if (StringUtils.equals(message.getCmd(), transaction.getState().getValue())) {
                    voteCount.incrementAndGet();
                } else {
                    xVoteCount.incrementAndGet();
                }
            } else {
                xVoteCount.incrementAndGet();
            }
        } else {
            xVoteCount.incrementAndGet();
        }
        if (voteCount.get() >= server.getMinimalVoteCount()) {
            transaction.nextState(true);
            if (transactionDone(true)) {
                data.getCommitIndex().incrementAndGet(); // commit
            } else {
                sendMsgToOthers(new Command(
                        server.getId(),
                        transaction.getTransactionId(),
                        transaction.getState().getValue(),
                        null
                )); // PREPARE
            }
            voteCount.set(0);
            xVoteCount.set(0);
        } else if (xVoteCount.get() >= server.getMinimalVoteCount()) {
            transaction.nextState(false);
            if (transactionDone(false)) {
                data.getLogs().removeLast(); // rollback
            } else {
                sendMsgToOthers(new Command(
                        server.getId(),
                        transaction.getTransactionId(),
                        transaction.getState().getValue(),
                        null
                )); // PREPARE
            }
            voteCount.set(0);
            xVoteCount.set(0);
        }
        if (!inTransaction()) {
            transaction = null; // clear transaction
        }
        return true;
    }
}
