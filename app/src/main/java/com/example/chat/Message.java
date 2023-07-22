package com.example.chat;

public class Message {
    private String content;
    private String senderName;

    public Message() {
        // Required empty public constructor for Firebase
    }

    public Message(String content, String senderName) {
        this.content = content;
        this.senderName = senderName;
    }

    public String getContent() {
        return content;
    }

    public String getSenderName() {
        return senderName;
    }
}
