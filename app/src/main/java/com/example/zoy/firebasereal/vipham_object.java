package com.example.zoy.firebasereal;

import java.io.Serializable;
import java.util.ArrayList;

public class vipham_object implements Serializable {
    private ArrayList<String> loaivipham ;
    private ArrayList<Integer> diemtru ;
    private ArrayList<ArrayList<String>> name ;

    public vipham_object(ArrayList<String> loaivipham, ArrayList<Integer> diemtru, ArrayList<ArrayList<String>> name) {
        this.loaivipham = loaivipham;
        this.diemtru = diemtru;
        this.name = name;
    }

    public ArrayList<String> getLoaivipham() {
        return loaivipham;
    }

    public void setLoaivipham(ArrayList<String> loaivipham) {
        this.loaivipham = loaivipham;
    }

    public ArrayList<Integer> getDiemtru() {
        return diemtru;
    }

    public void setDiemtru(ArrayList<Integer> diemtru) {
        this.diemtru = diemtru;
    }

    public ArrayList<ArrayList<String>> getName() {
        return name;
    }

    public void setName(ArrayList<ArrayList<String>> name) {
        this.name = name;
    }

    public vipham_object() {
    }
}
