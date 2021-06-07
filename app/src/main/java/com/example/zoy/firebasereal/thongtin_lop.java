package com.example.zoy.firebasereal;

import android.support.v7.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
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
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class thongtin_lop extends AppCompatActivity {
    private String id;
    private ActionBar toolbar;
    private String lop;
    private ListView lisths;
    private Context context = this;
    private ArrayList<String> arraydshs = new ArrayList<>();
    private ArrayAdapter arrayAdapter;
    private DatabaseReference databaseReference;
    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongtin_lop);

        lop = getIntent().getStringExtra("lop");
        id = getIntent().getStringExtra("id");

        setTitle("DANH SÁCH LỚP "+lop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        lisths = findViewById(R.id.list_dslop);
        search = findViewById(R.id.search_id);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String id = "HS"+lop;
                search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        ArrayList<String> search_item = new ArrayList<>();

                        for (String item : arraydshs){
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
                        lisths.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                ArrayList<String> list = new ArrayList<>();

                arraydshs.clear();
                for (DataSnapshot keyNode : dataSnapshot.child("USER").getChildren()){
                    if (String.valueOf(keyNode.getKey()).substring(0,2).equals("HS")) {
                        if (String.valueOf(keyNode.getKey()).substring(0, id.length()).equals(id)) {
                            String ID = String.valueOf(keyNode.getKey());
                            String stt = ID.substring(id.length(), ID.length());
                            arraydshs.add(stt + ". " + String.valueOf(keyNode.child("name").getValue()));
                            list.add(stt + ". " + String.valueOf(keyNode.child("name").getValue()));
                            //Log.d("out", String.valueOf());
                        }
                    }
                }
                for (String item : list){
                    int stt1 = Integer.parseInt(item.substring(0,item.indexOf(".")))-1;
//                        Log.d("out", String.valueOf(item));
                    arraydshs.set(stt1, item);
                }
                arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, arraydshs);
                lisths.setAdapter(arrayAdapter);
                lisths.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String ID = "HS"+lop+arraydshs.get(i).substring(0,1);
                        Dialogviewthongtin(ID);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void Dialogviewthongtin(final String ID){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_viewthongtin);
        dialog.setCanceledOnTouchOutside(false);
        final TextView Viewname = (TextView) dialog.findViewById(R.id.dialog_viewname);
        final TextView Viewemail = (TextView) dialog.findViewById(R.id.dialog_viewemail);
        final TextView Viewsdt = (TextView) dialog.findViewById(R.id.dialog_viewsdt);
        Button Okbt = (Button) dialog.findViewById(R.id.dialog_okbt);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("USER");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(ID).getValue(User.class);
                Viewname.setText("Tên: "+user.getName());
                Viewemail.setText("Email: "+user.getEmail());
                Viewsdt.setText("Số ĐT: "+user.getSdt());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dialog.show();
        Okbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_change, menu);
        MenuItem item1 = menu.findItem(R.id.menu_thongtin);
        item1.setTitle("SỔ ĐẦU BÀI");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;
        switch (item.getItemId()){
            case R.id.menu_thongtin:
                intent = new Intent(getApplicationContext(), show_sodaubai.class);
                intent.putExtra("cant", false);
                intent.putExtra("lop", "Lớp: "+lop);
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