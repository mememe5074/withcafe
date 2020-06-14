package com.example.withcafe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public class ReadReviewActivity extends AppCompatActivity {
    public static Context mContext;

    private String TAG = "ReadReviewActivity";
    private String District;
    private String Cafe_Name;
    private String Title;

    private EditText reply_contents;
    private TextView district;
    private Button reply_upload;
    private TextView title;
    private RecyclerView reply_list;
    private LinearLayout contentsLayout;
    private ArrayList<String> image;

    private TextView user;
    private TextView date;
    private TextView num_of_star;


    private ReplyInfo replyInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_revivew);

        Cafe_Name = getIntent().getStringExtra("카페이름 전달");
        mContext = this;
        Intent intent =getIntent();
        District = intent.getStringExtra("지역이름");

        final TextView cafe_name = findViewById(R.id.cafe_name);
        cafe_name.setText(Cafe_Name);

        reply_upload = findViewById(R.id.reply_upload);
        reply_upload.setOnClickListener(onClickListener);
        reply_contents = findViewById(R.id.reply_contents);

        title = findViewById(R.id.title);
        user = findViewById(R.id.user);
        num_of_star = findViewById(R.id.num_of_star);
        date = findViewById(R.id.date);
        contentsLayout = findViewById(R.id.contentsLayout);

        reply_list = findViewById(R.id.reply_list);

        final SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy.MM.dd hh:mm", Locale.KOREA);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(District)
                .document(Cafe_Name)
                .collection("review")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<ReviewInfo> reviewList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                reviewList.add(new ReviewInfo(
                                        document.getData().get("cafe_name").toString(),
                                        document.getData().get("title").toString() ,
                                        (ArrayList<String>) document.getData().get("contents"),
                                        document.getData().get("user").toString() ,
                                        new Date(document.getDate("createdAt").getTime()),
                                        document.getData().get("numOfStar").toString() ,
                                        document.getData().get("seekBar_num").toString()));

                                title.setText( document.getData().get("title").toString() );
                                String getTime1 = simpleDate.format(new Date(document.getDate("createdAt").getTime()));
                                simpleDate.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
                                date.setText( getTime1);
                                user.setText( document.getData().get("user").toString() );
                                num_of_star.setText( document.getData().get("numOfStar").toString() );
                                image = (ArrayList<String>) document.getData().get("contents");

                                if (Patterns.WEB_URL.matcher(image.get(0)).matches()) {
                                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    ImageView imageView = new ImageView(ReadReviewActivity.this);
                                    imageView.setLayoutParams(layoutParams);
                                    contentsLayout.addView(imageView);
                                    Glide.with(ReadReviewActivity.this).load(image.get(0)).override(1000).thumbnail(0.1f).into(imageView);
                                    Log.d("성공",image.get(0));
                                }else{
                                    Log.d("실패","실패다"+image.get(0));
                                }

                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        Title = intent.getStringExtra("리뷰제목");

        db.collection(District)
                .document(Cafe_Name)
                .collection("review")
                .document(Title+"user")
                .collection("reply")
                .orderBy("createdAt", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<ReplyInfo> replyList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                replyList.add(new ReplyInfo(
                                        document.getData().get("reply_contents").toString(),
                                        document.getData().get("user").toString(),
                                        new Date(document.getDate("createdAt").getTime())
                                        ));
                            }

                            reply_list.setHasFixedSize(true);
                            reply_list.setLayoutManager(new LinearLayoutManager(ReadReviewActivity.this));
                            RecyclerView.Adapter mAdapter = new ReadReviewAdapter(ReadReviewActivity.this, replyList);
                            reply_list.setAdapter(mAdapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.reply_upload:
                    WriteReply(
                            reply_contents.getText().toString(),
                            District,
                            Cafe_Name,
                            title.getText().toString(),
                            "user");
                    reply_contents.setText(" ");
                    reply_contents.setHint("댓글쓰기");
                    Update();
                    break;
            }
        }
    };

    void Update(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(District)
                .document(Cafe_Name)
                .collection("review")
                .document(Title+"user")
                .collection("reply")
                .orderBy("createdAt", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<ReplyInfo> replyList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                replyList.add(new ReplyInfo(
                                        document.getData().get("reply_contents").toString(),
                                        document.getData().get("user").toString(),
                                        new Date(document.getDate("createdAt").getTime())
                                ));
                            }

                            reply_list.setHasFixedSize(true);
                            reply_list.setLayoutManager(new LinearLayoutManager(ReadReviewActivity.this));
                            RecyclerView.Adapter mAdapter = new ReadReviewAdapter(ReadReviewActivity.this, replyList);
                            reply_list.setAdapter(mAdapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void WriteReply(String reply_contents,String district, String cafe_name, String title, String user){

        final Date date = replyInfo == null ? new Date() : replyInfo.getCreatedAt();

        replyInfo = new ReplyInfo(reply_contents, user, date);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        Random random = new Random();
        final DocumentReference documentReference = firebaseFirestore
                .collection(district)
                .document(cafe_name)
                .collection("review")
                .document(title+"user") //수정
                .collection("reply")
                .document(user+ random.nextInt());

        storageUpload(documentReference, replyInfo);
    }

    public void storageUpload(DocumentReference documentReference, ReplyInfo replyInfo) {

        documentReference
                .set(replyInfo.getReplyInfo())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("ReadReviewActivity", "성공");
                        //댓글창 update!
                        Toast.makeText(ReadReviewActivity.this, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("ReadReviewActivity", "실패2");
                    }
                });
    }

}
