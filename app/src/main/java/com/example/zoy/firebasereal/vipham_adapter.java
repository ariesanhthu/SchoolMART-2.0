package com.example.zoy.firebasereal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class vipham_adapter extends BaseAdapter {
    private vipham_object vipham;
    private Context context;

    public vipham_adapter(vipham_object vipham, Context context) {
        this.vipham = vipham;
        this.context = context;
    }

    @Override
    public int getCount() {
        return vipham.getLoaivipham().size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.custom_vipham, viewGroup, false);
        TextView loaivipham = (TextView) view.findViewById(R.id.custom_loaivipham);
        TextView diemtru = (TextView) view.findViewById(R.id.custom_diemtru);
        TextView tongdiemtru = (TextView) view.findViewById(R.id.custom_tongdiemtru);
        TextView name = (TextView) view.findViewById(R.id.custom_name);

        loaivipham.setText(vipham.getLoaivipham().get(i));
        tongdiemtru.setText("Tổng điểm trừ: "+vipham.getDiemtru().get(i).toString());
        String text = "Học sinh vi phạm:"+'\n';
        for (String s : vipham.getName().get(i))
        {
            text = text +" + "+s+'\n';
        }
        name.setText(text);
        return view;
    }
}
