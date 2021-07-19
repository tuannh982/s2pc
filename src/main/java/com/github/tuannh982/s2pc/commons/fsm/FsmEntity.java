package com.github.tuannh982.s2pc.commons.fsm;

public interface FsmEntity {
    State state();
    void changeState(State newState);
}
