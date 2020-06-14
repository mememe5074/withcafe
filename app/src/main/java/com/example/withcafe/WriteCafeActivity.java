package com.example.withcafe;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firestore.v1.Write;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class WriteCafeActivity extends AppCompatActivity {

    private CafeInfo cafeInfo;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageRef;
    private int pathCount;
    private int successCount;
    private String TAG = this.toString();
    private ArrayList<String> pathList = new ArrayList<>();
    private ImageView selectedImageVIew;
    private ImageView univSearch;
    private ImageView subwaySearch;
    private ImageView sublineSearch;

    private ImageView upload;
    private ImageView gallery;
    private ImageView cafe_picture;
    private Button add_menu;
    private Button delete_menu;

    private TextView search_univ;
    private TextView search_subline;
    private TextView search_subway;

    private EditText selectedEditText;
    private EditText contentsEditText;
    private EditText univ_min;
    private EditText subway_min;


    private LinearLayout parent;
    private LinearLayout cafe_pic_box;
    private int id;
    private Spinner spinner;
    public String wifi;
    private String district;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_cafe);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // do your stuff
        } else {
            signInAnonymously();
        }


        spinner = findViewById(R.id.spinner);
        univ_min = findViewById(R.id.univ_min);
        subway_min = findViewById(R.id.subway_min);
        univSearch = (ImageView)findViewById(R.id.univSearch);
        sublineSearch = (ImageView)findViewById(R.id.sublineSearch);
        upload = (ImageView)findViewById(R.id.upload);
        gallery = (ImageView)findViewById(R.id.gallery);
        add_menu = (Button)findViewById(R.id.add_menu);
        delete_menu = (Button)findViewById(R.id.delete_menu);
        parent = (LinearLayout) findViewById(R.id.menuLayout);
        cafe_pic_box = findViewById(R.id.cafe_pic_box);
        search_univ = findViewById(R.id.search_univ);
        search_subline = findViewById(R.id.search_subline);
        search_subway = findViewById(R.id.search_subway);

        upload.setOnClickListener(onClickListener);
        gallery.setOnClickListener(onClickListener);
        delete_menu.setOnClickListener(onClickListener);
        add_menu.setOnClickListener(onClickListener);
        univSearch.setOnClickListener(onClickListener);
        sublineSearch.setOnClickListener(onClickListener);
        search_univ.setOnClickListener(onClickListener);
        search_subline.setOnClickListener(onClickListener);


        final CheckBox wifi_O = (CheckBox)findViewById(R.id.wifi_O);
        final CheckBox wifi_X = (CheckBox)findViewById(R.id.wifi_X);
        wifi = "O";
        findViewById(R.id.wifi_O).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                wifi_O.setChecked(true);
                wifi_X.setChecked(false);
                wifi = "O";
            }});

        findViewById(R.id.wifi_X).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                wifi_X.setChecked(true);
                wifi_O.setChecked(false);
                wifi = "X";
            }});

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }

        Intent intent = getIntent();
        Intent intent2 = getIntent();


        if(intent2.getExtras() != null) {
            search_subway.setText(intent.getExtras().getString("지하철 검색"));
            search_subline.setText(intent.getExtras().getString("노선 검색"));
        }

        final Spinner spinner_field = (Spinner) findViewById(R.id.spinner);
        //1번에서 생성한 field.xml의 item을 String 배열로 가져오기
        String[] str = getResources().getStringArray(R.array.district_array);
        //2번에서 생성한 spinner_item.xml과 str을 인자로 어댑터 생성.
        final ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, R.layout.district_spinner_item,str);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_field.setAdapter(adapter);
        //spinner 이벤트 리스너
        spinner_field.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinner_field.getSelectedItemPosition() > 0){
                    //선택된 항목
                    district = spinner_field.getSelectedItem().toString();
                    Log.v("알림",spinner_field.getSelectedItem().toString()+ "is selected");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });



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


    View.OnClickListener onClickListener = new View.OnClickListener() {
        String image_path;

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.upload:
                    storageUpdate();
                    break;
                case R.id.gallery:
                        cafe_pic_box.removeAllViewsInLayout();
                        Intent intent = new Intent(WriteCafeActivity.this, GalleryActivity.class);
                        startActivityForResult(intent, 0);

                    break;
                case R.id.add_menu:
                    Typeface typeface = ResourcesCompat.getFont(WriteCafeActivity.this, R.font.yangjin);
                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    ViewGroup.LayoutParams layoutParams2 = new ViewGroup.LayoutParams(550, ViewGroup.LayoutParams.WRAP_CONTENT);

                    LinearLayout linearLayout = new LinearLayout(WriteCafeActivity.this);
                    linearLayout.setLayoutParams(layoutParams);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    parent.addView(linearLayout);

                    EditText menu_text = new EditText(WriteCafeActivity.this);
                    menu_text.setLayoutParams(layoutParams2);
                    menu_text.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                    menu_text.setHint("ex) 아이스아메리카노");
                    menu_text.setTypeface(typeface);
                    menu_text.setTextSize(18);
                    linearLayout.addView(menu_text);

                    EditText price_text = new EditText(WriteCafeActivity.this);
                    price_text.setLayoutParams(layoutParams2);
                    price_text.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                    price_text.setHint("4,500");
                    menu_text.setTextSize(18);
                    price_text.setTypeface(typeface);
                    linearLayout.addView(price_text);
                    id++;

                    break;

                case R.id.delete_menu:
                    if(0 < id) {
                        parent.removeViewAt(id);
                        id--;
                    }else
                        Toast.makeText(WriteCafeActivity.this, "삭제할 메뉴가 없습니다.", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.univSearch:
                    Intent intent_serach1 = new Intent(WriteCafeActivity.this, UnivSearchActivity.class);
                    startActivity(intent_serach1);
                    search_univ.setText("적용하기");

                    break;

                case R.id.sublineSearch:
                    Intent intent_serach2 = new Intent(WriteCafeActivity.this, SublineSearchActivity.class);
                    search_subline.setText("적용하기");
                    startActivity(intent_serach2);
                    break;
                case R.id.search_univ:
                    if(((Global_Variable) getApplication()).getGb_UnivName() != null) {
                        search_univ.setText(((Global_Variable) getApplication()).getGb_UnivName());
                    }
                    break;
                case R.id.search_subline:
                    if(((Global_Variable) getApplication()).getGb_UnivName() != null) {
                        search_subline.setText(((Global_Variable) getApplication()).getGb_SublineName());
                        search_subway.setText(((Global_Variable) getApplication()).getGb_SubwayName());
                    }
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    String path = data.getStringExtra("path");
                    pathList.add(path);

                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    ImageView imageView = new ImageView(WriteCafeActivity.this);
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
                    cafe_pic_box.addView(imageView);
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

    public static boolean isStorageUrl(String url) {
        return Patterns.WEB_URL.matcher(url).matches() && url.contains("https://firebasestorage.googleapis.com/v0/b/with-cafe-0415.appspot.com/o/posts/d");
    }

    public void storageUpdate() {
        final String cafe_name = ((EditText) findViewById(R.id.cafe_name)).getText().toString();
        final String time_close = ((TextView) findViewById(R.id.time_close)).getText().toString();
        final String time_open = ((TextView) findViewById(R.id.time_open)).getText().toString();
        final ArrayList<String> menuList = new ArrayList<>();
        final ArrayList<String> picture_cafe = new ArrayList<>();
        final ArrayList<String> near_Univ = new ArrayList<>();
        final ArrayList<String> near_Subway = new ArrayList<>();

        if(search_univ.getText().toString() != "대학교 검색"){
            near_Univ.add(search_univ.getText().toString());
        }else{
            near_Univ.add("");
        }

        near_Univ.add(univ_min.getText().toString());

        if(search_subline.getText().toString() != "지하철 검색"){
            near_Subway.add(search_subline.getText().toString());
        }else{
            near_Univ.add("");
        }
        near_Subway.add(search_subway.getText().toString());
        near_Subway.add(subway_min.getText().toString());

        if(cafe_name.length()>0 && district != null && cafe_pic_box.getChildAt(0) != null ) {

            parent.getChildAt(0).findViewById(R.id.menuLayout);

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

            final DocumentReference documentReference;
            documentReference =
                    firebaseFirestore
                    .collection(district)
                    .document(cafe_name)
                    .collection("info")
                    .document("d");


            for(int i = 0 ; i < parent.getChildCount(); i++){
                LinearLayout linearLayout = (LinearLayout)parent.getChildAt(i);

                for(int ii = 0 ; ii < linearLayout.getChildCount() ; ii++){
                    View view = linearLayout.getChildAt(ii);

                    if(view instanceof EditText){
                        String text = ((EditText)view).getText().toString();
                        if (text.length() > 0)
                            menuList.add(text);
                    }
                    if(!isStorageUrl(pathList.get(pathCount)) && picture_cafe.isEmpty()) {
                        picture_cafe.add(pathList.get(pathCount));
                        successCount++;
                        String[] pathArray = pathList.get(pathCount).split("\\.");

                        Log.d("로그2",pathList.toString());

                        final StorageReference mountainImagesRef = storageRef.child("posts/" + documentReference.getId() + "/" + (pathCount) + "." + pathArray[pathArray.length - 1]);

                        try {
                            InputStream stream = new FileInputStream(new File(pathList.get(pathCount)));
                            StorageMetadata metadata = new StorageMetadata.Builder()
                                    .setCustomMetadata("index", ""+ (menuList.size()-1))
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
                                            picture_cafe.set(index+1, uri.toString());    //contentsList에 index와 uri주소가 들어가게 됨

                                            if(successCount == 0){
                                                CafeInfo cafeInfo = new CafeInfo(
                                                        cafe_name,
                                                        menuList,
                                                        picture_cafe,
                                                        wifi,
                                                        time_open,
                                                        time_close,
                                                        district,
                                                        near_Univ,
                                                        near_Subway
                                                );
                                                storageUpload(documentReference,cafeInfo);

                                            }
                                        }
                                    });
                                }
                            });
                        }
                        catch (FileNotFoundException e){
                            Log.e("로그","에러" + e.toString());
                        }
                    }
                }
            }
        } else {
            Toast.makeText(this, "제목(혹은 내용, 위치)을 입력해주세요.", Toast.LENGTH_SHORT).show();
        }

    }

    public void storageUpload(DocumentReference documentReference, CafeInfo cafeInfo) {

        final String cafe_name = ((EditText) findViewById(R.id.cafe_name)).getText().toString();

        documentReference
                .set(cafeInfo.getCafeInfo())
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
