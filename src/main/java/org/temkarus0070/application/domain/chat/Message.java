package org.temkarus0070.application.domain.chat;

import org.temkarus0070.application.domain.User;


public class Message {
    private String content;
    private User sender;

    public Message(String content, User sender) {
        this.content = content;
        this.sender = sender;
    }

    public Message() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
}
