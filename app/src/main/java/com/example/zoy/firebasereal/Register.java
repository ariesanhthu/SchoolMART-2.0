package com.example.zoy.firebasereal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Register extends AppCompatActivity {
    EditText tID,tName,tEmail,tPassword,tConfirmPassword;
    Button btnRegister;
    ListView ListUser;
    DatabaseReference databaseReference;
    private TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("Đăng kí");
        result=(TextView)findViewById(R.id.textView2);
        tID=(EditText) findViewById(R.id.tID);
        tID.setText(getIntent().getStringExtra("id"));
        tID.setEnabled(false);
        tName=(EditText) findViewById(R.id.tName);
        tEmail=(EditText) findViewById(R.id.tEmail);
        tPassword=(EditText) findViewById(R.id.tPassword);
        tConfirmPassword=(EditText) findViewById(R.id.tConfirmPassword);
        btnRegister=(Button) findViewById(R.id.btnRegister);

        databaseReference= FirebaseDatabase.getInstance().getReference("USER");
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addArrayList();
                onBackPressed();
                finish();
            }
        });
    }
    private void  addArrayList(){
        String ID = tID.getText().toString().trim();
        String email=tEmail.getText().toString().trim();
        String name =tName.getText().toString().trim();
        String password =tPassword.getText().toString().trim();
        String comfirmpassword =tConfirmPassword.getText().toString().trim();
        String resulthash = result.getText().toString().trim();

        if(TextUtils.isEmpty(ID)) {
            tID.setError("Please enter your ID!");
        }else if (TextUtils.isEmpty(name)){
            tName.setError("Hãy nhập tên bạn!");
        }else if(TextUtils.isEmpty(email)){
            tEmail.setError("Hãy nhập email!");
        }else if(TextUtils.isEmpty(password)){
            tPassword.setError("Hãy nhập mật khẩu!");
        }else if(!password.equals(comfirmpassword)){
            tConfirmPassword.setError("Hãy nhập lại mật khẩu!");
        }else{

            //String id=  databaseReference.push().getKey();
            Students students= new Students(ID,name,email);
            databaseReference.child(ID).child("id").setValue(ID.toString());
            databaseReference.child(ID).child("email").setValue(email.toString());
            databaseReference.child(ID).child("name").setValue(name.toString());
            try {
                databaseReference.child(ID).child("password").setValue(password);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Toast.makeText(this,"Student is added",Toast.LENGTH_LONG).show();
            Cleartxt();

        }

    }
    private void Cleartxt(){
        tID.setText("");
        tEmail.setText("");
        tName.setText("");
        tPassword.setText("");
        tConfirmPassword.setText("");
        result.setText("");
        tID.requestFocus();

    }
    public void computeMD5Hash(String password) {

        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer MD5Hash = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                MD5Hash.append(h);
            }

            result.setText(MD5Hash);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        onBackPressed();
        return true;
    }
}
