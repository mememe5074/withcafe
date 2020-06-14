package com.example.withcafe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SelectCafeActivity extends AppCompatActivity {

    private String TAG = "SelectCafeActivity";
    private String district;
    private TextView district_name;
    private RecyclerView cafe_list;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_cafe);

        Intent intent = getIntent();
        district = intent.getStringExtra("지역이름");



        cafe_list = findViewById(R.id.cafe_list);
        district_name = findViewById(R.id.district_name);
        district_name.setText(" \" "+district+ " \" ");


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collectionGroup("info")
                .whereEqualTo("district",district)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()) {
                           final ArrayList<CafeInfo> cafeList = new ArrayList<>(); //이유를 모르겠음
                           for (QueryDocumentSnapshot document : task.getResult()) {
                               cafeList.add(new CafeInfo(
                                       document.getData().get("cafe_name").toString(),
                                       (ArrayList<String>)document.getData().get("picture_cafe"),
                                       (ArrayList<String>)document.getData().get("menu"),
                                       document.getData().get("wifi").toString(),
                                       document.getData().get("time_open").toString(),
                                       document.getData().get("time_close").toString(),
                                       document.getData().get("district").toString(),
                                       (ArrayList<String>)document.getData().get("near_Univ"),
                                       (ArrayList<String>)document.getData().get("near_Subway")));

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

                                           Intent intent = new Intent(SelectCafeActivity.this, CafeInfoActivity.class);
                                           intent.putExtra("카페이름", cafeList.get(currentPosition).getCafe_name() );
                                           intent.putExtra("지역이름", district );
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
                           RecyclerView cafe_list = (RecyclerView)findViewById(R.id.cafe_list);
                           cafe_list.setHasFixedSize(true);
                           cafe_list.setLayoutManager(new LinearLayoutManager(SelectCafeActivity.this));
                           RecyclerView.Adapter mAdapter = new SelectCafeAdapter(SelectCafeActivity.this,cafeList);
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