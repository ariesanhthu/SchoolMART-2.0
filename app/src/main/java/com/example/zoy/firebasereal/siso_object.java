package com.example.zoy.firebasereal;

public class siso_object {
    private String name;
    private String id;
    private Boolean check = new Boolean(false);

    public siso_object(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public siso_object() {
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
