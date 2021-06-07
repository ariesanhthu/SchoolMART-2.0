package com.example.zoy.firebasereal;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class setting_sodaubai extends AppCompatActivity {

    private vipham_object vipham;
    private String time;
    private String buoi;
    private String tiet;
    private String address;
    private String lop;
    private int sisohs;
    private TextView view_vipham;
    private TextView view_siso;
    private RadioButton chose_diem;
    private RadioButton chose_tht;
    private Button set_vipham;
    private Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_sodaubai);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Sodaubai sodaubais = getIntent().getParcelableExtra("sodaubai");
        sisohs = getIntent().getIntExtra("SISO",0);
        //Log.d("out", String.valueOf(sisohs));
        time = getIntent().getStringExtra("time");
        buoi = getIntent().getStringExtra("buoi");
        tiet = getIntent().getStringExtra("tiet");
        address = getIntent().getStringExtra("address");
        lop = address.substring(address.indexOf(" ")+1,address.indexOf("/"));
        setTitle(sodaubais.getThoigian());
        set_vipham = findViewById(R.id.button_vipham);
        final Button xacnhan = (Button) findViewById(R.id.xacnhan);
        final TextInputLayout baihoc = (TextInputLayout) findViewById(R.id.sodaubai_baihoc);
        final RadioGroup gr_diem = (RadioGroup) findViewById(R.id.gr_diem);

        final RadioGroup gr_tiethoctot = (RadioGroup) findViewById(R.id.gr_tiethoctot);
        Button siso_bt = (Button) findViewById(R.id.button_siso);
        view_siso = findViewById(R.id.view_siso);
        view_vipham = findViewById(R.id.view_vipham);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        vipham = (vipham_object) getIntent().getSerializableExtra("vipham");
        //Log.d("out", String.valueOf(vipham.getName()));
        String text = "Nhận xét:"+'\n';
        for (int j=0; j<vipham.getLoaivipham().size(); j++)
        {
            text = text + "- "+vipham.getLoaivipham().get(j)+":"+'\n';
            for (String s : vipham.getName().get(j))
            {
                text = text +" + "+s+'\n';
            }
        }
        view_vipham.setText(text);
        view_vipham.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(view_vipham.getText());
                Toast.makeText(context, "Đã sao chép vi phạm!", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        switch (sodaubais.getTiethoctot()){
            case "Đạt" : gr_tiethoctot.check(R.id.chose_dat); break;
            case "Không đạt": gr_tiethoctot.check(R.id.chose_khongdat); break;
            case "Không đăng ký": gr_tiethoctot.check(R.id.chose_khongdangki); break;
            default: gr_tiethoctot.check((R.id.chose_khongdangki));
        }

        switch (sodaubais.getDiem()){
            case "A": gr_diem.check(R.id.chose_a); break;
            case "B": gr_diem.check(R.id.chose_b); break;
            case "C": gr_diem.check(R.id.chose_c); break;
            case "D": gr_diem.check(R.id.chose_d); break;
            default: gr_diem.check(R.id.chose_a);
        }
        baihoc.getEditText().setText(sodaubais.getBaihoc());
        view_siso.setText("Sỉ số lớp:"+sodaubais.getSisolop());
//        Log.d("out", lop);
        set_vipham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, vipham.class);
                intent.putExtra("lop", lop);
                intent.putExtra("address", address+ time +"/"+ buoi +"/"+ tiet);
                intent.putExtra("vipham", vipham);
                startActivityForResult(intent, 2);
            }
        });
        siso_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(setting_sodaubai.this, setting_siso.class);
                intent.putExtra("lop", lop);
                startActivityForResult(intent, 1);
            }
        });
        
        xacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chose_diem = findViewById(gr_diem.getCheckedRadioButtonId());
                chose_tht = findViewById(gr_tiethoctot.getCheckedRadioButtonId());
                //Log.d("out", String.valueOf(gr_diem.getCheckedRadioButtonId()));
                databaseReference.child(address+ time +"/"+ buoi +"/"+ tiet)
                        .setValue(new Sodaubai(sodaubais.getThoigian()
                                , sodaubais.getGv()
                                , sodaubais.getMon()
                                , view_siso.getText().toString().trim().substring(view_siso.getText().toString().indexOf(":")+1)
                                , chose_diem.getText().toString()
                                , chose_tht.getText().toString()
                                , sodaubais.getId()
                                , baihoc.getEditText().getText().toString()));
                //finish();
                //onBackPressed();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                int getcheck = data.getIntExtra("check", 0);
                switch (getcheck){
                    case  1:
                    case 2:
                        String name_vang = data.getStringExtra("vang");
                        int so_vang = data.getIntExtra("sohsvang", 0);


                        view_siso.setText("Sỉ số lớp: "+ String.valueOf(sisohs-so_vang)+"/"+String.valueOf(sisohs)+"\n"+"Vắng:"+"\n"+name_vang);
                        break;
                    case 0:
                        view_siso.setText("Sỉ số lớp: "+ String.valueOf(sisohs)+"/"+String.valueOf(sisohs));
                        break;
                }
            }
        }
    }
}