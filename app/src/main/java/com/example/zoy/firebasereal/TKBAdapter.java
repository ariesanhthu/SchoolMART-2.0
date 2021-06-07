package com.example.zoy.firebasereal;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TKBAdapter extends BaseAdapter {
    Context context;
    ArrayList<TKB> tkbs;

    public TKBAdapter(Context context, ArrayList<TKB> tkbs) {
        this.context = context;
        this.tkbs = tkbs;
    }

    @Override
    public int getCount() {
        return tkbs.size();
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

        view = LayoutInflater.from(context).inflate(R.layout.customlayout, viewGroup, false);
        TextView viewmon = (TextView) view.findViewById(R.id.ViewMon);
        TextView viewtg = (TextView) view.findViewById(R.id.ViewTG);
        TextView viewgv = (TextView) view.findViewById(R.id.ViewGV);
        TextView viewbuoi = (TextView) view.findViewById(R.id.ViewBuoi);
        TextView viewtiet = (TextView) view.findViewById(R.id.ViewTiet);

        viewmon.setText(tkbs.get(i).getMon());
        viewgv.setText(tkbs.get(i).getGv());
        viewtg.setText(tkbs.get(i).getTg());
        viewbuoi.setText(tkbs.get(i).getBuoi());
        viewtiet.setText(tkbs.get(i).getId());

        if (tkbs.get(i).getBuoi() != null) {
            viewtiet.setBackgroundColor(Color.argb(0, 255, 255, 255));
            if (tkbs.get(i).getBuoi().equals("Sáng")) {
                view.setBackgroundColor(Color.parseColor("#fffa94"));
            } else {
                if (!tkbs.get(i).getBuoi().equals("Sáng")){
                    view.setBackgroundColor(context.getResources().getColor(android.R.color.white));
                }
            }
            if (tkbs.get(i).getBuoi().equals("Chiều")) {
                view.setBackgroundColor(Color.parseColor("#ffc94a"));
            }
        } else  viewtiet.setBackgroundColor(Color.argb(255, 233, 30, 99));

        return view;
    }
}
