package org.example;

import java.util.ArrayList;
import java.util.List;

public class FullyConnectedNetwork implements NetworkTopology {
    private List<List<Integer>> adjList;
    private int numberOfNodes;

    public FullyConnectedNetwork() {
        this.adjList = new ArrayList<>();
    }

    @Override
    public void setupTopology(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
        this.adjList = new ArrayList<>(numberOfNodes);

        for (int i = 0; i < numberOfNodes; i++) {
            adjList.add(new ArrayList<>());
        }

        // En una red completamente conectada, cada nodo está conectado a todos los demás nodos.
        for (int i = 0; i < numberOfNodes; i++) {
            for (int j = 0; j < numberOfNodes; j++) {
                if (i != j) {
                    adjList.get(i).add(j);
                }
            }
        }
        System.out.println("Configurando topología de red completamente conectada con " + numberOfNodes + " nodos.");
    }

    @Override
    public void sendMessage(int fromNodeId, int toNodeId, Message message) {
        if (adjList.get(fromNodeId).contains(toNodeId)) {
            System.out.println("Enviando mensaje desde nodo " + fromNodeId + " a nodo " + toNodeId + ": " + message.getContent());
            receiveMessage(toNodeId, message);
        } else {
            System.out.println("Los nodos no están directamente conectados. No se puede enviar el mensaje.");
        }
    }

    @Override
    public void receiveMessage(int nodeId, Message message) {
        System.out.println("Recibiendo mensaje en nodo " + nodeId + ": " + message.getContent());
    }

    @Override
    public void broadcastMessage(int fromNodeId, Message message) {
        for (int i = 0; i < numberOfNodes; i++) {
            if (i != fromNodeId) {
                sendMessage(fromNodeId, i, message);
            }
        }
    }
}
