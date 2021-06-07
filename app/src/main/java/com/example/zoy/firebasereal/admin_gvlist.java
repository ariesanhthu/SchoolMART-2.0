package com.example.zoy.firebasereal;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class admin_gvlist extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private String lop;
    private ArrayList<String> stringList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private Context context = this;

    private ListView listView;
    private EditText search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_gvlist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        listView = findViewById(R.id.hs_list);
        search = findViewById(R.id.search_id);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        lop = getIntent().getStringExtra("lop");
        setTitle("GVBM LỚP: " + lop.substring(lop.indexOf(" ") + 1));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                stringList.clear();
                Log.d("out", "change");
                final ArrayList<String> list_id = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.child(lop + "/GVBM").getChildren()) {
                    Teacher teacher = dataSnapshot.child("USER").child(keyNode.getKey()).getValue(Teacher.class);
                    stringList.add(teacher.getName() + " - Môn: " + teacher.getMon());
                    list_id.add(keyNode.getKey());
                    //Log.d("out", keyNode.getKey());
                }
                arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, stringList);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Intent intent = new Intent(getApplicationContext(), admin_gv_caclopday.class);
                        intent.putExtra("id", list_id.get(i));
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

                        for (String item : stringList){
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