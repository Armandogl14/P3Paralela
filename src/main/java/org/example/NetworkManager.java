package org.example;

public class NetworkManager {
    NetworkTopology topology;

    public NetworkManager(NetworkTopology topology) {
        this.topology = topology;
    }

    public void setupNetwork(int numberOfNodes) {
        topology.setupTopology(numberOfNodes);
    }

    public void sendMessage(int fromNodeId, int toNodeId, Message message) {
        topology.sendMessage(fromNodeId, toNodeId, message);
    }

    public void receiveMessage(int nodeId, Message message) {
        topology.receiveMessage(nodeId, message);
    }
}

