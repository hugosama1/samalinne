package com.hugosama.samalinne.data.entities;

import com.hugosama.samalinne.data.SamalinneContract;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by hugo on 9/6/16.
 */
@Entity( nameInDb = "messages")
public class Message {
    @Id
    @Property(nameInDb = "_id")
    private long _id;
    @Property(nameInDb = "message")
    private String message;
    @Property(nameInDb = "date")
    private long date;

    @Generated(hash = 1243947406)
    public Message(long _id, String message, long date) {
        this._id = _id;
        this.message = message;
        this.date = date;
    }

    @Generated(hash = 637306882)
    public Message() {
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Message{" +
                "_id=" + _id +
                ", message='" + message + '\'' +
                ", date=" + date +
                '}';
    }
}
