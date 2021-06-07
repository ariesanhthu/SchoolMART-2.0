package com.example.zoy.firebasereal;

import android.os.Parcel;
import android.os.Parcelable;

public class Sodaubai implements Parcelable {
    private String thoigian;
    private String gv;
    private String mon;
    private String sisolop;
    private String diem;
    private String tiethoctot;
    private String id;
    private String baihoc;

    public Sodaubai() {
    }

    public Sodaubai(String thoigian, String gv, String mon, String sisolop, String diem, String tiethoctot, String id, String baihoc) {
        this.thoigian = thoigian;
        this.gv = gv;
        this.mon = mon;
        this.sisolop = sisolop;
        this.diem = diem;
        this.tiethoctot = tiethoctot;
        this.id = id;
        this.baihoc = baihoc;
    }

    protected Sodaubai(Parcel in) {
        thoigian = in.readString();
        gv = in.readString();
        mon = in.readString();
        sisolop = in.readString();
        diem = in.readString();
        tiethoctot = in.readString();
        id = in.readString();
        baihoc = in.readString();
    }

    public static final Creator<Sodaubai> CREATOR = new Creator<Sodaubai>() {
        @Override
        public Sodaubai createFromParcel(Parcel in) {
            return new Sodaubai(in);
        }

        @Override
        public Sodaubai[] newArray(int size) {
            return new Sodaubai[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThoigian() {
        return thoigian;
    }

    public void setThoigian(String thoigian) {
        this.thoigian = thoigian;
    }

    public String getGv() {
        return gv;
    }

    public void setGv(String gv) {
        this.gv = gv;
    }

    public String getMon() {
        return mon;
    }

    public void setMon(String mon) {
        this.mon = mon;
    }

    public String getSisolop() {
        return sisolop;
    }

    public void setSisolop(String sisolop) {
        this.sisolop = sisolop;
    }

    public String getDiem() {
        return diem;
    }

    public void setDiem(String diem) {
        this.diem = diem;
    }

    public String getTiethoctot() {
        return tiethoctot;
    }

    public void setTiethoctot(String tiethoctot) {
        this.tiethoctot = tiethoctot;
    }

    public String getBaihoc() {
        return baihoc;
    }

    public void setBaihoc(String baihoc) {
        this.baihoc = baihoc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(thoigian);
        parcel.writeString(gv);
        parcel.writeString(mon);
        parcel.writeString(sisolop);
        parcel.writeString(diem);
        parcel.writeString(tiethoctot);
        parcel.writeString(id);
        parcel.writeString(baihoc);
    }
}
