package org.example;

public class Main {
//    public static void main(String[] args) {
//        // Prueba Bus Network
//        NetworkManager busManager = new NetworkManager(new BusNetwork(5));
//        busManager.setupNetwork(5);
//
//        Message busMessage = new Message("(BusNetwork): Hola nodo 2", 1, 2);
//        busManager.sendMessage(1, 2, busMessage);
//
//        // Prueba Star Network
//        NetworkManager starManager = new NetworkManager(new StarNetwork(5));
//        starManager.setupNetwork(5);
//
//        Message starMessage = new Message("(StarNetwork): Hola, nodo 4 del central", 0, 4);
//        starManager.sendMessage(0, 4, starMessage);
//
//        // Prueba Ring Network
//        NetworkManager ringManager = new NetworkManager(new RingNetwork(5));
//        ringManager.setupNetwork(5);
//
//        Message ringMessage = new Message("(RingNetwork): Hola, nodo 1 desde nodo 0!", 0, 1);
//        ringManager.sendMessage(0, 1, ringMessage);
//
//        // Prueba Mesh Network
//        NetworkManager meshManager = new NetworkManager(new MeshNetwork(5));
//        meshManager.setupNetwork(5);
//
//        Message meshMessage = new Message("(MeshNetwork): Hola a todos los nodos desde nodo 0!", 0, -1);
//        meshManager.broadcastMessage(0, meshMessage);
//        //prueba Hypercube Network
//        NetworkManager hypercubeManager = new NetworkManager(new HypercubeNetwork(3));
//        hypercubeManager.setupNetwork(8);
//
//        Message hypercubeMessage = new Message("(HypercubeNetwork): Hola a todos los nodos desde nodo 0!", 0, -1);
//        hypercubeManager.broadcastMessage(0, hypercubeMessage);
//        return;
//
//        //prueba Tree Network
//        NetworkManager treeManager = new NetworkManager(new TreeNetwork(5));
//        treeManager.setupNetwork(5);
//
//        Message treeMessage = new Message("(TreeNetwork): Hola a todos los nodos desde nodo 0!", 0, -1);
//        treeManager.broadcastMessage(0, treeMessage);
//    }
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
        //prueba Hypercube Network
        NetworkManager hypercubeManager = new NetworkManager(new HypercubeNetwork(3));
        hypercubeManager.setupNetwork(8);

        Message hypercubeMessage = new Message("(HypercubeNetwork): Hola a todos los nodos desde nodo 0!", 0, -1);
        hypercubeManager.broadcastMessage(0, hypercubeMessage);

        //prueba Tree Network
        NetworkManager treeManager;
        treeManager = new NetworkManager(new TreeNetwork());
        treeManager.setupNetwork(5);

        Message treeMessage = new Message("(TreeNetwork): Hola a todos los nodos desde nodo 0!", 0, -1);
        treeManager.broadcastMessage(0, treeMessage);

        //prueba FullyConnected Network
        NetworkManager fullyConnectedManager = new NetworkManager(new FullyConnectedNetwork());
        fullyConnectedManager.setupNetwork(5);

        Message fullyConnectedMessage = new Message("(FullyConnectedNetwork): Hola a todos los nodos desde nodo 0!", 0, -1);
        fullyConnectedManager.broadcastMessage(0, fullyConnectedMessage);

        //prueba Switched Network
        NetworkManager switchedManager = new NetworkManager(new SwitchedNetwork());
        switchedManager.setupNetwork(5);

        Message switchedMessage = new Message("(SwitchedNetwork): Hola a todos los nodos desde nodo 0!", 0, -1);
        switchedManager.broadcastMessage(0, switchedMessage);
    }
}