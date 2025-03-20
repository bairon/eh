package com.eldritch.model;

public class ChatMessage {
    private String nickname; // Sender's nickname
    private String text;     // Message text

    // Default constructor (required for JSON deserialization)
    public ChatMessage() {}

    // Parameterized constructor
    public ChatMessage(String nickname, String text) {
        this.nickname = nickname;
        this.text = text;
    }

    // Getters and setters
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "nickname='" + nickname + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}