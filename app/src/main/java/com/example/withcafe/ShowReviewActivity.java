package com.example.withcafe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class ShowReviewActivity extends AppCompatActivity {

    private String TAG = "SelectCafeActivity";
    private String district;
    private String cafe_name;
    private String title;
    private TextView district_name;
    private TextView Cafe_Name;
    private RecyclerView cafe_list;
    private GestureDetector gestureDetector;
    private ReviewInfo reviewInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_reivew);

        Intent intent = getIntent();
        district = intent.getStringExtra("지역이름");
        cafe_name = intent.getStringExtra("카페이름 전달");

        cafe_list = findViewById(R.id.recyclerview_review_list);

        Cafe_Name = findViewById(R.id.cafe_name);
        Cafe_Name.setText(cafe_name);

        district_name = findViewById(R.id.district_name);
        district_name.setText(" \" "+district+ " \" ");


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final Date date = reviewInfo == null ? new Date() : reviewInfo.getCreatedAt();

        db.collection(district)
                .document(cafe_name)
                .collection("review")
                .whereEqualTo("cafe_name",cafe_name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            final ArrayList<ReviewInfo> reviewList = new ArrayList<>(); //이유를 모르겠음
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                reviewList.add(new ReviewInfo(
                                        document.getData().get("cafe_name").toString(),
                                        document.getData().get("title").toString(),
                                        document.getData().get("user").toString(),
                                        date,
                                        document.getData().get("numOfStar").toString()
                                        ));

                                title = document.getData().get("title").toString();
                                RecyclerView.OnItemTouchListener onItemTouchListener = new RecyclerView.OnItemTouchListener() {
                                    @Override
                                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                                        //손으로 터치한 곳의 좌표를 토대로 해당 Item의 View를 가져옴
                                        View childView = rv.findChildViewUnder(e.getX(),e.getY());

                                        //터치한 곳의 View가 RecyclerView 안의 아이템이고 그 아이템의 View가 null이 아니라
                                        //정확한 Item의 View를 가져왔고, gestureDetector에서 한번만 누르면 true를 넘기게 구현했으니
                                        //한번만 눌려서 그 값이 true가 넘어왔다면
                                        if(childView != null && gestureDetector.onTouchEvent(e)){

                                            //현재 터치된 곳의 position을 가져오고
                                            int currentPosition = rv.getChildAdapterPosition(childView);

                                            Intent intent = new Intent(ShowReviewActivity.this, ReadReviewActivity.class);
                                            intent.putExtra("카페이름 전달", cafe_name );
                                            intent.putExtra("지역이름", district );
                                            intent.putExtra("리뷰제목", title );
                                            startActivity(intent);

                                            //해당 위치의 Data를 가져옴
                                            return true;
                                        }
                                        return false;
                                    }

                                    @Override
                                    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                                    }

                                    @Override
                                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                                    }

                                };
                                cafe_list.addOnItemTouchListener(onItemTouchListener);
                            }
                            cafe_list.setHasFixedSize(true);
                            cafe_list.setLayoutManager(new LinearLayoutManager(ShowReviewActivity.this));
                            RecyclerView.Adapter mAdapter = new ShowReviewAdapter(ShowReviewActivity.this, reviewList, cafe_name, district);
                            cafe_list.setAdapter(mAdapter);
                        }else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        gestureDetector = new GestureDetector(getApplicationContext(),new GestureDetector.SimpleOnGestureListener() {

            //누르고 뗄 때 한번만 인식하도록 하기위해서
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });



    }

}
