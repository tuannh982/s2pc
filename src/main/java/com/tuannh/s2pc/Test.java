package com.tuannh.s2pc;

import com.tuannh.s2pc.server.Server;
import com.tuannh.s2pc.server.role.ServerRoleType;
import com.tuannh.s2pc.utility.network.Network;
import com.tuannh.s2pc.utility.network.SimpleQueueBasedNetwork;

import java.util.*;

public class Test {
    private static void summary(List<Server> rs) {
        System.out.println("-SERVER-SUMMARY--------------------------------------------------------------------");
        System.out.println("SIZE = " + rs.size());
        for (Server s : rs) {
            System.out.println("server " + s.getId() + "|" + s.getData());
        }
        System.out.println("-----------------------------------------------------------------------------------");
    }

    public static void main(String[] args) throws InterruptedException {
        Network<String> internet = new SimpleQueueBasedNetwork<>();
        List<String> quorum = Arrays.asList("1", "2", "3");
        List<Server> rs = Collections.synchronizedList(new ArrayList<>());
        internet.register("1");
        internet.register("2");
        internet.register("3");
        rs.add(new Server(ServerRoleType.PRIMARY, "1", quorum, internet));
        rs.add(new Server(ServerRoleType.REPLICA, "2", quorum, internet));
        rs.add(new Server(ServerRoleType.REPLICA, "3", quorum, internet));
        for (int i = 0; i < 5; i++) {
            rs.get(0).sendCommand("TEST-" + i);
            Thread.sleep(500);
            summary(rs);
        }
        System.exit(0);
    }
}
