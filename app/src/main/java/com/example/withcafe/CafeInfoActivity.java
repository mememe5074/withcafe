package com.example.withcafe;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Date;


public class CafeInfoActivity extends AppCompatActivity {
    private String Cafe_Name;
    private String District;
    private String TAG = "CafeInfoActivity";
    private float avg_star;
    private float tot_star = 0;
    private float avg_seekbar;
    private int tot_seekbar = 0;
    private int tot_review = 0;
    private int tot_fast = 0;
    private int tot_ordspeed = 0;
    private int tot_slow = 0;
    private int tot_noisy = 0;
    private int tot_ordnoise = 0;
    private int tot_quiet = 0;
    private TextView time_open;
    private TextView time_close;
    private TextView wifi_ox;
    private TextView university;
    private TextView subline;
    private TextView subway;
    private TextView univ_min;
    private TextView subway_min;
    private TextView cafe_name;
    private TextView district;
    private ArrayList<String> imageList;
    private ArrayList<String> menuList;
    private ArrayList<String> near_Univ;
    private ArrayList<String> near_Subway;
    private LinearLayout menuLayout;
    private LinearLayout priceLayout;
    private LinearLayout cafe_image_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_info);

        ImageView readReview = findViewById(R.id.readReview);
        ImageView writeReview = findViewById(R.id.writeReview);

        final TextView info_fast = findViewById(R.id.info_fast);
        final TextView info_ordspeed = findViewById(R.id.info_ordspeed);
        final TextView info_slow = findViewById(R.id.info_slow);

        final TextView info_noisy = findViewById(R.id.info_noisy);
        final TextView info_ordnoise = findViewById(R.id.info_ordnoise);
        final TextView info_quiet = findViewById(R.id.info_quiet);

        final TextView kindness = findViewById(R.id.kindness);
        final TextView num_star = findViewById(R.id.num_star);

        time_open = findViewById(R.id.time_open);
        time_close = findViewById(R.id.time_close);
        wifi_ox = findViewById(R.id.wifi_ox);
        university = findViewById(R.id.university);
        subline = findViewById(R.id.subline);
        subway = findViewById(R.id.subway);
        univ_min = findViewById(R.id.univ_min);
        subway_min = findViewById(R.id.subway_min);
        menuLayout = findViewById(R.id.menuLayout);
        priceLayout = findViewById(R.id.priceLayout);
        cafe_image_box = findViewById(R.id.cafe_image_box);
        cafe_name = findViewById(R.id.cafe_name);
        district = findViewById(R.id.district);

        Intent intent = getIntent();
        District = intent.getStringExtra("지역이름");
        district.setText("\"" + District+"\"");
        Cafe_Name = intent.getStringExtra("카페이름") ;   //수정해야함

        findViewById(R.id.readReview).setOnClickListener(onClickListener);
        findViewById(R.id.writeReview).setOnClickListener(onClickListener);

        cafe_name.setText(Cafe_Name);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection( intent.getStringExtra("지역이름") )
                .document(Cafe_Name)
                .collection("review")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                tot_star += Float.parseFloat(document.getData().get("numOfStar").toString());
                                tot_seekbar += Integer.parseInt(document.getData().get("seekBar_num").toString());
                                tot_fast += Integer.parseInt(document.getData().get("fast").toString());
                                tot_ordspeed += Integer.parseInt(document.getData().get("ordspeed").toString());
                                tot_slow += Integer.parseInt(document.getData().get("slow").toString());
                                tot_noisy += Integer.parseInt(document.getData().get("noisy").toString());
                                tot_ordnoise += Integer.parseInt(document.getData().get("ordnoise").toString());
                                tot_quiet += Integer.parseInt(document.getData().get("quiet").toString());
                                tot_review++;

                            }
                            if(tot_review != 0){
                                avg_star = tot_star/tot_review;
                                avg_seekbar = tot_seekbar/tot_review;

                                num_star.setText(String.valueOf(avg_star));
                                kindness.setText(String.valueOf(avg_seekbar));

                                info_fast.setText(String.valueOf(tot_fast));
                                info_ordspeed.setText(String.valueOf(tot_ordspeed));
                                info_slow.setText(String.valueOf(tot_slow));

                                info_noisy.setText(String.valueOf(tot_noisy));
                                info_ordnoise.setText(String.valueOf(tot_ordnoise));
                                info_quiet.setText(String.valueOf(tot_quiet));


                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        db.collection(intent.getStringExtra("지역이름"))
            .document(Cafe_Name)
            .collection("info")
            .document("d")
            .get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ViewGroup.LayoutParams layoutParams2 = new ViewGroup.LayoutParams(550, ViewGroup.LayoutParams.WRAP_CONTENT);

            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {

                    imageList = (ArrayList<String>) document.getData().get("picture_cafe");
                    String contents = imageList.get(0);
                    if (Patterns.WEB_URL.matcher(contents).matches()) {
                        ImageView imageView = new ImageView(CafeInfoActivity.this);
                        imageView.setLayoutParams(layoutParams);
                        cafe_image_box.addView(imageView);
                        Glide.with(CafeInfoActivity.this).load(contents).override(1000).thumbnail(0.1f).into(imageView);
                        Log.d("성공",contents);
                    }else{
                        Log.d("실패","실패다"+contents);
                    }


                    menuList = (ArrayList<String>) document.getData().get("menu");
                    Typeface typeface = ResourcesCompat.getFont(CafeInfoActivity.this, R.font.yangjin);

                    for(int i = 0; i < menuList.size() ; i++){
                        String contents1 = menuList.get(i);
                        if( (i%2 == 0) ){

                            TextView textView1 = new TextView(CafeInfoActivity.this);
                            textView1.setLayoutParams(layoutParams2);
                            textView1.setText(contents1);
                            textView1.setPadding(75,0,0,10);
                            textView1.setTypeface(typeface);
                            menuLayout.addView(textView1);
                        }else{

                            TextView textView2 = new TextView(CafeInfoActivity.this);
                            textView2.setLayoutParams(layoutParams2);
                            textView2.setText(contents1);
                            textView2.setPadding(75,0,0,10);
                            textView2.setTypeface(typeface);
                            priceLayout.addView(textView2);
                        }
                    }

                    wifi_ox.setText(document.getData().get("wifi").toString() );
                    time_open.setText( document.getData().get("time_open").toString() );
                    time_close.setText( document.getData().get("time_close").toString() );

                    near_Univ = (ArrayList<String>) document.getData().get("near_Univ");
                    university.setText( near_Univ.get(0) );
                    univ_min.setText( near_Univ.get(1) );

                    near_Subway = (ArrayList<String>) document.getData().get("near_Subway");
                    subline.setText( near_Subway.get(0) );
                    subway.setText( near_Subway.get(1) );
                    subway_min.setText( near_Subway.get(2) );

                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        }
    });

    }



    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.readReview:
                    Intent intent = new Intent(CafeInfoActivity.this, ShowReviewActivity.class);
                    intent.putExtra("카페이름 전달",Cafe_Name);
                    intent.putExtra("지역이름", District );
                    startActivity(intent);
                    break;
                case R.id.writeReview:
                    intent = new Intent(CafeInfoActivity.this, WriteReviewActivity.class);
                    intent.putExtra("카페이름 전달",Cafe_Name);
                    intent.putExtra("지역이름", District );
                    startActivity(intent);
                    break;
            }
        }
    };
}