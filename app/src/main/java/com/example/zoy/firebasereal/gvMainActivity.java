package com.example.zoy.firebasereal;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.Placeholder;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class gvMainActivity extends AppCompatActivity {
    private String name;
    private String mon;
    private String lop;
    private String id;
    private String tg;
    private String password;
    private LinearLayout linearLayout;
    private String thu;
    private String time_sodaubai;
    private Integer today;

    private Boolean[] click = new Boolean[3];
    private float khoancach_giua_cac_bt;
    private float bt_dautien;
    private Context context= this;

    private ListView listView;
    private ListView lisths;
    private ArrayList<String> arraydshs = new ArrayList<>();

    private ArrayList<TKB> arrayList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private TextView viewname;
    private TextView sotiet;
    private TextView viewmon;
    private TextView date;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gv_main);

        name = getIntent().getStringExtra("name");
        mon = getIntent().getStringExtra("mon");
        id = getIntent().getStringExtra("id");
        password = getIntent().getStringExtra("pass");

        setTitle("GIÁO VIÊN");

        viewname = findViewById(R.id.Viewname);
        viewname.setText(name);
        viewmon = findViewById(R.id.ViewMon);
        viewmon.setText("Môn: "+mon);
        sotiet = findViewById(R.id.ViewTime);
        lisths = findViewById(R.id.listdshs);
        date = findViewById(R.id.ViewDate);
        listView = findViewById(R.id.listtxt);


        databaseReference = FirebaseDatabase.getInstance().getReference();


        Calendar calendar = Calendar.getInstance();
        today = calendar.get(Calendar.DAY_OF_WEEK);
        String simpleDateFormat = new SimpleDateFormat("(dd/MM)", Locale.ENGLISH).format(calendar.getTime());
        if (today!=1){
            date.setText("Thứ "+String.valueOf(today)+" "+simpleDateFormat);
            thu = "Thứ "+String.valueOf(today);
        } else {
            date.setText("Chủ nhật"+" "+simpleDateFormat);
            thu = "Chủ nhật";
        }
        List_TKB(simpleDateFormat);
        simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY", Locale.ENGLISH).format(calendar.getTime());
        time_sodaubai = simpleDateFormat;

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialogdate();
            }
        });

        //--------------------------------------------------------------------------------------------------------------------------------------------------
        //Animation Thời khóa biểu
        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relative_main);
        final Button show_tkbbt = (Button) findViewById(R.id.tkbbt);
        final LinearLayout linear_tkbbt = (LinearLayout) findViewById(R.id.linear_tkbbt);
        TextView view_dstv = findViewById(R.id.ViewDshs);

        final Button show_thanhvienbt = (Button) findViewById(R.id.thanhvienbt);
        final LinearLayout linear_thanhvienbt = (LinearLayout) findViewById(R.id.linear_thanhvienbt);
        final LinearLayout linear_thanhvien = (LinearLayout) findViewById(R.id.linear_thanhvien);
        final LinearLayout linear_tiettxt = (LinearLayout) findViewById(R.id.linear_listtxt);

        ObjectAnimator animator_linear_thanhvien = ObjectAnimator.ofFloat(linear_thanhvien, "alpha", 0);
        animator_linear_thanhvien.start();
        animator_linear_thanhvien = ObjectAnimator.ofFloat(linear_thanhvien, "y", 4000);
        animator_linear_thanhvien.start();


        ObjectAnimator animator1 = ObjectAnimator.ofFloat(linear_tiettxt, "alpha", 0);
        animator1.start();
        animator1 = ObjectAnimator.ofFloat(linear_tiettxt, "y", 4000);
        animator1.start();

        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) show_tkbbt.getLayoutParams();
        params.width= (int) (metrics.widthPixels*(97.2/100.0f));
        show_tkbbt.setLayoutParams(params);
        params = (LinearLayout.LayoutParams) show_thanhvienbt.getLayoutParams();
        params.width= (int) (metrics.widthPixels*(97.2/100.0f));
        show_thanhvienbt.setLayoutParams(params);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) date.getLayoutParams();
        layoutParams.width= (int) (metrics.widthPixels*(97.2/100.0f));
        date.setLayoutParams(layoutParams);
        layoutParams = (LinearLayout.LayoutParams) listView.getLayoutParams();
        layoutParams.width= (int) (metrics.widthPixels*(97.2/100.0f));
        listView.setLayoutParams(layoutParams);
        layoutParams = (LinearLayout.LayoutParams) lisths.getLayoutParams();
        layoutParams.width= (int) (metrics.widthPixels*(97.2/100.0f));
        lisths.setLayoutParams(layoutParams);
        layoutParams = (LinearLayout.LayoutParams) view_dstv.getLayoutParams();
        layoutParams.width= (int) (metrics.widthPixels*(97.2/100.0f));
        view_dstv.setLayoutParams(layoutParams);

        khoancach_giua_cac_bt = 7.5f;
        bt_dautien = 74f;
        ObjectAnimator animator = ObjectAnimator.ofFloat(linear_tkbbt, "y", metrics.heightPixels*((bt_dautien+khoancach_giua_cac_bt)/100.0f));
        animator.start();

        //87.5-80
        ObjectAnimator animator_linear_thanhvienbt = ObjectAnimator.ofFloat(linear_thanhvienbt, "y", metrics.heightPixels*(bt_dautien/100.0f));
        animator_linear_thanhvienbt.start();


        click[0]=true;
        show_tkbbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (click[0]){
                    show_tkbbt.setEnabled(false);
                    show_thanhvienbt.setEnabled(false);
                    Integer khoancachy;
                    khoancachy = metrics.heightPixels-500-linear_thanhvienbt.getHeight()-10-312;

                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) listView.getLayoutParams();
                    params.height=khoancachy;
                    listView.setLayoutParams(params);

                    ObjectAnimator animator_linear_thanhvienbt = ObjectAnimator.ofFloat(linear_thanhvienbt, "x", 3000);
                    animator_linear_thanhvienbt.setDuration(200);
                    animator_linear_thanhvienbt.start();
                    ObjectAnimator animator = ObjectAnimator.ofFloat(linear_tkbbt, "y", 500);
                    animator.setDuration(700);
                    animator.start();
                    ObjectAnimator animator1 = ObjectAnimator.ofFloat(linear_tiettxt, "y", 500+linear_tkbbt.getHeight()+10);
                    animator1.setDuration(700);
                    animator1.start();
                    animator1 = ObjectAnimator.ofFloat(linear_tiettxt, "alpha", 1);
                    animator1.start();
                    click[0] = false;
                    show_tkbbt.setEnabled(true);
                }else {
                    show_tkbbt.setEnabled(false);
                    ObjectAnimator animator_linear_thanhvienbt = ObjectAnimator.ofFloat(linear_thanhvienbt, "x", 0);
                    animator_linear_thanhvienbt.setDuration(500);
                    animator_linear_thanhvienbt.start();
                    ObjectAnimator animator = ObjectAnimator.ofFloat(linear_tkbbt, "y", metrics.heightPixels*((bt_dautien+khoancach_giua_cac_bt)/100.0f));
                    animator.setDuration(700);
                    animator.start();
                    ObjectAnimator animator1 = ObjectAnimator.ofFloat(linear_tiettxt, "y", 4000);
                    animator1.setDuration(700);
                    animator1.start();
                    animator1 = ObjectAnimator.ofFloat(linear_tiettxt, "alpha", 0);
                    animator1.start();
                    click[0] = true;
                    show_tkbbt.setEnabled(true);
                    show_thanhvienbt.setEnabled(true);
                }
            }
        });
        click[1]=true;
        show_thanhvienbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (click[1]) {
                    show_tkbbt.setEnabled(false);
                    show_thanhvienbt.setEnabled(false);
                    Integer khoancachy;
                    khoancachy = metrics.heightPixels-500-linear_thanhvienbt.getHeight()-10-312;
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lisths.getLayoutParams();
                    params.height=khoancachy;
                    lisths.setLayoutParams(params);

                    ObjectAnimator animator_linear_tkbbt = ObjectAnimator.ofFloat(linear_tkbbt, "x", 3000);
                    animator_linear_tkbbt.setDuration(200);
                    animator_linear_tkbbt.start();
                    ObjectAnimator animator_linear_thanhvien = ObjectAnimator.ofFloat(linear_thanhvien, "alpha", 1);
                    animator_linear_thanhvien.start();
                    ObjectAnimator animator_linear_thanhvienbt = ObjectAnimator.ofFloat(linear_thanhvienbt, "y", 500);
                    animator_linear_thanhvienbt.setDuration(700);
                    animator_linear_thanhvienbt.start();
                    animator_linear_thanhvien = ObjectAnimator.ofFloat(linear_thanhvien, "y", 500+linear_thanhvienbt.getHeight()+10);
                    animator_linear_thanhvien.setDuration(700);
                    animator_linear_thanhvien.start();
                    click[1]=false;
                    show_thanhvienbt.setEnabled(true);

                } else {
                    show_thanhvienbt.setEnabled(false);

                    ObjectAnimator animator_linear_tkbbt = ObjectAnimator.ofFloat(linear_tkbbt, "x", 0);
                    animator_linear_tkbbt.setDuration(500);
                    animator_linear_tkbbt.start();
                    ObjectAnimator animator_linear_thanhvienbt = ObjectAnimator.ofFloat(linear_thanhvienbt, "y", metrics.heightPixels*(bt_dautien/100.0f));
                    animator_linear_thanhvienbt.setDuration(700);
                    animator_linear_thanhvienbt.start();
                    ObjectAnimator animator_linear_thanhvien = ObjectAnimator.ofFloat(linear_thanhvien, "y", 4000);
                    animator_linear_thanhvien.setDuration(700);
                    animator_linear_thanhvien.start();
                    animator_linear_thanhvien = ObjectAnimator.ofFloat(linear_thanhvien, "alpha", 0);
                    animator_linear_thanhvien.start();
                    click[1]=true;
                    show_thanhvienbt.setEnabled(true);
                    show_tkbbt.setEnabled(true);

                }
            }
        });
        //--------------------------------------------------------------------------------------------------------------------------------------------------
        //Animation Thành Viên

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

                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_YEAR,Integer.parseInt(thu.substring(4,thu.length()))-today);
                    String simpleDateFormat = new SimpleDateFormat("(dd/MM)", Locale.ENGLISH).format(calendar.getTime());

                    date.setText(thu+" "+simpleDateFormat);

                    List_TKB(simpleDateFormat);

                    simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY", Locale.ENGLISH).format(calendar.getTime());
                    time_sodaubai = simpleDateFormat;


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

    public void List_TKB(final String tiet){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                //Log.d("out", id);
                ArrayList<String> stringArrayList = new ArrayList<>();
                final ArrayList<Custom_thongtinlop_Object> thongtinlopObjects = new ArrayList<>();
                String sohs;
                arrayList.clear();
                stringArrayList.clear();
                thongtinlopObjects.clear();
                for (DataSnapshot keyNode : dataSnapshot.child("USER/"+id+"/class").getChildren()){
                    stringArrayList.add(String.valueOf(keyNode.getKey()));
                }
                //-------------------------------------------------------------------------------------------------------------------
                int Tiet=0;
                arrayList.add(new TKB("Sáng"));
                for (int i = 0; i<stringArrayList.size(); i++){
                    for (DataSnapshot keyNode : dataSnapshot.child("Lớp: "+stringArrayList.get(i)+"/TKB/"+thu+"/aSáng").getChildren()) {
                        String tgday = String.valueOf(keyNode.child("tg").getValue());
                        String idgv = String.valueOf(keyNode.child("id").getValue());
                        String id1 = "";
                        String id2 = "";
                        if (idgv.indexOf("-") != -1) {
                            id1 = idgv.substring(0,idgv.indexOf("-"));
                            id2 = idgv.substring(idgv.indexOf("-")+1);
                        }
                        if (id1.equals(id) || id2.equals(id)){
                            Tiet++;
                            arrayList.add(new TKB("Lớp: "+stringArrayList.get(i), null, tgday, keyNode.getKey().substring(5)));
                        }
                    }
                }
                arrayList.add(new TKB("Chiều"));
                for (int i = 0; i<stringArrayList.size(); i++){
                    for (DataSnapshot keyNode : dataSnapshot.child("Lớp: "+stringArrayList.get(i)+"/TKB/"+thu+"/bChiều").getChildren()) {
                        String tgday = String.valueOf(keyNode.child("tg").getValue());
                        String idgv = String.valueOf(keyNode.child("id").getValue());
                        String id1 = "";
                        String id2 = "";
                        if (idgv.indexOf("-") != -1) {
                            id1 = idgv.substring(0,idgv.indexOf("-"));
                            id2 = idgv.substring(idgv.indexOf("-")+1);
                        }
                        if (id1.equals(id) || id2.equals(id)){
                            Tiet++;
                            arrayList.add(new TKB("Lớp: "+stringArrayList.get(i), null, tgday, keyNode.getKey().substring(5)));
                        }
                    }
                }
                sotiet.setText("Số tiết "+tiet+": "+String.valueOf(Tiet));
                //-------------------------------------------------------------------------------------------------------------------
                for (int i = 0; i<stringArrayList.size(); i++){
                    thongtinlopObjects.add(new Custom_thongtinlop_Object("Lớp: "+stringArrayList.get(i),
                            "Tổng số HS: "+String.valueOf(dataSnapshot.child("Lớp: "+stringArrayList.get(i)+"/Số HS").getValue())));
                }

                TKBAdapter tkbAdapter = new TKBAdapter(context, arrayList);
                listView.setAdapter(tkbAdapter);
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        int j=i;
//                        Log.d("out", String.valueOf(listView.getAdapter().getView())));
                        while (arrayList.get(j).getBuoi() == null) {
                            j--;
                        }
                        String buoi;
                        if (arrayList.get(j).getBuoi().equals("Sáng")){
                            buoi = "aSáng";
                        } else buoi = "bChiều";
                        String thoigian;
                        thoigian = time_sodaubai+": Tiết "+arrayList.get(i).getId()+": "+arrayList.get(j).getBuoi();
                        if (arrayList.get(i).getBuoi() == null) {
                            Intent intent = new Intent(getApplicationContext(), show_sodaubai.class);
                            //Log.d("out", String.valueOf(dataSnapshot.child(arrayList.get(i).getMon()).child("Số HS").getValue()));
                            intent.putExtra("sisolop", Integer.parseInt(dataSnapshot.child(arrayList.get(i).getMon()).child("Số HS").getValue().toString()));
                            intent.putExtra("id", id);
                            intent.putExtra("name", name);
                            intent.putExtra("mon", mon);
                            intent.putExtra("lop", arrayList.get(i).getMon());
                            intent.putExtra("thoigian", thoigian);
                            intent.putExtra("tiet", "Tiết "+arrayList.get(i).getId());
                            intent.putExtra("buoi", buoi);
                            intent.putExtra("time", time_sodaubai);
                            startActivity(intent);
                        }
                    }
                });
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                DSL_GV_Adapter dsl_gv_adapter = new DSL_GV_Adapter(context, thongtinlopObjects);
                lisths.setAdapter(dsl_gv_adapter);

                lisths.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getApplicationContext(), thongtin_lop.class);
                        intent.putExtra("lop", thongtinlopObjects.get(i).getLop().substring(5));
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.menu_thongtin:
                intent = new Intent(getApplicationContext(), Thongtin_setting.class);
                intent.putExtra("id",id);
                startActivity(intent);
                break;
            case R.id.menu_dangxuat:
                intent = new Intent(getApplicationContext(), Login.class);
                intent.putExtra("id", id);
                intent.putExtra("pass", password);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}