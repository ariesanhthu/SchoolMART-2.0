package com.example.zoy.firebasereal;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class Admin extends AppCompatActivity {
    private String name;
    private Context context = this;

    private TextView Name;
    private ListView listView;
    private ListView listgv;
    private Button button;
    private Button addgv;

    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<String> arrayList_gv = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private ArrayAdapter<String> arrayAdapter_gv;
    private EditText search;
    private EditText search1;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        name = getIntent().getStringExtra("name");
        setTitle("QUẢN TRỊ VIÊN");
        listgv = findViewById(R.id.listgv);

        listView = findViewById(R.id.listtxt);
        button = findViewById(R.id.addclass);
        addgv = findViewById(R.id.addgv);
        search = findViewById(R.id.search_id);
        search1 = findViewById(R.id.search_id1);

        databaseReference.child("USER").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                final ArrayList<String> arrayList_data = new ArrayList<>();
                arrayList_gv.clear();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    if (keyNode.getKey().substring(0, 2).equals("GV"))
                    {
                        arrayList_gv.add(String.valueOf(keyNode.child("name").getValue())+" - Môn:"+String.valueOf(keyNode.child("mon").getValue()));
                        arrayList_data.add(keyNode.getKey());

                    }
                }
                arrayAdapter_gv = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,arrayList_gv);
                listgv.setAdapter(arrayAdapter_gv);
                listgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getApplicationContext(), admin_gv_caclopday.class);
                        intent.putExtra("id", arrayList_data.get(i));
                        startActivity(intent);
                    }
                });
                search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        ArrayList<String> search_item = new ArrayList<>();

                        for (String item : arrayList_gv){
                            Log.d("out", charSequence.toString().toLowerCase());
//                                    Log.d("out", String.valueOf(charSequence.toString().toLowerCase().contains(item.getName().toLowerCase())));
                            //tiet = Normalizer.normalize(tiet, Normalizer.Form.NFD);
                            //tiet = tiet.replaceAll("[^\\p{ASCII}]", "");
                            String text = convert(item.toLowerCase());
                            Log.d("out", text);
                            if (text.contains(charSequence.toString().toLowerCase())
                                    || item.toLowerCase().contains(charSequence.toString().toLowerCase())
                                    || text.contains(convert(charSequence.toString().toLowerCase()))){
                                search_item.add(item);
                            }
                        }
                        arrayAdapter_gv = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, search_item);
                        listgv.setAdapter(arrayAdapter_gv);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot keyNode1 : dataSnapshot.getChildren()) {

                    if (!keyNode1.getKey().equals("USER") && !keyNode1.getKey().equals("Data")){
                        arrayList.add(keyNode1.getKey());
                        //Log.d("out", String.valueOf(keyNode1.getKey()));
                    }
                }
                arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,arrayList);
                listView.setAdapter(arrayAdapter);

                search1.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        ArrayList<String> search_item = new ArrayList<>();

                        for (String item : arrayList){
                            Log.d("out", charSequence.toString().toLowerCase());
//                                    Log.d("out", String.valueOf(charSequence.toString().toLowerCase().contains(item.getName().toLowerCase())));
                            //tiet = Normalizer.normalize(tiet, Normalizer.Form.NFD);
                            //tiet = tiet.replaceAll("[^\\p{ASCII}]", "");
                            String text = convert(item.toLowerCase());
                            Log.d("out", text);
                            if (text.contains(charSequence.toString().toLowerCase())
                                    || item.toLowerCase().contains(charSequence.toString().toLowerCase())
                                    || text.contains(convert(charSequence.toString().toLowerCase()))){
                                search_item.add(item);
                            }
                        }
                        arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, search_item);
                        listView.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(Admin.this, String.valueOf(i), Toast.LENGTH_SHORT).show();
                Dialogchosse(arrayList.get(i));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        Dialogdate();
            }
        });

        addgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialogthongtingv();
            }
        });
    }


    private void  Dialogchosse(final String lop){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_chosse_admin);

        Button sodaubaibt = (Button) dialog.findViewById(R.id.sodaubai_bt);
        Button gvbt = (Button) dialog.findViewById(R.id.giaovien_bt);
        Button hsbt = (Button) dialog.findViewById(R.id.hocsinh_bt);
        Button tkbbt = (Button) dialog.findViewById(R.id.tkb_bt);
        Button xoabt = (Button) dialog.findViewById(R.id.xoalop_bt);
        Button huy = (Button) dialog.findViewById(R.id.huy_bt);

        sodaubaibt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin.this, show_sodaubai.class);
                intent.putExtra("lop", lop);
                intent.putExtra("id", "ADMIN");
                intent.putExtra("cant", false);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        gvbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin.this, admin_gvlist.class);
                intent.putExtra("lop", lop);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        tkbbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), admin_tkb.class);
                intent.putExtra("lop", lop);
                startActivities(new Intent[]{intent});
                dialog.dismiss();
            }
        });
        hsbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), admin_hslist.class);
                intent.putExtra("lop", lop);
                startActivities(new Intent[]{intent});
                dialog.dismiss();
            }
        });
        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void Dialogdate(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_addclass);

        final EditText editText = (EditText) dialog.findViewById(R.id.editclass);
        final EditText editText1 = (EditText) dialog.findViewById(R.id.editshs);
        Button button1 = (Button) dialog.findViewById(R.id.addbt);
        Button button2 = (Button) dialog.findViewById(R.id.huybt);
        dialog.show();


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                    editText.setError("Hãy nhập lớp");
                } else if (TextUtils.isEmpty(editText1.getText().toString().trim())){
                    editText1.setError("Hãy nhập số học sinh");
                }else {
                    Integer shs = Integer.parseInt(editText1.getText().toString().trim());
                    String lop = "Lớp: " + editText.getText().toString().trim().toUpperCase();
                    //Log.d("out", lop);
                    String buoi;
                    String id;
                    Integer hh;
                    Integer mm;
                    Integer thu;
                    Integer i;
                    Integer tiet;
                    Integer j;
                    String mmstr;
                    for (thu = 2; thu < 7; thu++) {
                        for (i = 0; i <= 1; i++) {
                            if (i == 0) buoi = "aSáng";
                            else buoi = "bChiều";
                            tiet = 5;
                            hh = 6;
                            mm = 45;
                            mmstr = mm.toString();
                            if (i == 1) {
                                tiet = 4;
                                hh = 13;
                                mm = 45;
                                mmstr = mm.toString();
                            }
                            for (j = 1; j <= tiet; j++) {
                                databaseReference.child(lop + "/TKB")
                                        .child("Thứ " + thu.toString() + "/" + buoi + "/Tiết " + j.toString() + "/tg")
                                        .setValue(hh.toString() + "h" + mmstr);
                                if (mm + 45 >= 60) {
                                    mm = 45 - 15;
                                    mmstr = mm.toString();
                                    if (mmstr.equals("0")) mmstr = null;
                                    hh++;
                                }
                                databaseReference.child(lop + "/TKB")
                                        .child("Thứ " + thu.toString() + "/" + buoi + "/Tiết " + j.toString() + "/mon")
                                        .setValue("Trống");
                                databaseReference.child(lop + "/TKB")
                                        .child("Thứ " + thu.toString() + "/" + buoi + "/Tiết " + j.toString() + "/gv")
                                        .setValue("Trống");
                                databaseReference.child(lop + "/TKB")
                                        .child("Thứ " + thu.toString() + "/" + buoi + "/Tiết " + j.toString() + "/id")
                                        .setValue("");
                            }
                        }
                    }
                    for (i=1; i<=shs; i++){
                        id = "HS"+lop.substring(5,lop.length())+i.toString();
                        User user = new User("123@","123@","123@", lop.substring(5,lop.length()));
                        databaseReference.child(lop+"/Thành viên/"+i.toString()).setValue(id);
                        databaseReference.child("USER/"+id).setValue(user);
                    }
                    dialog.dismiss();
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
    private void addTKB(){

    }
    private void Dialogthongtingv(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_thongtin_gv);

        Button huy = (Button) dialog.findViewById(R.id.huy);
        Button xacnhan = (Button) dialog.findViewById(R.id.xacnhan);
        final TextInputLayout tendn = (TextInputLayout) dialog.findViewById(R.id.chinhsua_tendn);
        final TextInputLayout pass = (TextInputLayout) dialog.findViewById(R.id.chinhsua_pass);
        final TextInputLayout hoten = (TextInputLayout) dialog.findViewById(R.id.chinhsua_hoten);
        final TextInputLayout mon = (TextInputLayout) dialog.findViewById(R.id.chinhsua_mon);
        final TextInputLayout sdt = (TextInputLayout) dialog.findViewById(R.id.chinhsua_sdt);
        final TextInputLayout email = (TextInputLayout) dialog.findViewById(R.id.chinhsua_email);

        tendn.getEditText().setEnabled(false);


        xacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pass.getEditText().getText().toString().isEmpty()) pass.setError("Hãy nhập mật khẩu"); else
                {
                    pass.setError(null);
                    if (hoten.getEditText().getText().toString().isEmpty()) hoten.setError("Hãy nhập họ tên"); else
                    {
                        hoten.setError(null);
                        if (mon.getEditText().getText().toString().isEmpty()) mon.setError("Hãy nhập môn"); else
                        {
                            mon.setError(null);

                            final Boolean[] check = {true};
                            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("USER");
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (check[0]) {
                                        check[0] = false;
                                        ArrayList<Integer> integers = new ArrayList<>();
                                        for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                                            if (keyNode.getKey().substring(0, 2).equals("GV")) {
                                                String num = keyNode.getKey().replaceAll("\\D+", "");
                                                integers.add(Integer.parseInt(num));
                                            }
                                        }
                                        int i;
                                        Collections.sort(integers);
                                        for (i = 0; i < integers.size(); i++) {
                                            if (i + 1 != integers.get(i)) break;
                                        }

                                        String name = hoten.getEditText().getText().toString();

                                        while (name.indexOf(" ") != -1) {
                                            name = name.substring(name.indexOf(" ") + 1);
                                        }

                                        name = name.substring(0, 1).toUpperCase();

                                        final String newid = "GV" + name + String.valueOf(i + 1);


                                        reference.child(newid).setValue(new Teacher(
                                                email.getEditText().getText().toString(),
                                                pass.getEditText().getText().toString(),
                                                hoten.getEditText().getText().toString(),
                                                sdt.getEditText().getText().toString(),
                                                mon.getEditText().getText().toString()), new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                if (databaseError == null) {
                                                    Toast.makeText(context, "Chỉnh sửa thông tin thành công", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                } else {
                                                    Toast.makeText(context, "Chỉnh sửa thông tin thất bại!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }
                }
            }
        });
        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.menu_thongtin:
                intent = new Intent(getApplicationContext(), Thongtin_setting.class);
                intent.putExtra("id",getIntent().getStringExtra("id"));
                startActivity(intent);
                break;
            case R.id.menu_dangxuat:
                intent = new Intent(getApplicationContext(), Login.class);
                intent.putExtra("id", getIntent().getStringExtra("id"));
                intent.putExtra("pass", getIntent().getStringExtra("pass"));
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}