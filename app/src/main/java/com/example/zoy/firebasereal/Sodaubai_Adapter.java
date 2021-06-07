package com.example.zoy.firebasereal;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.Normalizer;
import java.util.ArrayList;

public class Sodaubai_Adapter extends BaseAdapter {
    private ArrayList<Sodaubai> sodaubais;
    private Context context;
    private ArrayList<vipham_object> viphamObjects;

    public Sodaubai_Adapter(ArrayList<Sodaubai> sodaubais, Context context, ArrayList<vipham_object> viphamObjects) {
        this.sodaubais = sodaubais;
        this.context = context;
        this.viphamObjects = viphamObjects;
    }

    @Override
    public int getCount() {
        return sodaubais.size();
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
        view = LayoutInflater.from(context).inflate(R.layout.custom_list_sodaubai, viewGroup, false);

        TextView thoigian = (TextView) view.findViewById(R.id.custom_thoigian);
        TextView giaovien = (TextView) view.findViewById(R.id.custom_giaovien);
        TextView mon = (TextView) view.findViewById(R.id.custom_mon);
        TextView sisohs = (TextView) view.findViewById(R.id.custom_siso);
        TextView diem = (TextView) view.findViewById(R.id.custom_diem);
        TextView tiethoctot = (TextView) view.findViewById(R.id.custom_thtot);
        TextView vipham = (TextView) view.findViewById(R.id.custom_vipham);
        String text = "Nhận xét:"+'\n';
        for (int j=0; j<viphamObjects.get(i).getLoaivipham().size(); j++)
        {
            text = text + "- "+viphamObjects.get(i).getLoaivipham().get(j)+":"+'\n';
            for (String s : viphamObjects.get(i).getName().get(j))
            {
                text = text +" + "+s+'\n';
            }
        }
        vipham.setText(text);
        thoigian.setText(sodaubais.get(i).getThoigian());
        giaovien.setText(sodaubais.get(i).getGv());
        mon.setText("Môn: "+sodaubais.get(i).getMon());
        sisohs.setText("Sỉ số lớp: "+sodaubais.get(i).getSisolop());
        diem.setText("Xếp loại: "+sodaubais.get(i).getDiem());
        tiethoctot.setText("Tiết học tốt: "+sodaubais.get(i).getTiethoctot());
//        vipham.setText("Vi phạm: "+"\n"+sodaubais.get(i).getVipham());

        return view;
    }
}
