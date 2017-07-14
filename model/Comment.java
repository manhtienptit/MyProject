package com.seatgeek.placesautocompletedemo.model;

import java.util.Date;

/**
 * Created by tranminhtue on 10/05/2017.
 */

public class Comment {
    private int id;
    private User user_cmt;
    private String title;
    private String message;
    private int rate;
    private Date timestamp;
    private ParkingCar picture;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser_cmt() {
        return user_cmt;
    }

    public void setUser_cmt(User user_cmt) {
        this.user_cmt = user_cmt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public ParkingCar getPicture() {
        return picture;
    }

    public void setPicture(ParkingCar picture) {
        this.picture = picture;
    }

}
