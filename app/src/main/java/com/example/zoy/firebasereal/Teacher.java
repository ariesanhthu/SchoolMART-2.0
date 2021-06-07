package com.example.zoy.firebasereal;

public class Teacher {
    private String email;
    private String password;
    private String name;
    private String sdt;
    private String mon;

    public Teacher(String email, String password, String name, String sdt, String mon) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.sdt = sdt;
        this.mon = mon;
    }

    public Teacher() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getMon() {
        return mon;
    }

    public void setMon(String mon) {
        this.mon = mon;
    }
}
