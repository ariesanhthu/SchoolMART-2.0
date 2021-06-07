package com.example.zoy.firebasereal;

public class TKB {
    private String mon;
    private String gv;
    private String tg;
    private String id;
    private String buoi;
    private String tiet;

    public TKB(String mon, String gv, String tg, String id) {
        this.mon = mon;
        this.gv = gv;
        this.tg = tg;
        this.id = id;
    }

    public TKB(String mon, String gv, String tg) {
        this.mon = mon;
        this.gv = gv;
        this.tg = tg;
    }



    public TKB(String buoi) {
        this.buoi = buoi;
    }

    public TKB() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTiet() {
        return tiet;
    }

    public void setTiet(String tiet) {
        this.tiet = tiet;
    }

    public String getMon() {
        return mon;
    }

    public void setMon(String mon) {
        this.mon = mon;
    }

    public String getGv() {
        return gv;
    }

    public void setGv(String gv) {
        this.gv = gv;
    }

    public String getTg() {
        return tg;
    }

    public void setTg(String tg) {
        this.tg = tg;
    }

    public String getBuoi() {
        return buoi;
    }

    public void setBuoi(String buoi) {
        this.buoi = buoi;
    }
}
