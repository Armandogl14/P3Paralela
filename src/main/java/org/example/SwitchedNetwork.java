package org.example;

import java.util.ArrayList;
import java.util.List;

public class SwitchedNetwork implements NetworkTopology {
    private List<List<Integer>> adjList;
    private int numberOfNodes;

    public SwitchedNetwork() {
        this.adjList = new ArrayList<>();
    }

    @Override
    public void setupTopology(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
        this.adjList = new ArrayList<>(numberOfNodes);

        for (int i = 0; i < numberOfNodes; i++) {
            adjList.add(new ArrayList<>());
        }

        // En una red conmutada, cada nodo está conectado a un conmutador.
        // Vamos a suponer que tenemos un conmutador central al cual todos los nodos se conectan.
        // Representamos el conmutador central con el nodo 0.
        for (int i = 1; i < numberOfNodes; i++) {
            adjList.get(0).add(i);  // Conectar el nodo 0 (conmutador) con cada nodo
            adjList.get(i).add(0);  // Conectar cada nodo con el nodo 0 (conmutador)
        }
        System.out.println("Configurando topología de red conmutada con " + numberOfNodes + " nodos.");

        // Imprimir la lista de adyacencia para depuración
        printAdjList();
    }

    @Override
    public void sendMessage(int fromNodeId, int toNodeId, Message message) {
        // Verificar si los nodos están correctamente conectados al conmutador central (nodo 0)
        if ((fromNodeId == 0 || adjList.get(fromNodeId).contains(0)) &&
                (toNodeId == 0 || adjList.get(toNodeId).contains(0))) {
            System.out.println("Enviando mensaje desde nodo " + fromNodeId + " a nodo " + toNodeId + " a través del conmutador central: " + message.getContent());
            receiveMessage(toNodeId, message);
        } else {
            System.out.println("Los nodos no están conectados al conmutador central. No se puede enviar el mensaje.");
        }
    }

    @Override
    public void receiveMessage(int nodeId, Message message) {
        System.out.println("Recibiendo mensaje en nodo " + nodeId + ": " + message.getContent());
    }

    @Override
    public void broadcastMessage(int fromNodeId, Message message) {
        for (int i = 1; i < numberOfNodes; i++) {
            if (i != fromNodeId) {
                sendMessage(fromNodeId, i, message);
            }
        }
    }

    // Método para imprimir la lista de adyacencia (para depuración)
    private void printAdjList() {
        for (int i = 0; i < adjList.size(); i++) {
            System.out.println("Nodo " + i + " está conectado a: " + adjList.get(i));
        }
    }
}
