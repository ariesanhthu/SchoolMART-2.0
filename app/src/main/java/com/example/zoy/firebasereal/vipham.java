package com.example.zoy.firebasereal;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

public class vipham extends AppCompatActivity {


    private String lop;
    private String address;
    private Button add;
    private ListView listView;
    private vipham_object vipham;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vipham);

        address = getIntent().getStringExtra("address");
        vipham = (vipham_object) getIntent().getSerializableExtra("vipham");
        lop = getIntent().getStringExtra("lop");
        setTitle("NHẬN XÉT "+lop);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        add = findViewById(R.id.them_vipham);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, vipham_setting.class);
                intent.putExtra("lop", lop);
                intent.putExtra("vipham", vipham);
                startActivityForResult(intent, 1);
            }
        });
        listView = findViewById(R.id.list_vipham);
        listView.setAdapter(new vipham_adapter(vipham, context));

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

            }
        }
    }
}