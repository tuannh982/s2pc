package com.github.tuannh982.s2pc.server;

import com.github.tuannh982.s2pc.dto.rpc.BaseRpc;
import lombok.extern.log4j.Log4j2;


@Log4j2
public class MsgPollWorker implements Runnable {
    private static final long IO_LOOP_WAIT = 20;
    private final Object ioLock = new Object[0];
    private volatile boolean stop = false;

    private final Server server;

    public MsgPollWorker(Server server) {
        this.server = server;
    }

    public void stop() {
        synchronized (ioLock) {
            stop = true;
            ioLock.notifyAll();
        }
    }

    @Override
    public void run() {
        try {
            while (!stop) {
                synchronized (ioLock) {
                    ioLock.wait(IO_LOOP_WAIT);
                }
                BaseRpc message = server.getNetwork().pollMsg(server.getId());
                if (message != null) {
                    server.visit(message);
                }
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
    }
}