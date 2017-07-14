package com.seatgeek.placesautocompletedemo.model;

/**
 * Created by Ravi Tamada on 21/02/17.
 * www.androidhive.info
 */

public class Message {
    private int id;
    private String idUser;
    private String idLocation;
    private String txtTitle;
    private String txtCmt;
    private String datatime;
    private String rate;
    private int color = -1;

    public Message(int id, String idUser, String idLocation, String txtTitle, String txtCmt, String datatime, String rate, int color) {
        this.id = id;
        this.idUser = idUser;
        this.idLocation = idLocation;
        this.txtTitle = txtTitle;
        this.txtCmt = txtCmt;
        this.datatime = datatime;
        this.rate = rate;
        this.color = color;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdLocation() {
        return idLocation;
    }

    public void setIdLocation(String idLocation) {
        this.idLocation = idLocation;
    }

    public String getTxtTitle() {
        return txtTitle;
    }

    public void setTxtTitle(String txtTitle) {
        this.txtTitle = txtTitle;
    }

    public String getTxtCmt() {
        return txtCmt;
    }

    public void setTxtCmt(String txtCmt) {
        this.txtCmt = txtCmt;
    }

    public String getDatatime() {
        return datatime;
    }

    public void setDatatime(String datatime) {
        this.datatime = datatime;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }


    public Message() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public String getFrom() {
//        return from;
//    }
//
//    public void setFrom(String from) {
//        this.from = from;
//    }
//
//    public String getSubject() {
//        return subject;
//    }
//
//    public void setSubject(String subject) {
//        this.subject = subject;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public String getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(String timestamp) {
//        this.timestamp = timestamp;
//    }
//
//    public boolean isImportant() {
//        return isImportant;
//    }
//
//    public void setImportant(boolean important) {
//        isImportant = important;
//    }
//
//    public String getPicture() {
//        return picture;
//    }
//
//    public void setPicture(String picture) {
//        this.picture = picture;
//    }
//
//    public boolean isRead() {
//        return isRead;
//    }
//
//    public void setRead(boolean read) {
//        isRead = read;
//    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
