package com.example.zoy.firebasereal;

public class User {
    private String email;
    private String password;
    private String name;
    private String lop;
    private String sdt;
    private String mon;
    private String chucvu;

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

    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop;
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

    public String getChucvu() {
        return chucvu;
    }

    public void setChucvu(String chucvu) {
        this.chucvu = chucvu;
    }

    public User(String email, String password, String name, String lop) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.lop = lop;
    }

    public User(String email, String name, String sdt) {
        this.email = email;
        this.name = name;
        this.sdt = sdt;
    }

    public User(String email, String password, String name, String lop, String sdt) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.lop = lop;
        this.sdt = sdt;
    }


    public  User(){

    }

}
