package com.example.zoy.firebasereal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DSL_GV_Adapter extends BaseAdapter {
    private Context context;
    private ArrayList<Custom_thongtinlop_Object> thonginlop;

    public DSL_GV_Adapter(Context context, ArrayList<Custom_thongtinlop_Object> thonginlop) {
        this.context = context;
        this.thonginlop = thonginlop;
    }

    @Override
    public int getCount() {
        return thonginlop.size();
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
        view = LayoutInflater.from(context).inflate(R.layout.custom_thongtinlop, viewGroup, false);
        TextView viewlop = (TextView) view.findViewById(R.id.custom_namelop);
        TextView viewsohs = (TextView) view.findViewById(R.id.custom_sohs);

        viewlop.setText(thonginlop.get(i).getLop());
        viewsohs.setText(thonginlop.get(i).getShs());

        return view;
    }
}
