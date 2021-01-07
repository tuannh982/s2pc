package com.tuannh.s2pc.server;

import com.tuannh.s2pc.api.ClientInterface;
import com.tuannh.s2pc.dto.rpc.Command;
import com.tuannh.s2pc.dto.rpc.CommandResponse;
import com.tuannh.s2pc.dto.rpc.RpcVisitor;
import com.tuannh.s2pc.utility.network.Network;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class Server implements RpcVisitor, ClientInterface {
    private final List<String> logs = new ArrayList<>();
    private int commitIndex = -1;

    private final String id;
    private final Network<String> network;
    private final int quorumSize;
    private final List<String> quorum;
    private final List<String> neighbors;
    private final int minimalVoteCount;

    private final ServerRole role;
    private TransactionState state;

    public Server(ServerRole role, String id, List<String> quorum, Network<String> network) {
        this.role = role;
        this.id = id;
        // quorum
        this.quorum = quorum;
        quorumSize = quorum.size();
        minimalVoteCount = (quorumSize + 1) / 2;
        neighbors = new ArrayList<>();
        for (String s : quorum) {
            if (StringUtils.equals(s, id)) continue;
            neighbors.add(s);
        }
        // network
        this.network = network;
        state = TransactionState.NONE;
    }

    @Override
    public void sendCommand(String cmd) {
        if (role == ServerRole.PRIMARY) {
            if (state != TransactionState.NONE) {
                log.error("{} | currently in transaction, could not do anything", id);
            } else {

            }
        }
        log.error("{} | server must be primary to receive client command", id);
    }

    @Override
    public boolean handle(Command message) {
        switch (role) {
            case PRIMARY: {
                break;
            }
            case REPLICA: {
                break;
            }
        }
        return true;
    }

    @Override
    public boolean handle(CommandResponse message) {
        switch (role) {
            case PRIMARY: {
                break;
            }
            case REPLICA: {
                break;
            }
        }
        return true;
    }
}
