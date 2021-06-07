package com.example.zoy.firebasereal;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class admin_tkb extends AppCompatActivity {
    private ListView listView;
    private TextView textView;
    private TextView textView1;

    private ArrayList<TKB> arrayList = new ArrayList<>();
    private ArrayList<String> stringList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    DatabaseReference databaseReference;
    private Integer sang;
    private Integer chieu;
    private String buoi;

    private String lop;
    private String thu;
    private String mon;
    private String gv;
    private String tg;
    private String tiet;
    private Context context = this;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tkb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lop = getIntent().getStringExtra("lop");

        //Toast.makeText(admin_tkb.this, lop, Toast.LENGTH_SHORT).show();
        listView = findViewById(R.id.listadmin);
        textView = findViewById(R.id.dateadmin);
        TKB_list();
    }

    private void TKB_list(){
        databaseReference = FirebaseDatabase.getInstance().getReference(lop+"/TKB");


        thu = "Thứ 2";
        textView.setText(thu);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialogdate();
            }
        });
        Show_tkb_list();
    }
    private void Show_tkb_list(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot keyNode1 : dataSnapshot.child(thu).getChildren()) {


                    String key1 = String.valueOf(keyNode1.getKey());
                    //Log.d("out", key1);
                    arrayList.add(new TKB(key1.substring(1,key1.length())));
                    for (DataSnapshot keyNode2 : dataSnapshot.child(thu+"/"+key1).getChildren()) {
                        String key2 = String.valueOf(keyNode2.getKey());
                        //Log.d("out", key2);
                        mon = String.valueOf(keyNode2.child("mon").getValue());
                        gv = String.valueOf(keyNode2.child("gv").getValue());
                        tg = String.valueOf(keyNode2.child("tg").getValue());
                        tiet = keyNode2.getKey().substring(5);
                        arrayList.add(new TKB(mon, gv, tg, tiet));

                        //Log.d("out", String.valueOf(tkb.getMon(1)));
                    }
                }
                listView.setAdapter(new TKBAdapter(context, arrayList));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (arrayList.get(i).getBuoi() == null){
                            int j = i;
                            while (arrayList.get(j).getBuoi() == null){
                                j--;
                            }
                            if (arrayList.get(j).getBuoi().equals("Sáng")) buoi = "aSáng"; else buoi = "bChiều";
                            tiet = "Tiết "+arrayList.get(i).getId();
                            Log.d("out", arrayList.get(i).getMon());
                            Dialogclass(arrayList.get(i).getMon()
                                    , arrayList.get(i).getGv()
                                    ,arrayList.get(i).getTg()
                                    ,buoi
                                    ,tiet);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void Dialogdate(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_choosedate);
        final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radiogr);
        final Button button = (Button) dialog.findViewById(R.id.chonbt);
        Button button1 = (Button) dialog.findViewById(R.id.huybtdialog);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int btid = radioGroup.getCheckedRadioButtonId();
                //Log.d("out", String.valueOf(btid));
                if (btid!=-1) {
                    RadioButton radioButton = (RadioButton) dialog.findViewById(btid);
                    thu = (String) radioButton.getText();
                    textView.setText(thu);
                    Show_tkb_list();
                    dialog.dismiss();
                } else button.setError("Hãy chọn thứ");
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setEnabled(false);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void Dialogclass(String mon, final String gv, String tg, final String buoi, final String tiet){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.admin_setclass);
        TextView textView = (TextView) dialog.findViewById(R.id.viewtiet_admin);
        final EditText editmon = (EditText) dialog.findViewById(R.id.editmon_admin);
        final EditText editgv = (EditText) dialog.findViewById(R.id.editgv_admin);
        final EditText edittg = (EditText) dialog.findViewById(R.id.edittg_admin);
        Button buttonxacnhan = (Button) dialog.findViewById(R.id.xacnhanbt_admin);
        Button buttonhuy = (Button) dialog.findViewById(R.id.huybt_admin_dialog);
        textView.setText(tiet);

        editmon.setText(mon);
        editgv.setText(gv);
        edittg.setText(tg);
        buttonxacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mon;
                String gv;
                String tg;
                mon = editmon.getText().toString().trim();
                gv = editgv.getText().toString().trim();
                tg = edittg.getText().toString().trim();
                if (TextUtils.isEmpty(mon)) editmon.setError("Hãy nhập môn học"); else
                    if (TextUtils.isEmpty(gv)) editgv.setError("Hãy nhập giáo viên"); else
                        if (TextUtils.isEmpty(tg)) edittg.setError("Hãy nhập thời gian"); else {

                            TKB tkb = new TKB(mon, gv, tg);
                            databaseReference.child(thu + "/" + buoi + "/" + tiet).setValue(tkb, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    if (databaseError == null) Toast.makeText(admin_tkb.this, "Sửa/Thêm thành công", Toast.LENGTH_SHORT).show();
                                    else Toast.makeText(admin_tkb.this, "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
            }
        });
        buttonhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}