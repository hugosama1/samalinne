package com.hugosama.samalinne.events;

/**
 * Created by hugo on 8/23/16.
 */
public class ErrorEvent {
    private String message;
    private String devMessage;

    public ErrorEvent() {

    }

    public ErrorEvent(String message) {
        this.message = message;
    }
    public ErrorEvent(String message,String devMessage) {
        this.message = message;
        this.devMessage = devMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDevMessage() {
        return devMessage;
    }

    public void setDevMessage(String devMessage) {
        this.devMessage = devMessage;
    }
}
