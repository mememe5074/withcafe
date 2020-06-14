package com.example.withcafe;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import static java.lang.Integer.parseInt;

public class WriteReviewActivity extends AppCompatActivity{

    private ReviewInfo reviewInfo;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageRef;
    private int pathCount;
    private int successCount;
    private String TAG = this.toString();
    private String District;
    private ArrayList<String> pathList = new ArrayList<>();
    private LinearLayout parent;
    private ImageView selectedImageVIew;
    private EditText selectedEditText;
    private EditText contentsEditText;
    private TextView cafe_name;
    private TextView district;

    public int fast=0, ordspeed=1, slow=0 ;
    public int noisy=0, ordnoise=1, quiet=0 ;

    RatingBar ratingBar;
    TextView numOfStar;
    SeekBar seekbar;
    TextView outcome;
    ImageView upload;
    ImageView camera;

    public int number_seekbar = 0;
    public int number_rating = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // do your stuff
        } else {
            signInAnonymously();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }


        district = findViewById(R.id.district);
        parent = (LinearLayout)findViewById(R.id.contentsLayout);
        numOfStar = (TextView)findViewById(R.id.numOfstar);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        seekbar = (SeekBar)findViewById(R.id.seekBar);
        outcome = (TextView)findViewById(R.id.seekBar_num);
        upload = (ImageView)findViewById(R.id.upload);
        camera = (ImageView)findViewById(R.id.camera);
        camera = (ImageView)findViewById(R.id.camera);
        cafe_name = findViewById(R.id.cafe_name);

        Intent intent = getIntent();
        cafe_name.setText( intent.getStringExtra("카페이름 전달"));
        District = intent.getStringExtra("지역이름");
        district.setText("\"" +District+ "\"");

        contentsEditText = findViewById(R.id.contentsEditText);

        findViewById(R.id.upload).setOnClickListener(onClickListener);
        findViewById(R.id.camera).setOnClickListener(onClickListener);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                numOfStar.setText(""+rating);
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                number_seekbar = seekbar.getProgress();
                update_seekbar();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                number_seekbar = seekbar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                number_seekbar = seekbar.getProgress();
            }
        });

        final CheckBox ch_fast = (CheckBox)findViewById(R.id.ch_fast);
        final CheckBox ch_ordspeed = (CheckBox)findViewById(R.id.ch_ordspeed);
        final CheckBox ch_slow = (CheckBox)findViewById(R.id.ch_slow);
        final CheckBox ch_noisy = (CheckBox)findViewById(R.id.ch_noisy);
        final CheckBox ch_ordnoise = (CheckBox)findViewById(R.id.ch_ordnoise);
        final CheckBox ch_quiet = (CheckBox)findViewById(R.id.ch_quiet);

        findViewById(R.id.ch_fast).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                    ch_ordspeed.setChecked(false);
                    ch_slow.setChecked(false);
                    fast = 1; ordspeed=0; slow=0;
            }});
        findViewById(R.id.ch_ordspeed).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                ch_fast.setChecked(false);
                ch_slow.setChecked(false);
                fast = 0; ordspeed=1; slow=0;

            }});
        findViewById(R.id.ch_slow).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                ch_fast.setChecked(false);
                ch_ordspeed.setChecked(false);
                fast = 0; ordspeed=0; slow=1;

            }});
        findViewById(R.id.ch_noisy).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                ch_ordnoise.setChecked(false);
                ch_quiet.setChecked(false);
                noisy = 1; ordnoise=0; quiet=0;
            }});
        findViewById(R.id.ch_ordnoise).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                ch_quiet.setChecked(false);
                ch_noisy.setChecked(false);
                noisy = 1; ordnoise=0; quiet=0;
            }});
        findViewById(R.id.ch_quiet).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                ch_ordnoise.setChecked(false);
                ch_noisy.setChecked(false);
                noisy = 1; ordnoise=0; quiet=0;
            }});

    }


    private void signInAnonymously(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        }) .addOnFailureListener(this, new OnFailureListener() {
            @Override public void onFailure(@NonNull Exception exception) {
                Log.e("TAG", "signInAnonymously:FAILURE", exception);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    String path = data.getStringExtra("path");
                    pathList.add(path);

                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    LinearLayout linearLayout = new LinearLayout(WriteReviewActivity.this);
                    linearLayout.setLayoutParams(layoutParams);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);

                    if(selectedEditText == null){
                        parent.addView(linearLayout);
                    }else{
                        for(int i = 0 ; i < parent.getChildCount() ; i++){
                            parent.addView(linearLayout, i+1);
                            break;
                        }
                    }
                    ImageView imageView = new ImageView(WriteReviewActivity.this);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setAdjustViewBounds(true);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectedImageVIew = (ImageView) v;
                        }
                    });
                    Glide.with(this).load(path).override(1000).into(imageView);
                    linearLayout.addView(imageView);

                    EditText editText = new EditText(WriteReviewActivity.this);
                    editText.setLayoutParams(layoutParams);
                    editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                    editText.setHint("내용을 입력해주세요");
                    linearLayout.addView(editText);
                }
                break;
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    String path = data.getStringExtra("path");
                    pathList.set(parent.indexOfChild((View) selectedImageVIew.getParent()) - 1, path);
                    Glide.with(this).load(path).override(1000).into(selectedImageVIew);
                }
                break;
        }
    }

    public void update_seekbar(){
        outcome.setText(new StringBuilder().append(number_seekbar));
        //seekbar의 값에 따라 textview로 보여주고 화면을 바꾸기 위해 정의한 함수
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.upload:
                    storageUpdate();
                    break;
                case R.id.camera:
                    Intent intent = new Intent(WriteReviewActivity.this, GalleryActivity.class);
                    startActivityForResult(intent, 0);
                    break;
            }
        }
    };

    public static boolean isStorageUrl(String url) {
        return Patterns.WEB_URL.matcher(url).matches() && url.contains("https://firebasestorage.googleapis.com/v0/b/with-cafe-0415.appspot.com/o/review");
    }

    public void storageUpdate() {
        final String title = ((EditText) findViewById(R.id.review_title_w)).getText().toString();
        final String seekbar_num = ((TextView) findViewById(R.id.seekBar_num)).getText().toString();
        final String numOfstar = ((TextView) findViewById(R.id.numOfstar)).getText().toString();
        final ArrayList<String> contentsList = new ArrayList<>();
        final Date date = reviewInfo == null ? new Date() : reviewInfo.getCreatedAt();
        final String Cafe_Name;
        Cafe_Name = getIntent().getStringExtra("카페이름 전달");

        if(title.length()>0) {

            parent.getChildAt(0).findViewById(R.id.contentsEditText);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

            Random random = new Random();
            final DocumentReference documentReference = reviewInfo
                    == null ? firebaseFirestore
                    .collection(District)
                    .document(Cafe_Name)
                    .collection("review")
                    .document(title + "user") //수정

                    : firebaseFirestore.collection(Cafe_Name).document("review");

            for(int i = 0 ; i < parent.getChildCount(); i++){
                LinearLayout linearLayout = (LinearLayout)parent.getChildAt(i);
                for(int ii = 0 ; ii < linearLayout.getChildCount() ; ii++){
                    View view = linearLayout.getChildAt(ii);
                    if(view instanceof EditText){
                        String text = ((EditText)view).getText().toString();
                        if (text.length() > 0) {
                            contentsList.add(text);
                        }
                    }else if(!isStorageUrl(pathList.get(pathCount))) {

                        contentsList.add(pathList.get(pathCount));
                        successCount++;
                        String[] pathArray = pathList.get(pathCount).split("\\.");
                        final StorageReference mountainImagesRef = storageRef.child("posts/" + documentReference.getId() + "/" + pathCount + "." + pathArray[pathArray.length - 1]);

                        try {
                            InputStream stream = new FileInputStream(new File(pathList.get(pathCount)));
                            StorageMetadata metadata = new StorageMetadata.Builder()
                                    .setCustomMetadata("index", ""+ (contentsList.size()-1))
                                    .build();
                            UploadTask uploadTask = mountainImagesRef.putStream(stream, metadata);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    final int index = Integer.parseInt(taskSnapshot.getMetadata().getCustomMetadata("index"));
                                    mountainImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() { //이미지 uri를 받기위해 사용
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            successCount--;
                                            contentsList.set(index, uri.toString());    //contentsList에 index와 uri주소가 들어가게 됨

                                            ReviewInfo reviewInfo = new ReviewInfo(Cafe_Name, title, contentsList, "user", date, numOfstar, seekbar_num,
                                                    String.valueOf(fast), String.valueOf(ordspeed), String.valueOf(slow),
                                                    String.valueOf(noisy), String.valueOf(ordnoise), String.valueOf(quiet));
                                            storageUpload(documentReference, reviewInfo);
                                        }
                                    });
                                }
                            });

                        }
                        catch (FileNotFoundException e){
                            Log.e("로그","에러" + e.toString());
                        }
                        pathCount++;
                    }
                }
            }
            }
    }

    public void storageUpload(DocumentReference documentReference, ReviewInfo reviewInfo) {

        final String Cafe_Name;
        Cafe_Name = getIntent().getStringExtra("카페이름 전달");

        Log.d("로그",Cafe_Name);
        Random random = new Random();
        final String title = ((EditText) findViewById(R.id.review_title_w)).getText().toString();

        documentReference
                .set(reviewInfo.getReviewInfo())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "성공");
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "실패2");
                    }
                });
    }

}