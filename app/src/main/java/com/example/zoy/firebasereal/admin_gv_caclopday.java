package com.example.zoy.firebasereal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class admin_gv_caclopday extends AppCompatActivity {
    private Button thongtingv;
    private Button addlop;
    private ListView listView;
    private String id;
    private DatabaseReference databaseReference;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_gv_caclopday);

        listView = findViewById(R.id.list_class);
        addlop = findViewById(R.id.addlop);
        thongtingv = findViewById(R.id.thongtingv);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        id = getIntent().getStringExtra("id");
        setTitle(id);
        databaseReference = FirebaseDatabase.getInstance().getReference("USER/"+id);
        databaseReference.child("class").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                arrayList.clear();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()){
                    arrayList.add(keyNode.getKey());
                }
                arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,arrayList);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Dialogdelet("class/"+arrayList.get(i));
//                        Log.d("out", "class/"+arrayList.get(i));
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addlop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialogaddlop();
            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final User user= dataSnapshot.getValue(User.class);
                thongtingv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialogthongtingv(user);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void Dialogthongtingv(User user){
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

        tendn.getEditText().setText(id);
        tendn.getEditText().setEnabled(false);
        pass.getEditText().setText(user.getPassword());
        hoten.getEditText().setText(user.getName());
        mon.getEditText().setText(user.getMon());
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
                        if (mon.getEditText().getText().toString().isEmpty()) mon.setError("Hãy nhập môn"); else
                        {
                            mon.setError(null);

                            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("USER");

                            String name = hoten.getEditText().getText().toString();

                            while (name.indexOf(" ") != -1){
                                name = name.substring(name.indexOf(" ")+1);
                            }

                            name = name.substring(0,1).toUpperCase();

                            final String newid = "GV"+name+id.replaceAll("\\D+","");

                            reference.addValueEventListener(new ValueEventListener() {

                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot keyNode : dataSnapshot.child(id+"/class").getChildren()){
                                        arrayList.add(keyNode.getKey());
                                    }
                                    for (int i=0; i<arrayList.size(); i++){
                                        reference.child(newid + "/class/"+arrayList.get(i)).setValue("");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            if (!newid.equals(id)) reference.child(id).setValue(null);

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
                                        finish();
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
    private void Dialogdelet(final String lop){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Xóa lớp");
        alertDialog.setMessage("Bạn có muốn xóa lớp "+lop.substring(lop.indexOf(" ")+1).substring(lop.indexOf("/")+1)+" không?");

        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseReference.child(lop).setValue(null);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Lớp: "+lop.substring(lop.indexOf(" ")+1).substring(lop.indexOf("/")+1));
                reference.child("GVBM").child(id).setValue(null);
            }
        });

        alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }
    private void Dialogaddlop() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_addclass);

        TextView textView = (TextView) dialog.findViewById(R.id.sohs);
        final EditText editText = (EditText) dialog.findViewById(R.id.editclass);
        final EditText editText1 = (EditText) dialog.findViewById(R.id.editshs);
        Button button1 = (Button) dialog.findViewById(R.id.addbt);
        Button button2 = (Button) dialog.findViewById(R.id.huybt);

//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Log.d("out", String.valueOf(charSequence));
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

        editText1.setEnabled(false);
        editText1.setAlpha(0);
        textView.setAlpha(0);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().isEmpty()) {
                    editText.setError("Hãy nhập tên lớp");
                } else {
                    editText.setError(null);
                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Lớp: "+editText.getText().toString().trim());
                    databaseReference1.child("GVBM").child(id).setValue("");
                    databaseReference.child("class").child(editText.getText().toString().trim()).setValue("", new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                Toast.makeText(context, "Thêm lớp "+editText.getText().toString().trim()+" thành công", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(context, "Thêm lớp thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        onBackPressed();
        return true;
    }
}