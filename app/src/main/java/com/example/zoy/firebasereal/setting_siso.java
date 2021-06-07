package com.example.zoy.firebasereal;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class setting_siso extends AppCompatActivity {
    private RadioGroup radioGroup;
    private ListView listView;
    private DatabaseReference databaseReference;
    private String lop;
    private ArrayList<siso_object> arrayList = new ArrayList<>();
    private Context context = this;
    private EditText search;
    private Button xacnhan;
    private TextInputLayout sohocsinh_vang;
    private TextInputLayout tenhocsinh_vang;
    private int size_search;
    private int size_listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_siso);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("ĐIỂM DANH");
        lop = getIntent().getStringExtra("lop");
        listView = findViewById(R.id.list_hs);
        radioGroup = findViewById(R.id.siso_radiogr);
        search = findViewById(R.id.search_id);
        search.setAlpha(0);
        search.setEnabled(false);
        xacnhan = findViewById(R.id.xacnhan);
        sohocsinh_vang = findViewById(R.id.sohocsinh_vang);
        tenhocsinh_vang = findViewById(R.id.tenhocsinh_vang);
        radioGroup.check(R.id.r_1);
        enter(R.id.r_1);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, final int i) {

                        search.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                ArrayList<siso_object> search_item = new ArrayList<>();

                                for (siso_object item : arrayList){
//                                    Log.d("out", charSequence.toString().toLowerCase());
//                                    Log.d("out", String.valueOf(charSequence.toString().toLowerCase().contains(item.getName().toLowerCase())));
                                    //tiet = Normalizer.normalize(tiet, Normalizer.Form.NFD);
                                    //tiet = tiet.replaceAll("[^\\p{ASCII}]", "");
                                    String text = convert(item.getName().toLowerCase());
                                    Log.d("out", text);
                                    if (text.contains(charSequence.toString().toLowerCase())
                                            || item.getName().toLowerCase().contains(charSequence.toString().toLowerCase())
                                            || text.contains(convert(charSequence.toString().toLowerCase()))){
                                        search_item.add(item);
                                    }
                                }
                                listView.setAdapter(new Siso_Adapter(search_item, context));
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                            }
                        });

                        if (i==R.id.r_2) {
                            search.setAlpha(1);
                            search.setEnabled(true);
//                            Log.d("out", String.valueOf(lop));
                            String id = "HS"+lop;
                            arrayList.clear();
                            ArrayList<String> list = new ArrayList<>();
                            ArrayList<String> listid = new ArrayList<>();
                            for (DataSnapshot keyNode : dataSnapshot.child("USER").getChildren()){
                                if (String.valueOf(keyNode.getKey()).substring(0,2).equals("HS")) {
                                    if (String.valueOf(keyNode.getKey()).substring(0, id.length()).equals(id)) {
                                        String ID = String.valueOf(keyNode.getKey());
                                        String stt = ID.substring(id.length(), ID.length());
                                        list.add(stt + ". " + String.valueOf(keyNode.child("name").getValue()));
                                        listid.add(ID);
                                        arrayList.add(new siso_object(stt + ". " + String.valueOf(keyNode.child("name").getValue()), ID));
                                        //Log.d("out", String.valueOf());
                                    }
                                }
                            }
                            for (int j=0; j<list.size(); j++)
                            {
                                arrayList.set(Integer.parseInt(list.get(j).substring(0,list.get(j).indexOf(".")))-1, new siso_object(list.get(j),listid.get(j)) );
                            }
                            listView.setAdapter(new Siso_Adapter(arrayList, context));


                        } else {
                            listView.setAdapter(null);
                            search.setEnabled(false);
                            search.setAlpha(0);
                        }
                        Log.d("out", String.valueOf(listView.getHeight()));
                        if (i==R.id.r_3){
                            DisplayMetrics metrics = context.getResources().getDisplayMetrics();


                            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) sohocsinh_vang.getLayoutParams();
                            layoutParams.height= (int) (metrics.heightPixels*(9/100.0f));
                            sohocsinh_vang.setLayoutParams(layoutParams);
                            layoutParams = (LinearLayout.LayoutParams) tenhocsinh_vang.getLayoutParams();
                            layoutParams.height= (int) (metrics.heightPixels*(30/100.0f));
                            tenhocsinh_vang.setLayoutParams(layoutParams);
                            layoutParams = (LinearLayout.LayoutParams) search.getLayoutParams();
                            layoutParams.height= 0;
                            search.setLayoutParams(layoutParams);
                            layoutParams = (LinearLayout.LayoutParams) listView.getLayoutParams();
                            layoutParams.height= 0;
                            listView.setLayoutParams(layoutParams);

                        } else {

                            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) sohocsinh_vang.getLayoutParams();
                            layoutParams.height= 0;
                            sohocsinh_vang.setLayoutParams(layoutParams);
                            layoutParams = (LinearLayout.LayoutParams) tenhocsinh_vang.getLayoutParams();
                            layoutParams.height= 0;
                            tenhocsinh_vang.setLayoutParams(layoutParams);
                            layoutParams = (LinearLayout.LayoutParams) search.getLayoutParams();
                            layoutParams.height= 181;
                            search.setLayoutParams(layoutParams);
                            layoutParams = (LinearLayout.LayoutParams) listView.getLayoutParams();
                            layoutParams.height= 1600;
                            listView.setLayoutParams(layoutParams);
                        }
                        enter(i);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void enter(final int i){
        xacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (i==R.id.r_2){
                    String s = "";
                    int v=0;
                    for (siso_object i : arrayList){
                        if (i.getCheck()) {
                            s = s+i.getName()+"\n";
                            v++;
                        }
                    }
                    Log.d("out", String.valueOf(arrayList));
                    Intent intent = new Intent();
                    intent.putExtra("vang", s);
                    intent.putExtra("sohsvang", v);
                    if (v==0){
                        intent.putExtra("check", 0);
                    } else intent.putExtra("check", 1);
                    setResult(RESULT_OK, intent);
                    finish();

                }
                if (i==R.id.r_1){
                    Intent intent = new Intent();
                    intent.putExtra("check", 0);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                if (i==R.id.r_3){
                    if (sohocsinh_vang.getEditText().getText().toString().isEmpty()) sohocsinh_vang.setError("Hãy nhập số học sinh vắng!");
                    else if (tenhocsinh_vang.getEditText().getText().toString().isEmpty()) tenhocsinh_vang.setError("Hãy tên học sinh vắng!");
                    else {
                        sohocsinh_vang.setError(null);
                        tenhocsinh_vang.setError(null);
                        Intent intent = new Intent();
                        intent.putExtra("check", 2);
                        intent.putExtra("vang", tenhocsinh_vang.getEditText().getText().toString().trim());
                        intent.putExtra("sohsvang", Integer.parseInt(sohocsinh_vang.getEditText().getText().toString()));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        });
    }
    public String convert(String str){
        str = str.replaceAll("à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ", "a");
        str = str.replaceAll("è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ", "e");
        str = str.replaceAll("ì|í|ị|ỉ|ĩ", "i");
        str = str.replaceAll("ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ", "o");
        str = str.replaceAll("ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ", "u");
        str = str.replaceAll("ỳ|ý|ỵ|ỷ|ỹ", "y");
        str = str.replaceAll("đ", "d");
        return str;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        onBackPressed();
        return true;
    }
}