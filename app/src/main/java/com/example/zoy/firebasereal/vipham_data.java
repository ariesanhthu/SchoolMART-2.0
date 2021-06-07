package com.example.zoy.firebasereal;

public class vipham_data {
    private String loaivipham;
    private Integer diemlop;
    private Integer diemcanhan;

    public vipham_data(String loaivipham, Integer diemlop, Integer diemcanhan) {
        this.loaivipham = loaivipham;
        this.diemlop = diemlop;
        this.diemcanhan = diemcanhan;
    }

    public vipham_data() {
    }

    public String getLoaivipham() {
        return loaivipham;
    }

    public void setLoaivipham(String loaivipham) {
        this.loaivipham = loaivipham;
    }

    public Integer getDiemlop() {
        return diemlop;
    }

    public void setDiemlop(Integer diemlop) {
        this.diemlop = diemlop;
    }

    public Integer getDiemcanhan() {
        return diemcanhan;
    }

    public void setDiemcanhan(Integer diemcanhan) {
        this.diemcanhan = diemcanhan;
    }
}
