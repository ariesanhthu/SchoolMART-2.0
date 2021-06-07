package com.example.zoy.firebasereal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Delete extends AppCompatActivity {
    EditText tID;
    Button btnDelete;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        tID=(EditText) findViewById(R.id.tID);
        btnDelete=(Button) findViewById(R.id.btnDelete);
        databaseReference= FirebaseDatabase.getInstance().getReference("Students");
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ID = tID.getText().toString().trim();
                //databaseReference.child("Students").child(ID);
                //databaseReference.setValue(null);
                databaseReference.child("Students").child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //databaseReference = FirebaseDatabase.getInstance().getReference("Students");
                        databaseReference.child(ID).removeValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Toast.makeText(Delete.this,"Student is deleted",Toast.LENGTH_LONG).show();
                tID.setText("");
                Intent intphto =new Intent(getApplicationContext(),Showdata.class);
                startActivity(intphto);
            }
        });

    }
}
