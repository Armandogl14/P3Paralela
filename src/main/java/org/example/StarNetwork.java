package org.example;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class StarNetwork implements NetworkTopology {
    private ConcurrentHashMap<Integer, Node> nodes;
    private ExecutorService executor;
    private ReentrantLock lock;
    private Node center;

    public StarNetwork(int numberOfNodes){
        nodes = new ConcurrentHashMap<>();
        lock = new ReentrantLock();
        executor = Executors.newFixedThreadPool(numberOfNodes);
    }

    @Override
    public void setupTopology(int numberOfNodes) {
        lock.lock();
        try {
            center = new Node(0);
            for (int i = 1; i < numberOfNodes; i++) {
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
                if (fromNodeId == 0) {
                    nodes.get(toNodeId).recieveMessage(message);
                } else {
                    center.recieveMessage(message);
                }
            } finally {
                lock.unlock();
            }
        });
    }

    @Override
    public void receiveMessage(int nodeId, Message message) {
        lock.lock();
        try {
            if (nodeId == 0) {
                center.recieveMessage(message);
            } else {
                nodes.get(nodeId).recieveMessage(message);
            }
        } finally {
            lock.unlock();
        }
    }
}
