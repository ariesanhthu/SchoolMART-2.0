package com.example.zoy.firebasereal;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class hsMainActivity extends AppCompatActivity {

    private TextView Name;
    private TextView Lop;
    private TextView Time;
    private TextView date;
    private LinearLayout linearLayout;

    private ListView listgv;
    private ListView listView;
    private ListView lisths;
    private ArrayList<String> arraydshs = new ArrayList<>();
    private ArrayList<TKB> arrayList = new ArrayList<>();
    private ArrayList<String> arrayGV = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private ArrayAdapter<String> arrayAdapter_GV;

    private String id;
    private String name;
    private String lop;
    private String password;

    private Context context= this;
    private Integer today;
    private Integer i;
    private String tiet;
    private String thu;
    private String mon;
    private String gv;
    private String tg;
    private Boolean[] click = new Boolean[5];
    private float khoancach_giua_cac_bt;
    private float bt_dautien;
    private float bt_thuhai;
    private float bt_thuba;
    private int time_anim;

    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hs_main);

        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        lop = getIntent().getStringExtra("lop");
        password = getIntent().getStringExtra("pass");

        setTitle("HỌC SINH");

        listgv = findViewById(R.id.listgv);
        databaseReference = FirebaseDatabase.getInstance().getReference("Lớp: "+lop);
        lisths = findViewById(R.id.listdshs);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                arrayGV.clear();
                final ArrayList<String> list_id = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.child("Lớp: "+lop+"/GVBM").getChildren()){
                    Teacher teacher = dataSnapshot.child("USER").child(keyNode.getKey()).getValue(Teacher.class);
                    arrayGV.add(teacher.getName()+" - Môn: "+teacher.getMon());
                    list_id.add(keyNode.getKey());
                    //Log.d("out", keyNode.getKey());
                }
                arrayAdapter_GV = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, arrayGV);
                listgv.setAdapter(arrayAdapter_GV);
                listgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Teacher teacher = dataSnapshot.child("USER").child(list_id.get(i)).getValue(Teacher.class);
                        Dialogdelet(teacher);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("USER");
            databaseReference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String id1 = "HS"+lop;
                    ArrayList<String> list = new ArrayList<>();
                    arraydshs.clear();
                    for (DataSnapshot keyNode : dataSnapshot.getChildren()){
                        if (String.valueOf(keyNode.getKey()).substring(0,2).equals("HS")) {
                            if (String.valueOf(keyNode.getKey()).substring(0, id1.length()).equals(id1)) {
                                String ID = String.valueOf(keyNode.getKey());
                                String stt = ID.substring(id1.length(), ID.length());
                                arraydshs.add(stt + ". " + String.valueOf(keyNode.child("name").getValue()));
                                list.add(stt + ". " + String.valueOf(keyNode.child("name").getValue()));
                                //Log.d("out", String.valueOf());
                            }
                        }
                    }

//                    Log.d("out", String.valueOf(list));
                    for (String item : list){
                        int stt1 = Integer.parseInt(item.substring(0,item.indexOf(".")))-1;
//                        Log.d("out", String.valueOf(item));
                        arraydshs.set(stt1, item);
                    }
                    final Integer stt = Integer.parseInt(id.substring(id1.length()))-1;
                    arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, arraydshs){
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            if (stt == position) view.setBackgroundColor(Color.parseColor("#fffcd4")); else
                                view.setBackgroundColor(context.getResources().getColor(android.R.color.white));
                            return view;
                        }
                    };
                    lisths.setAdapter(arrayAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        lisths.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String ID = "HS"+lop+arraydshs.get(i).substring(0,1);
                Dialogviewthongtin(ID);
            }
        });


        date = findViewById(R.id.ViewDate);
        Name = findViewById(R.id.Viewname);
        Name.setText("Tên: "+name);
        Lop = findViewById(R.id.ViewLop);
        Lop.setText("Lớp: "+lop);

        Time = findViewById(R.id.ViewTime);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialogdate();
            }
        });
        listView = findViewById(R.id.listtxt);

        Time.setText("Số tiết: ");
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

        createNotificationChannel();

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel1")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Thời khóa biểu có thay đổi")
                .setContentText("abcdxyz")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        final NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

//        databaseReference.child("TKB").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Log.d("out", "2 "+s);
//                notificationManagerCompat.notify(100, builder.build());
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        TKB_list(simpleDateFormat);
        //--------------------------------------------------------------------------------------------------------------------------------------------------
        //Animation Thời khóa biểu
        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relative_main);
        final Button show_tkbbt = (Button) findViewById(R.id.tkbbt);
        final LinearLayout linear_tkbbt = (LinearLayout) findViewById(R.id.linear_tkbbt);

        TextView view_dstv = findViewById(R.id.ViewDshs);
        TextView view_gv = findViewById(R.id.ViewGv);

        final Button show_thanhvienbt = (Button) findViewById(R.id.thanhvienbt);
        final LinearLayout linear_thanhvienbt = (LinearLayout) findViewById(R.id.linear_thanhvienbt);
        final LinearLayout linear_thanhvien = (LinearLayout) findViewById(R.id.linear_thanhvien);
        final LinearLayout linear_tiettxt = (LinearLayout) findViewById(R.id.linear_listtxt);
        final LinearLayout linear_thongbaobt = (LinearLayout) findViewById(R.id.linear_thongbaobt);
        final Button show_thongbaobt = (Button) findViewById(R.id.thongbaobt);
        final LinearLayout linear_gv = (LinearLayout) findViewById(R.id.linear_gv);
        final LinearLayout linear_gvbt = (LinearLayout) findViewById(R.id.linear_gvbt);
        final Button show_gvbt = (Button) findViewById(R.id.gvbt);

        ObjectAnimator animator_linear_thanhvien = ObjectAnimator.ofFloat(linear_thanhvien, "alpha", 0);
        animator_linear_thanhvien.start();
        animator_linear_thanhvien = ObjectAnimator.ofFloat(linear_thanhvien, "y", 4000);
        animator_linear_thanhvien.start();


        ObjectAnimator animator1 = ObjectAnimator.ofFloat(linear_tiettxt, "alpha", 0);
        animator1.start();
        animator1 = ObjectAnimator.ofFloat(linear_tiettxt, "y", 4000);
        animator1.start();
        animator1 = ObjectAnimator.ofFloat(linear_gv, "alpha", 0);
        animator1.start();
        animator1 = ObjectAnimator.ofFloat(linear_gv, "y", 4000);
        animator1.start();

        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
//        Log.d("out", String.valueOf(metrics.widthPixels));
//        Log.d("out", String.valueOf(metrics.heightPixels));

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) show_tkbbt.getLayoutParams();
        params.width= (int) (metrics.widthPixels*(97.2/100.0f));
        show_tkbbt.setLayoutParams(params);
        params = (LinearLayout.LayoutParams) show_thanhvienbt.getLayoutParams();
        params.width= (int) (metrics.widthPixels*(97.2/100.0f));
        show_thanhvienbt.setLayoutParams(params);
        params = (LinearLayout.LayoutParams) show_gvbt.getLayoutParams();
        params.width= (int) (metrics.widthPixels*(97.2/100.0f));
        show_gvbt.setLayoutParams(params);
        params = (LinearLayout.LayoutParams) show_thongbaobt.getLayoutParams();
        params.width= (int) (metrics.widthPixels*(97.2/100.0f));
        show_thongbaobt.setLayoutParams(params);

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
        layoutParams = (LinearLayout.LayoutParams) view_gv.getLayoutParams();
        layoutParams.width= (int) (metrics.widthPixels*(97.2/100.0f));
        view_gv.setLayoutParams(layoutParams);
        layoutParams = (LinearLayout.LayoutParams) listgv.getLayoutParams();
        layoutParams.width= (int) (metrics.widthPixels*(97.2/100.0f));
        listgv.setLayoutParams(layoutParams);

        layoutParams = (LinearLayout.LayoutParams) listgv.getLayoutParams();
        layoutParams.height= (int) (metrics.heightPixels-510);
        listgv.setLayoutParams(layoutParams);
        layoutParams = (LinearLayout.LayoutParams) listView.getLayoutParams();
        layoutParams.height= (int) (metrics.heightPixels-510);
        listView.setLayoutParams(layoutParams);
        layoutParams = (LinearLayout.LayoutParams) lisths.getLayoutParams();
        layoutParams.height= (int) (metrics.heightPixels-510);
        lisths.setLayoutParams(layoutParams);

        khoancach_giua_cac_bt = 7.5f;
        bt_dautien = 81.5f;
        time_anim = 500;

        //87.5-80

        ObjectAnimator animator = ObjectAnimator.ofFloat(linear_tkbbt, "y", metrics.heightPixels*((bt_dautien)/100.0f));
        animator.start();

        animator = ObjectAnimator.ofFloat(linear_thanhvienbt, "y", metrics.heightPixels*((bt_dautien-khoancach_giua_cac_bt)/100.0f));
        animator.start();

        bt_thuhai = bt_dautien-khoancach_giua_cac_bt;

        animator = ObjectAnimator.ofFloat(linear_gvbt, "y", metrics.heightPixels*((bt_thuhai-khoancach_giua_cac_bt)/100.0f));
        animator.start();

        bt_thuba = bt_thuhai-khoancach_giua_cac_bt;

        animator = ObjectAnimator.ofFloat(linear_thongbaobt, "y", metrics.heightPixels*((bt_thuba-khoancach_giua_cac_bt)/100.0f));
        animator.start();

//        Log.d("out", String.valueOf(show_tkbbt.getY()));
        click[0]=true;
        show_tkbbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (click[0]){
                    show_tkbbt.setEnabled(false);
                    anim_hidden(0);


                    Integer khoancachy;
                    khoancachy = metrics.heightPixels-500-linear_thanhvienbt.getHeight()-10-312;
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) listView.getLayoutParams();
                    params.height=khoancachy;
                    listView.setLayoutParams(params);

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


                    ObjectAnimator animator = ObjectAnimator.ofFloat(linear_tkbbt, "y", metrics.heightPixels*((bt_dautien)/100.0f));
                    animator.setDuration(700);
                    animator.start();
                    ObjectAnimator animator1 = ObjectAnimator.ofFloat(linear_tiettxt, "y", 4000);
                    animator1.setDuration(700);
                    animator1.start();
                    animator1 = ObjectAnimator.ofFloat(linear_tiettxt, "alpha", 0);
                    animator1.start();
                    click[0] = true;

                    show_tkbbt.setEnabled(true);
                    anim_show(0);
                }
            }
        });
        click[1]=true;
        show_thanhvienbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (click[1]) {
                    show_thanhvienbt.setEnabled(false);

                    Integer khoancachy;
                    khoancachy = metrics.heightPixels-500-linear_thanhvienbt.getHeight()-10-312;
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lisths.getLayoutParams();
                    params.height=khoancachy;
                    lisths.setLayoutParams(params);

                    anim_hidden(1);

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



                    ObjectAnimator animator_linear_thanhvienbt = ObjectAnimator.ofFloat(linear_thanhvienbt, "y", metrics.heightPixels*((bt_dautien-khoancach_giua_cac_bt)/100.0f));
                    animator_linear_thanhvienbt.setDuration(700);
                    animator_linear_thanhvienbt.start();
                    ObjectAnimator animator_linear_thanhvien = ObjectAnimator.ofFloat(linear_thanhvien, "y", 4000);
                    animator_linear_thanhvien.setDuration(700);
                    animator_linear_thanhvien.start();
                    animator_linear_thanhvien = ObjectAnimator.ofFloat(linear_thanhvien, "alpha", 0);
                    animator_linear_thanhvien.start();
                    click[1]=true;

                    show_thanhvienbt.setEnabled(true);
                    anim_show(1);
                }
            }
        });
        click[2] = true;
        show_gvbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (click[2]) {
                    show_gvbt.setEnabled(false);
                    anim_hidden(2);
                    ObjectAnimator animator = ObjectAnimator.ofFloat(linear_gvbt, "y", 500);
                    animator.setDuration(700);
                    animator.start();
                    animator = ObjectAnimator.ofFloat(linear_gv, "y", 500+linear_gvbt.getHeight()+10);
                    animator.setDuration(700);
                    animator.start();
                    animator = ObjectAnimator.ofFloat(linear_gv, "alpha", 1);
                    animator.start();
                    click[2]=false;
                    show_gvbt.setEnabled(true);
                }else {
                    show_gvbt.setEnabled(false);
                    ObjectAnimator animator = ObjectAnimator.ofFloat(linear_gvbt, "y", metrics.heightPixels*((bt_thuhai-khoancach_giua_cac_bt)/100.0f));
                    animator.setDuration(700);
                    animator.start();
                    animator = ObjectAnimator.ofFloat(linear_gv, "y", 4000);
                    animator.setDuration(700);
                    animator.start();
                    animator = ObjectAnimator.ofFloat(linear_gv, "alpha", 0);
                    animator.start();
                    click[2]=true;
                    show_gvbt.setEnabled(true);
                    anim_show(2);
                }
            }
        });
        click[3] = true;
        show_thongbaobt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (click[3]) {
                    show_thongbaobt.setEnabled(false);
                    anim_hidden(3);
                    ObjectAnimator animator = ObjectAnimator.ofFloat(linear_thongbaobt, "y", 500);
                    animator.setDuration(700);
                    animator.start();
                    click[3]=false;
                    show_thongbaobt.setEnabled(true);
                }else {
                    show_thongbaobt.setEnabled(false);
                    ObjectAnimator animator = ObjectAnimator.ofFloat(linear_thongbaobt, "y", metrics.heightPixels*((bt_thuba-khoancach_giua_cac_bt)/100.0f));
                    animator.setDuration(700);
                    animator.start();
                    click[3]=true;
                    show_thongbaobt.setEnabled(true);
                    anim_show(3);
                }
            }
        });
        //--------------------------------------------------------------------------------------------------------------------------------------------------
        //Animation Thành Viên
    }
    private void anim_hidden(int id){
        Button show_tkbbt = (Button) findViewById(R.id.tkbbt);
        Button show_thanhvienbt = (Button) findViewById(R.id.thanhvienbt);
        Button show_gvbt = (Button) findViewById(R.id.gvbt);
        Button show_thongbaobt = (Button) findViewById(R.id.thongbaobt);

        LinearLayout linear_tkbbt = (LinearLayout) findViewById(R.id.linear_tkbbt);
        LinearLayout linear_thanhvienbt = (LinearLayout) findViewById(R.id.linear_thanhvienbt);
        LinearLayout linear_gvbt = (LinearLayout) findViewById(R.id.linear_gvbt);
        LinearLayout linear_thongbaobt = (LinearLayout) findViewById(R.id.linear_thongbaobt);

        ObjectAnimator animator;
        if (id!=0){
            show_tkbbt.setEnabled(false);
            animator = ObjectAnimator.ofFloat(linear_tkbbt, "x", 3000);
            animator.setDuration(time_anim);
            animator.start();
        }
        if (id!=1){
            show_thanhvienbt.setEnabled(false);
            animator = ObjectAnimator.ofFloat(linear_thanhvienbt, "x", 3000);
            animator.setDuration(time_anim);
            animator.start();
        }
        if (id!=2){
            show_gvbt.setEnabled(false);
            animator = ObjectAnimator.ofFloat(linear_gvbt, "x", 3000);
            animator.setDuration(time_anim);
            animator.start();
        }
        if (id!=3){
            show_thongbaobt.setEnabled(false);
            animator = ObjectAnimator.ofFloat(linear_thongbaobt, "x", 3000);
            animator.setDuration(time_anim);
            animator.start();
        }
    }
    private void anim_show(int id){
        Button show_tkbbt = (Button) findViewById(R.id.tkbbt);
        Button show_thanhvienbt = (Button) findViewById(R.id.thanhvienbt);
        Button show_gvbt = (Button) findViewById(R.id.gvbt);
        Button show_thongbaobt = (Button) findViewById(R.id.thongbaobt);

        LinearLayout linear_tkbbt = (LinearLayout) findViewById(R.id.linear_tkbbt);
        LinearLayout linear_thanhvienbt = (LinearLayout) findViewById(R.id.linear_thanhvienbt);
        LinearLayout linear_gvbt = (LinearLayout) findViewById(R.id.linear_gvbt);
        LinearLayout linear_thongbaobt = (LinearLayout) findViewById(R.id.linear_thongbaobt);

        ObjectAnimator animator;
        if (id!=0){
            animator = ObjectAnimator.ofFloat(linear_tkbbt, "x", 0);;
            animator.setDuration(time_anim);
            animator.start();
            show_tkbbt.setEnabled(true);
        }
        if (id!=1){
            animator = ObjectAnimator.ofFloat(linear_thanhvienbt, "x", 0);;
            animator.setDuration(time_anim);
            animator.start();
            show_thanhvienbt.setEnabled(true);
        }
        if (id!=2){
            animator = ObjectAnimator.ofFloat(linear_gvbt, "x", 0);;
            animator.setDuration(time_anim);
            animator.start();
            show_gvbt.setEnabled(true);
        }
        if (id!=3){
            animator = ObjectAnimator.ofFloat(linear_thongbaobt, "x", 0);;
            animator.setDuration(time_anim);
            animator.start();
            show_thongbaobt.setEnabled(true);
        }
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "TKBChannel";
            String description = "Thay đổi TKB";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void Dialogviewthongtin(final String ID){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_viewthongtin);
        dialog.setCanceledOnTouchOutside(false);
        final TextView Viewname = (TextView) dialog.findViewById(R.id.dialog_viewname);
        final TextView Viewemail = (TextView) dialog.findViewById(R.id.dialog_viewemail);
        final TextView Viewsdt = (TextView) dialog.findViewById(R.id.dialog_viewsdt);
        Button Okbt = (Button) dialog.findViewById(R.id.dialog_okbt);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("USER");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(ID).getValue(User.class);
                Viewname.setText("Tên: "+user.getName());
                Viewemail.setText("Email: "+user.getEmail());
                Viewsdt.setText("Số ĐT: "+user.getSdt());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dialog.show();
        Okbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
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

                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_YEAR,Integer.parseInt(thu.substring(4,thu.length()))-today);
                    String simpleDateFormat = new SimpleDateFormat("(dd/MM)", Locale.ENGLISH).format(calendar.getTime());

                    date.setText(thu+" "+simpleDateFormat);
                    TKB_list(simpleDateFormat);
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

    public void TKB_list(final String ngaythang){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int t=0;
                arrayList.clear();
                for (DataSnapshot keyNode1 : dataSnapshot.child("TKB/"+thu).getChildren()) {

                    String key1 = String.valueOf(keyNode1.getKey());
                    arrayList.add(new TKB(key1.substring(1,key1.length())));
                    for (DataSnapshot keyNode2 : dataSnapshot.child("TKB/"+thu+"/"+key1).getChildren()) {
                        String key2 = String.valueOf(keyNode2.getKey());
                        mon = String.valueOf(keyNode2.child("mon").getValue());
                        gv = String.valueOf(keyNode2.child("gv").getValue());
                        tg = String.valueOf(keyNode2.child("tg").getValue());
                        tiet = keyNode2.getKey().substring(5);
                        if (!mon.equals("Trống")) t++;
                        arrayList.add(new TKB(mon, gv, tg, tiet));
                    }
                }
                TKBAdapter tkbAdapter = new TKBAdapter(context, arrayList);
                listView.setAdapter(tkbAdapter);
                Time.setText("Số tiết "+ngaythang+": "+String.valueOf(t));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Dialogdelet(Teacher teacher){

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setMessage(teacher.getName()+"\n"+"Môn: "+teacher.getMon()+"\n"+"Số điện thoại: "+teacher.getSdt()+"\n"+"Email: "+teacher.getEmail());

        alertDialog.setTitle("Thông tin");

        alertDialog.setCancelable(true);

        alertDialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
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