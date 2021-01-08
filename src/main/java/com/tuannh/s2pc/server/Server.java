package com.tuannh.s2pc.server;

import com.tuannh.s2pc.api.ClientInterface;
import com.tuannh.s2pc.dto.rpc.Command;
import com.tuannh.s2pc.dto.rpc.CommandResponse;
import com.tuannh.s2pc.dto.rpc.RpcVisitor;
import com.tuannh.s2pc.server.role.Primary;
import com.tuannh.s2pc.server.role.Replica;
import com.tuannh.s2pc.server.role.ServerRole;
import com.tuannh.s2pc.server.role.ServerRoleType;
import com.tuannh.s2pc.utility.network.Network;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class Server implements RpcVisitor, ClientInterface {
    //
    @Getter private final String id;
    @Getter private final Network<String> network;
    @Getter private final int quorumSize;
    @Getter private final List<String> quorum;
    @Getter private final List<String> neighbors;
    @Getter private final int minimalVoteCount;
    //
    private final ServerRole role;
    @Getter
    private final ServerData data;
    //
    private final Thread msgPollThread;

    public Server(ServerRoleType role, String id, List<String> quorum, Network<String> network) {
        this.id = id;
        // quorum
        this.quorum = quorum;
        quorumSize = quorum.size();
        minimalVoteCount = quorumSize - 1;
        neighbors = new ArrayList<>();
        for (String s : quorum) {
            if (StringUtils.equals(s, id)) continue;
            neighbors.add(s);
        }
        // network
        this.network = network;
        // data
        this.data = new ServerData();
        // role
        switch (role) {
            case PRIMARY:
                this.role = new Primary(this, data);
                break;
            case REPLICA:
                this.role = new Replica(this, data);
                break;
            default:
                throw new IllegalStateException("server role undefined");
        }
        // msg polling thread
        msgPollThread = new Thread(new MsgPollWorker(this));
        msgPollThread.setName("message-polling-thread-" + id);
        msgPollThread.start();
    }

    @Override
    public void sendCommand(String cmd) {
        role.sendCommand(cmd);
    }

    @Override
    public boolean handle(Command message) {
        return role.handle(message);
    }

    @Override
    public boolean handle(CommandResponse message) {
        return role.handle(message);
    }
}
