package com.example.zoy.firebasereal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

public class Siso_Adapter extends BaseAdapter {
    private ArrayList<siso_object> objects;
    private Context context;

    public Siso_Adapter(ArrayList<siso_object> objects, Context context) {
        this.objects = objects;
        this.context = context;
    }

    @Override
    public int getCount() {
        return objects.size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.custom_siso, viewGroup, false);
        TextView view_hs = (TextView) view.findViewById(R.id.viewname);
        final CheckBox chon_hs = (CheckBox) view.findViewById(R.id.check_hs);
        view_hs.setText(objects.get(i).getName());
        chon_hs.setChecked(objects.get(i).getCheck());
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (chon_hs.isChecked()){
//                    chon_hs.setChecked(false);
//                } else chon_hs.setChecked(true);
//            }
//        });
        chon_hs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                objects.get(i).setCheck(b);
            }
        });
        return view;
    }
}
