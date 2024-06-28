package org.example;

public class Message {
    private String content;
    private int senderId;
    private int receiverId;

    public Message(String content, int senderId, int receiverId) {
        this.content = content;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }
}
