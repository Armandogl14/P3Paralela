package org.example;

public class Main {
    public static void main(String[] args) {
        // Prueba Bus Network
        NetworkManager busManager = new NetworkManager(new BusNetwork(5));
        busManager.setupNetwork(5);

        Message busMessage = new Message("Hello, Node 2!", 1, 2);
        busManager.sendMessage(1, 2, busMessage);

        // Prueba Star Network
        NetworkManager starManager = new NetworkManager(new StarNetwork(5));
        starManager.setupNetwork(5);

        Message starMessage = new Message("Hello, Node 4 from central!", 0, 4);
        starManager.sendMessage(0, 4, starMessage);

        // Prueba Ring Network
        NetworkManager ringManager = new NetworkManager(new RingNetwork(5));
        ringManager.setupNetwork(5);

        Message ringMessage = new Message("Hello, Node 1 from Node 0!", 0, 1);
        ringManager.sendMessage(0, 1, ringMessage);

        return;
    }
}