package org.example;

public class Main {
    public static void main(String[] args) {
        // Prueba Bus Network
        NetworkManager busManager = new NetworkManager(new BusNetwork(5));
        busManager.setupNetwork(5);

        Message busMessage = new Message("(BusNetwork): Hola nodo 2", 1, 2);
        busManager.sendMessage(1, 2, busMessage);

        // Prueba Star Network
        NetworkManager starManager = new NetworkManager(new StarNetwork(5));
        starManager.setupNetwork(5);

        Message starMessage = new Message("(StarNetwork): Hola, nodo 4 del central", 0, 4);
        starManager.sendMessage(0, 4, starMessage);

        // Prueba Ring Network
        NetworkManager ringManager = new NetworkManager(new RingNetwork(5));
        ringManager.setupNetwork(5);

        Message ringMessage = new Message("(RingNetwork): Hola, nodo 1 desde nodo 0!", 0, 1);
        ringManager.sendMessage(0, 1, ringMessage);

        // Prueba Mesh Network
        NetworkManager meshManager = new NetworkManager(new MeshNetwork(5));
        meshManager.setupNetwork(5);

        Message meshMessage = new Message("(MeshNetwork): Hola a todos los nodos desde nodo 0!", 0, -1);
        meshManager.broadcastMessage(0, meshMessage);

        return;
    }
}