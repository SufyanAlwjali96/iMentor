package com.tarmiz.imentor.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Teacher extends RealmObject {


    @PrimaryKey
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("materials")
    @Expose
    private String materials;
    @SerializedName("address")
    @Expose
    private String address;

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
    @SerializedName("certified")
    @Expose
    private Integer certified;
    @SerializedName("created_at")
    @Expose
    private Integer createdAt;
    @SerializedName("category")
    @Expose
    private RealmList<Tcategory> category = new RealmList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }

    public String getMaterials() {
        return materials;
    }

    public void String(String materials) {
        this.materials = materials;
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

    public Integer getCertified() {
        return certified;
    }

    public void setCertified(Integer certified) {
        this.certified = certified;
    }

    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

    public List<Tcategory> getTcategory() {
        return category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public RealmList<Tcategory> getCategory() {
        return category;
    }


    public void setCategory(RealmList<Tcategory> category) {
        this.category = category;
    }


}

