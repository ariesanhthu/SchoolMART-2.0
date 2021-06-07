package com.example.zoy.firebasereal;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Thongtin_setting extends AppCompatActivity {

    private TextInputEditText hovaten;
    private TextInputEditText email;
    private TextInputLayout lop;
    private TextInputEditText id;
    private TextInputEditText pass;
    private TextInputEditText sdt;

    private Button xacnhan;
    private Button huy;
    private Button doipass;
    private String ID;

    private SharedPreferences sharedPreferences;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongtin_setting);
        setTitle("THÔNG TIN");
        databaseReference= FirebaseDatabase.getInstance().getReference("USER");
        hovaten = findViewById(R.id.chinhsua_hoten);
        email = findViewById(R.id.chinhsua_email);
        lop = findViewById(R.id.chinhsua_viewlop);
        id = findViewById(R.id.chinhsua_viewid);
        pass = findViewById(R.id.chinhsua_pass);
        sdt = findViewById(R.id.chinhsua_sdt);
        ID = getIntent().getStringExtra("id");

        doipass = findViewById(R.id.chinhsua_doipass);
        doipass.setText("ĐỔI"+"\n"+"MẬT KHẨU");
        xacnhan = findViewById(R.id.chinhsua_xacnhan);
        huy = findViewById(R.id.chinhsua_huy);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (ID.contains("GV")){
                    Teacher teacher = dataSnapshot.child(ID).getValue(Teacher.class);
                    id.setText(ID);
                    lop.getEditText().setEnabled(false);
                    lop.setHint("Môn");
                    lop.getEditText().setText(teacher.getMon());

                    hovaten.setText(teacher.getName());
                    email.setText(teacher.getEmail());
                    pass.setText(teacher.getPassword());
                    sdt.setText(teacher.getSdt());
                }else {
                    User user = dataSnapshot.child(ID).getValue(User.class);
                    id.setText(getIntent().getStringExtra("id"));
                    lop.getEditText().setText(user.getLop());
                    lop.setEnabled(false);
                    hovaten.setText(user.getName());
                    email.setText(user.getEmail());
                    pass.setText(user.getPassword());
                    sdt.setText(user.getSdt());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        doipass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog_doipass();
            }
        });
        xacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ID.contains("GV")) {
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<String> list = new ArrayList<>();
                            for (DataSnapshot keyNode : dataSnapshot.child(ID+"/class").getChildren()){
                                list.add(keyNode.getKey());
                            }
                            databaseReference.child(ID)
                                    .setValue(new Teacher(email.getText().toString().trim(),
                                            pass.getText().toString().trim(),
                                            hovaten.getText().toString().trim(),
                                            sdt.getText().toString().trim(),
                                            lop.getEditText().getText().toString().trim()));
                            for (int i=0; i<list.size(); i++){
                                databaseReference.child(ID + "/class/"+list.get(i)).setValue("");
                            }
                        }

                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    databaseReference.child(getIntent().getStringExtra("id"))
                            .setValue(new User(email.getText().toString().trim(),
                                    pass.getText().toString().trim(),
                                    hovaten.getText().toString().trim(),
                                    lop.getEditText().getText().toString().trim(),
                                    sdt.getText().toString().trim()));
                }
            }
        });
        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thongtin_setting.super.onBackPressed();
            }
        });
    }
    private void Dialog_doipass(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_doipass);
        dialog.setCanceledOnTouchOutside(false);
        final TextInputLayout passold = (TextInputLayout) dialog.findViewById(R.id.doi_passold);
        final TextInputLayout passnew = (TextInputLayout) dialog.findViewById(R.id.doi_passnew);
        final TextInputLayout passnew1 = (TextInputLayout) dialog.findViewById(R.id.doi_passnew1);
        Button xacnhan = (Button) dialog.findViewById(R.id.doi_xacnhan);
        ImageView huy = (ImageView) dialog.findViewById(R.id.doi_huyimg);

        xacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passold.getEditText().getText().toString().isEmpty()) passold.setError("Hãy nhập mật khẩu cũ"); else
                {
                    passold.setError(null);
                    if (passnew.getEditText().getText().toString().isEmpty()) passnew.setError("Hãy nhập mật khẩu mới"); else
                    {
                        passnew.setError(null);
                        if (passnew1.getEditText().getText().toString().isEmpty()) passnew1.setError("Hãy nhập lại mật khẩu mới"); else
                        {
                            passnew1.setError(null);
                            if (!passold.getEditText().getText().toString().trim().equals(pass.getText().toString().trim())) passold.setError("Mật khẩu cũ không chính xác"); else
                            {
                                passold.setError(null);
                                if (!passnew.getEditText().getText().toString().trim().equals(passnew1.getEditText().getText().toString().trim())) passnew1.setError("Mật khẩu mới không chính xác"); else
                                {
                                    passnew1.setError(null);
                                    databaseReference.child(getIntent().getStringExtra("id")).child("password").setValue(passnew.getEditText().getText().toString().trim());
                                    Toast.makeText(Thongtin_setting.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                    sharedPreferences = getSharedPreferences("datalogin", MODE_PRIVATE);

                                    if (sharedPreferences.getBoolean("check", false)){
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("password", passnew.getEditText().getText().toString().trim());
                                        editor.apply();
                                    }
                                    dialog.dismiss();
                                }
                            }
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
        dialog.show();
    }
}