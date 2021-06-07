package com.example.zoy.firebasereal;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class Login extends AppCompatActivity {
    EditText tEmail,tPassword;
    Button btnLogin;
    DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private CheckBox checkBox;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tEmail=(EditText) findViewById(R.id.tEmail);
        tPassword=(EditText) findViewById(R.id.tPassword);
        btnLogin=(Button) findViewById(R.id.btnLogin);
        databaseReference=FirebaseDatabase.getInstance().getReference("USER");
        checkBox = findViewById(R.id.save_acc);
        sharedPreferences = getSharedPreferences("datalogin", MODE_PRIVATE);

        tEmail.setText(getIntent().getStringExtra("id"));
        tPassword.setText(getIntent().getStringExtra("pass"));

        if (sharedPreferences.getBoolean("check", false)){
            tEmail.setText(sharedPreferences.getString("id", ""));
            tPassword.setText(sharedPreferences.getString("password", ""));

            checkBox.setChecked(true);
            if (getIntent().getStringExtra("pass") == null) {

                logIn(sharedPreferences.getString("id", ""), sharedPreferences.getString("password", ""));

            }
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd= null;
                try {
                    pwd = tPassword.getText().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                logIn(tEmail.getText().toString(),pwd);
            }
        });

    }


    private void logIn(final String id,final String password) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
          if(dataSnapshot.child(id).exists()){
              if (!id.isEmpty()){
                  User user=dataSnapshot.child(id).getValue(User.class);
                  if (user.getPassword().equals(password)){
                      Toast.makeText(Login.this,"Login Success",Toast.LENGTH_LONG).show();
                      if (password.equals("123@") || user.getEmail().equals("123@")) {
                          Intent intent = new Intent(getApplicationContext(), Register.class);
                          intent.putExtra("id", id);
                          dialog.dismiss();
                          startActivity(intent);
                      } else {
                          if (checkBox.isChecked()) {
                              SharedPreferences.Editor editor = sharedPreferences.edit();
                              editor.putBoolean("check", true);
                              editor.putString("id", id);
                              editor.putString("password", password);
                              editor.apply();
                          } else {
                              SharedPreferences.Editor editor = sharedPreferences.edit();
                              editor.clear();
                              editor.apply();
                          }
                          if (id.substring(0,2).equals("GV")) {
                              ArrayList<String> arrayList = new ArrayList<>();

                              for (DataSnapshot keyNode : dataSnapshot.child(id+"/class").getChildren()){
                                  arrayList.add(String.valueOf(keyNode.getValue()));
                              }

                              Intent intent = new Intent(getApplicationContext(), gvMainActivity.class);
                              intent.putExtra("name", user.getName());
                              intent.putExtra("id", id);
                              intent.putExtra("pass", password);
                              intent.putExtra("mon", user.getMon());
                              intent.putExtra("list_class", arrayList);
                              dialog.dismiss();
                              startActivity(intent);

                              finish();
                          }
                          if (id.substring(0,2).equals("HS")) {
                              Intent intent = new Intent(getApplicationContext(), hsMainActivity.class);
                              String name = user.getName();
                              String lop = user.getLop();
                              intent.putExtra("pass", password);
                              intent.putExtra("id", id);
                              intent.putExtra("name", name);
                              intent.putExtra("lop", lop);
                              //Log.d("out", id.substring(0,2));
                              startActivity(intent);
                              dialog.dismiss();
                              finish();
                          }
                          if (id.substring(0,2).equals("AD")) {
                              Intent intent = new Intent(getApplicationContext(), Admin.class);
                              String name = user.getName();
                              intent.putExtra("id", id);
                              intent.putExtra("pass", password);
                              intent.putExtra("name", name);
                              dialog.dismiss();
                              startActivity(intent);
                              finish();
                          }

                      }
                  }else {
                      Toast.makeText(Login.this,"Password Incorrect",Toast.LENGTH_LONG).show();
                      dialog.dismiss();
                  }
              }else {
                  Toast.makeText(Login.this,"User is not register",Toast.LENGTH_LONG).show();
                  dialog.dismiss();
              }

          }else {
              Toast.makeText(Login.this,"User is not register",Toast.LENGTH_LONG).show();
              dialog.dismiss();
          }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    }
}
