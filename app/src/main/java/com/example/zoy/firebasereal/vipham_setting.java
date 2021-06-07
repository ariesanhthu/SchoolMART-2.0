package com.example.zoy.firebasereal;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class vipham_setting extends AppCompatActivity {

    private Spinner spinner;
    private ArrayList<String> namevipham = new ArrayList<>();
    private ArrayList<vipham_data> data = new ArrayList<>();
    private String address;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vipham_setting);
        spinner = findViewById(R.id.spiner);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        address = getIntent().getStringExtra("address");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                namevipham.clear();
                data.add(new vipham_data("Chọn loại vi phạm!", 0, 0));
                namevipham.add("Chọn loại vi phạm!");
                for (DataSnapshot keyNode : dataSnapshot.child("Data").child("VIPHAM").getChildren())
                {
                    String loaivipham = keyNode.getKey();
                    Integer diemlop = keyNode.child("diemlop").getValue(Integer.class);
                    Integer diemcanhan = keyNode.child("diemcanhan").getValue(Integer.class);
                    data.add(new vipham_data(loaivipham, diemlop, diemcanhan));
                    namevipham.add(loaivipham);
                }
                spinner.setAdapter(new ArrayAdapter<>(vipham_setting.this, android.R.layout.simple_spinner_dropdown_item, namevipham));
                spinner.getSelectedItemId();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        onBackPressed();
        return true;
    }
}