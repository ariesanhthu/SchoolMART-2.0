package com.example.zoy.firebasereal;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class show_sodaubai extends AppCompatActivity {
    private ArrayList<Sodaubai> sodaubais = new ArrayList<>();
    private ArrayList<vipham_object> vipham = new ArrayList<>();
    private ListView listView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private Button addsodaubai;
    private DatabaseReference databaseReference;
    private String lop;
    private Context context = this;
    private String tiet;
    private String buoi;
    private String thoigian;
    private String time;
    private String name;
    private String mon;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sodaubai);

        addsodaubai = findViewById(R.id.them_sodaubai);
        listView = findViewById(R.id.list_sodaudai);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Boolean check = getIntent().getBooleanExtra("cant", true);
        if (!check) {
            addsodaubai.setEnabled(false);
            addsodaubai.setAlpha(0);
        }
        id = getIntent().getStringExtra("id");
        final int sohs = getIntent().getIntExtra("sisolop", 0);
        mon = getIntent().getStringExtra("mon");
        name = getIntent().getStringExtra("name");
        lop = getIntent().getStringExtra("lop");
        setTitle("SỔ ĐẦU BÀI LỚP "+lop.substring(lop.indexOf(" ")+1));

        tiet = getIntent().getStringExtra("tiet");
        buoi = getIntent().getStringExtra("buoi");
        thoigian = getIntent().getStringExtra("thoigian");
        time = getIntent().getStringExtra("time");

        databaseReference.child(lop + "/SDB").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sodaubais.clear();
                vipham.clear();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()){
                    for (DataSnapshot keyNode1 : keyNode.getChildren()){
                        for (DataSnapshot keyNode2 : keyNode1.getChildren()){
                            Sodaubai sodaubai = keyNode2.getValue(Sodaubai.class);
                            if (sodaubai.getId().equals(id) || id.equals("ADMIN"))
                            {
                                String stt = lop.substring(lop.indexOf(" ")+1);
                                sodaubais.add(sodaubai);
                                ArrayList<ArrayList<String>> name1 = new ArrayList<>();
                                ArrayList<String> loaivipham = new ArrayList<>();
                                ArrayList<Integer> diemtru = new ArrayList<>();
                                for (DataSnapshot keyNode3 : keyNode2.child("vipham").getChildren()){
                                    ArrayList<String> name2 = new ArrayList<>();
                                    loaivipham.add(keyNode3.getKey());
                                    diemtru.add((keyNode3.child("diemtru").getValue(Integer.class)));
                                    //Log.d("out", String.valueOf(keyNode3.getValue()));
                                    for (DataSnapshot keyNode4 : keyNode3.child("name").getChildren()){
                                        name2.add(String.valueOf(keyNode4.getValue()));
                                    }
                                    name1.add(name2);
                                }
                                vipham.add(new vipham_object(loaivipham, diemtru, name1));
                            }
                        }
                    }
                }

                listView.setAdapter(new Sodaubai_Adapter(sodaubais, context, vipham));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //Log.d("out", sodaubais.get(i).getThoigian());
                        String day = sodaubais.get(i).getThoigian();

                        Intent intent = new Intent(context, setting_sodaubai.class);
                        intent.putExtra("vipham", vipham.get(i));
                        intent.putExtra("sodaubai", sodaubais.get(i));
                        //Log.d("out", day.substring(0,day.indexOf(":")));
                        intent.putExtra("time", day.substring(0,day.indexOf(":")));
                        day = day.substring(day.indexOf(" ")+1);
                        intent.putExtra("tiet", day.substring(0,day.indexOf(":")));
                        day = day.substring(day.indexOf(":")+2);
                        if (day.equals("Sáng")) day="a"+day; else day="b"+day;
                        Log.d("out", day);
                        intent.putExtra("buoi", day);
                        intent.putExtra("address", lop + "/SDB/");
                        intent.putExtra("SISO", sohs);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Log.d("out", lop.substring(lop.indexOf(" ")+1));
        addsodaubai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(lop + "/SDB/"+time+"/"+buoi+"/"+tiet).addValueEventListener(new ValueEventListener() {
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Sodaubai sodaubai = dataSnapshot.getValue(Sodaubai.class);
                        Intent intent = new Intent(context, setting_sodaubai.class);
                        //intent.putExtra("sodaubai", new Sodaubai(thoigian, name, mon, "", "", "", "", id));
                        if (sodaubai == null){
                            sodaubai = new Sodaubai(thoigian, name, mon, "", "", "", id, "");
                        }
                        //Log.d("out", String.valueOf(sohs));
                        intent.putExtra("SISO", sohs);
                        intent.putExtra("sodaubai", sodaubai);
                        intent.putExtra("time", time);
                        intent.putExtra("tiet", tiet);
                        intent.putExtra("buoi", buoi);
                        intent.putExtra("address", lop + "/SDB/");
                        startActivity(intent);
                    }
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_change, menu);
        MenuItem item1 = menu.findItem(R.id.menu_thongtin);
        item1.setTitle("THÔNG TIN LỚP "+lop.substring(lop.indexOf(" ")+1));
        if (id.equals("ADMIN")) item1.setEnabled(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;
        switch (item.getItemId()){
            case R.id.menu_thongtin:
                intent = new Intent(getApplicationContext(), thongtin_lop.class);
                intent.putExtra("lop", lop.substring(lop.indexOf(" ")+1));
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        onBackPressed();
        return true;
    }
}