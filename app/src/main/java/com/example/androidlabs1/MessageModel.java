
package com.example.androidlabs1;
public class MessageModel {
    public String message;
    public boolean isSend;
    protected long id;

    public MessageModel(String message, boolean isSend) {
        this ( message, isSend, 0);
    }

    public MessageModel(String message, boolean isSend, long id) {
        this.message = message;
        this.isSend = isSend;
        this.id = id;
    }

    public MessageModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }
}
