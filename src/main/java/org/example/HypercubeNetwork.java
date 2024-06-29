package org.example;

public class HypercubeNetwork implements NetworkTopology {
    private int n;
    private int[][] adjMatrix;
    // Constructor
    public HypercubeNetwork(int n) {
        this.n = n;
        this.adjMatrix = new int[(int) Math.pow(2, n)][(int) Math.pow(2, n)];
        for (int i = 0; i < (int) Math.pow(2, n); i++) {
            for (int j = 0; j < (int) Math.pow(2, n); j++) {
                adjMatrix[i][j] = 0;
            }
        }
        for (int i = 0; i < (int) Math.pow(2, n); i++) {
            for (int j = 0; j < n; j++) {
                int neighbour = i ^ (1 << j);
                adjMatrix[i][neighbour] = 1;
            }
        }
    }

    // Métodos
    @Override
    public void setupTopology(int numberOfNodes) {
        this.n = (int) (Math.log(numberOfNodes) / Math.log(2));
        this.adjMatrix = new int[numberOfNodes][numberOfNodes];
        for (int i = 0; i < numberOfNodes; i++) {
            for (int j = 0; j < numberOfNodes; j++) {
                adjMatrix[i][j] = 0;
            }
        }
        for (int i = 0; i < numberOfNodes; i++) {
            for (int j = 0; j < n; j++) {
                int neighbour = i ^ (1 << j);
                adjMatrix[i][neighbour] = 1;
            }
        }
    }

    @Override
    public void sendMessage(int fromNodeId, int toNodeId, Message message) {
        if (adjMatrix[fromNodeId][toNodeId] == 1) {
            System.out.println("Message sent from node " + fromNodeId + " to node " + toNodeId);
            receiveMessage(toNodeId, message);
        } else {
            System.out.println("Nodes are not directly connected. Message cannot be sent.");
        }
    }

    @Override
    public void receiveMessage(int nodeId, Message message) {
        System.out.println("Node " + nodeId + " received message: " + message.getContent());
    }

    @Override
    public void broadcastMessage(int fromNodeId, Message message) {
        for (int i = 0; i < adjMatrix[fromNodeId].length; i++) {
            if (adjMatrix[fromNodeId][i] == 1) {
                sendMessage(fromNodeId, i, message);
            }
        }
    }
}