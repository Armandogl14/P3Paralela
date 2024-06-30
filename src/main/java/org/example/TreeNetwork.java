package org.example;

import java.util.ArrayList;
import java.util.List;

public class TreeNetwork implements NetworkTopology{
    private List<List<Integer>> adjList;
    private int numberOfNodes;

    public TreeNetwork() {
        this.adjList = new ArrayList<>();
    }

    @Override
    public void setupTopology(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
        this.adjList = new ArrayList<>(numberOfNodes);

        for (int i = 0; i < numberOfNodes; i++) {
            adjList.add(new ArrayList<>());
        }

        // En una red de árbol, cada nodo está conectado a un nodo padre y a cero o más nodos hijos.
        // Aquí, simplemente conectamos cada nodo con su nodo padre (el nodo con índice i está conectado con el nodo con índice i/2).
        for (int i = 1; i < numberOfNodes; i++) {
            int parentNodeId = (i - 1) / 2; // Cambiado a (i - 1) / 2 para obtener correctamente el padre
            adjList.get(parentNodeId).add(i);
            adjList.get(i).add(parentNodeId);
        }
        System.out.println("Configurando topología de red en árbol con " + numberOfNodes + " nodos.");
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
        for (int i : adjList.get(fromNodeId)) {
            sendMessage(fromNodeId, i, message);
        }
    }
}
