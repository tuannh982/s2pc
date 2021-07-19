package com.github.tuannh982.s2pc.utility.timer;

public interface Timer {
    long getTimeout();
    void reset(long timeout);
    void reset();
    void stop();
}
