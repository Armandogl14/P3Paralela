package org.example;

public class Node {
    private int id;

    public Node(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void recieveMessage(Message message) {
        System.out.println("Node " + id + " received message: " + message.getContent());
    }
}
