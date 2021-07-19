# Simple Two Phase Commit implementation

# Introduction

Simple Two Phase Commit protocol implementation

# Quickstarts

run `main` in class `Test`

# TODOs

- [x] network emulation
    - [x] join/leave network
    - [x] transport (send, recv, broadcast)
    - [x] message encapsulation
    - [x] async IO (simple message queue)
    - [x] network jamming
    - [x] test
- [x] timer
    - [x] scheduled task
    - [x] listen/notify
    - [x] change timeout
    - [x] test
- [ ] 2PC
    - [ ] rpc (Command/CommandResponse)
        - [ ] command timeout
    - [x] client command (cmd)
    - [x] role
        - [x] Primary (coordinator)
        - [x] Replica (replica)
    - [x] test
    
