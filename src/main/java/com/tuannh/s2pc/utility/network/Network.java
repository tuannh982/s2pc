package com.tuannh.s2pc.utility.network;

import java.util.concurrent.TimeUnit;

public interface Network<I> {
    void register(I id);
    void deregister(I id);
    <T> void broadcast(I from, T message);
    <T> void sendMsg(I from, I to, T message);
    <T> T pollMsg(I id, long timeout, TimeUnit unit) throws InterruptedException;
    void jamConnection(JammingType jammingType, double value, I from, I to);
}
