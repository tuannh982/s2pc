package com.tuannh.s2pc.commons.fsm;

public interface FsmEntity {
    State state();
    void changeState(State newState);
}
