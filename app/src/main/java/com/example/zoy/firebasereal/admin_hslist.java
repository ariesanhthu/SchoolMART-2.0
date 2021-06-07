package com.example.zoy.firebasereal;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class admin_hslist extends AppCompatActivity {
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
        setContentView(R.layout.activity_admin_hslist);
        listView = findViewById(R.id.hs_list);
        search = findViewById(R.id.search_id);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        lop = getIntent().getStringExtra("lop");
        setTitle("DANH SÁCH LỚP "+lop.substring(lop.indexOf(" ")+1));
        
        HS_list();
    }
    private void HS_list(){
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                String id = "HS"+lop.substring(5);

//                int maxlist = Integer.parseInt(dataSnapshot.child(lop).child("Số HS").getValue().toString());
//                Log.d("out", String.valueOf(maxlist));
                final ArrayList<String> stringID = new ArrayList<>();
                stringList.clear();

                for (DataSnapshot keyNode : dataSnapshot.child("USER").getChildren()){
                    if (String.valueOf(keyNode.getKey()).substring(0,2).equals("HS")) {
                        if (String.valueOf(keyNode.getKey()).substring(0, id.length()).equals(id)) {
                            String ID = String.valueOf(keyNode.getKey());
                            String stt = ID.substring(id.length(), ID.length());
                            stringList.add(stt + ". " + String.valueOf(keyNode.child("name").getValue()));
                            stringID.add(keyNode.getKey());
                        }
                    }
                }
                Log.d("out", String.valueOf(stringList.size()));
                for (DataSnapshot keyNode : dataSnapshot.child("USER").getChildren()){
                    if (String.valueOf(keyNode.getKey()).substring(0,2).equals("HS")) {
                        if (String.valueOf(keyNode.getKey()).substring(0, id.length()).equals(id)) {
                            String ID = String.valueOf(keyNode.getKey());
                            String stt = ID.substring(id.length());
                            stringList.set(Integer.parseInt(stt)-1, stt + ". " + String.valueOf(keyNode.child("name").getValue()));
                            stringID.set(Integer.parseInt(stt)-1, keyNode.getKey());
                        }
                    }
                }
                arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, stringList);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(context, stringID.get(i), Toast.LENGTH_SHORT).show();
                        User user = dataSnapshot.child("USER").child(stringID.get(i)).getValue(User.class);
                        Dialogthongtinhs(user, stringID.get(i));
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
    private void Dialogthongtinhs(User user, final String id){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_thongtin_gv);

        Button huy = (Button) dialog.findViewById(R.id.huy);
        Button xacnhan = (Button) dialog.findViewById(R.id.xacnhan);
        final TextInputLayout tendn = (TextInputLayout) dialog.findViewById(R.id.chinhsua_tendn);
        final TextInputLayout pass = (TextInputLayout) dialog.findViewById(R.id.chinhsua_pass);
        final TextInputLayout hoten = (TextInputLayout) dialog.findViewById(R.id.chinhsua_hoten);
        final TextInputLayout lop = (TextInputLayout) dialog.findViewById(R.id.chinhsua_mon);
        final TextInputLayout sdt = (TextInputLayout) dialog.findViewById(R.id.chinhsua_sdt);
        final TextInputLayout email = (TextInputLayout) dialog.findViewById(R.id.chinhsua_email);

        tendn.getEditText().setText(id);
        tendn.getEditText().setEnabled(false);
        pass.getEditText().setText(user.getPassword());
        hoten.getEditText().setText(user.getName());
        lop.getEditText().setText(user.getLop());
        lop.setHint("Lớp");
        sdt.getEditText().setText(user.getSdt());
        email.getEditText().setText(user.getEmail());

        xacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pass.getEditText().getText().toString().isEmpty()) pass.setError("Hãy nhập mật khẩu"); else
                {
                    pass.setError(null);
                    if (hoten.getEditText().getText().toString().isEmpty()) hoten.setError("Hãy nhập họ tên"); else
                    {
                        hoten.setError(null);
                        if (lop.getEditText().getText().toString().isEmpty()) lop.setError("Hãy nhập lớp"); else
                        {
                            lop.setError(null);

                            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("USER");

                            reference.child(id).setValue(new User(
                                    email.getEditText().getText().toString(),
                                    pass.getEditText().getText().toString(),
                                    hoten.getEditText().getText().toString(),
                                    lop.getEditText().getText().toString(),
                                    sdt.getEditText().getText().toString()), new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        Toast.makeText(context, "Chỉnh sửa thông tin thành công", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }else {
                                        Toast.makeText(context, "Chỉnh sửa thông tin thất bại!", Toast.LENGTH_SHORT).show();
                                    }
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
    public boolean onSupportNavigateUp() {
        finish();
        onBackPressed();
        return true;
    }
}