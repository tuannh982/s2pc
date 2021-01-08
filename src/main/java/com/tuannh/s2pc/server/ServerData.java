package com.tuannh.s2pc.server;

import lombok.Getter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

@ToString
@Getter
public class ServerData {
    private final LinkedList<String> logs = new LinkedList<>();
    private final AtomicInteger commitIndex = new AtomicInteger(-1);
}
