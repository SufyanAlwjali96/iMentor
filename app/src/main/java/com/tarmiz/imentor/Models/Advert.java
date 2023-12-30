package com.tarmiz.imentor.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Advert extends RealmObject {
    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("title")
    @Expose

    private String title;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("details")
    @Expose
    private String details;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("phone2")
    @Expose
    private String phone2;

    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("facebook")
    @Expose
    private String facebook;


    @SerializedName("faceUrl")
    @Expose
    private String faceUrl;

    @SerializedName("reglink")
    @Expose
    private String reglink;
    @SerializedName("created_at")
    @Expose
    private String  createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public String getReglink() {
        return reglink;
    }

    public void setReglink(String reglink) {
        this.reglink = reglink;
    }

    public String  getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String  createdAt) {
        this.createdAt = createdAt;
    }
}