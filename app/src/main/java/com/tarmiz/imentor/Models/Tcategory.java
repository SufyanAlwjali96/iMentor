package com.tarmiz.imentor.Models;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;

public class Tcategory extends RealmObject {

    public Integer id;
    public String cat;

    @LinkingObjects("category") // <-- !
    private final RealmResults<Teacher> schoolRealmResults = null; // <-- !

    public Tcategory() {
    }

    public Tcategory(Integer id, String cat) {
        this.id = id;
        this.cat = cat;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTcategory() {
        return cat;
    }

    public void setTcategory(String cat) {
        this.cat = cat;
    }
}
