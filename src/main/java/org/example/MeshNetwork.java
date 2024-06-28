package org.example;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class MeshNetwork implements NetworkTopology{
    private ConcurrentHashMap<Integer, Node> nodes;
    private ExecutorService executor;
    private ReentrantLock lock;

    public MeshNetwork(int numberOfNodes){
        nodes = new ConcurrentHashMap<>();
        lock = new ReentrantLock();
        executor = Executors.newFixedThreadPool(numberOfNodes);
    }
    @Override
    public void setupTopology(int numberOfNodes) {
        lock.lock();
        try {
            for (int i = 0; i < numberOfNodes; i++) {
                nodes.put(i, new Node(i));
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void sendMessage(int fromNodeId, int toNodeId, Message message) {
        executor.execute(() -> {
            lock.lock();
            try {
                nodes.get(toNodeId).recieveMessage(message);
            } finally {
                lock.unlock();
            }
        });
    }

    @Override
    public void receiveMessage(int nodeId, Message message) {
        lock.lock();
        try {
            nodes.get(nodeId).recieveMessage(message);
        } finally {
            lock.unlock();
        }
    }

    public void broadcastMessage(int fromNodeId, Message message) {
        for (int nodeId : nodes.keySet()) {
            if (nodeId != fromNodeId) {
                sendMessage(fromNodeId, nodeId, message);
            }
        }
    }
}
