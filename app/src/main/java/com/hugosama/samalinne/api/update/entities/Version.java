package com.hugosama.samalinne.api.update.entities;

/**
 * Created by hugo on 8/23/16.
 */
public class Version {
    private int version;
    private String url;
    private long messagesVersion;

    public long getMessagesVersion() {
        return messagesVersion;
    }

    public void setMessagesVersion(long messagesVersion) {
        this.messagesVersion = messagesVersion;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
