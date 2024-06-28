package org.example;

public interface NetworkTopology {
    void setupTopology(int numberOfNodes);
    void sendMessage(int fromNodeId, int toNodeId, Message message);
    void receiveMessage(int nodeId, Message message);
}
