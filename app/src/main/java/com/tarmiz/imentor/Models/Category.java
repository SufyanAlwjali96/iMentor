package com.tarmiz.imentor.Models;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;

public class Category extends RealmObject {

    public Integer id;
    public String category;

    @LinkingObjects("category") // <-- !
    private final RealmResults<School> schoolRealmResults = null; // <-- !

    public Category() {
    }

    public Category(Integer id, String category) {
        this.id = id;
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
