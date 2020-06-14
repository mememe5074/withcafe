package com.example.withcafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SelectDistrictActivity extends AppCompatActivity {
    private Button incheon, seoul, gyeonggi, chungnam,
            chungbuk, jeonbuk, jeonnam, gyeongbuk,
            gyeongnam, sejong, busan, gwangju,
            daegu, daejeon, gangwon, ulsan, jeju;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_district);

        incheon = findViewById(R.id.incheon); incheon.setOnClickListener(onClickListener);
        seoul = findViewById(R.id.seoul); seoul.setOnClickListener(onClickListener);
        gyeonggi = findViewById(R.id.gyeonggi); gyeonggi.setOnClickListener(onClickListener);
        chungnam = findViewById(R.id.chungnam); chungnam.setOnClickListener(onClickListener);
        chungbuk = findViewById(R.id.chungbuk); chungbuk.setOnClickListener(onClickListener);
        jeonbuk = findViewById(R.id.jeonbuk); jeonbuk.setOnClickListener(onClickListener);
        jeonnam = findViewById(R.id.jeonnam); jeonnam.setOnClickListener(onClickListener);
        gyeongbuk = findViewById(R.id.gyeongbuk); gyeongbuk.setOnClickListener(onClickListener);
        gyeongnam = findViewById(R.id.gyeongnam) ;gyeongnam.setOnClickListener(onClickListener);
        sejong = findViewById(R.id.sejong); sejong.setOnClickListener(onClickListener);
        busan = findViewById(R.id.busan); busan.setOnClickListener(onClickListener);
        gwangju = findViewById(R.id.gwangju); gwangju.setOnClickListener(onClickListener);
        daegu = findViewById(R.id.daegu); daegu.setOnClickListener(onClickListener);
        daejeon = findViewById(R.id.daejeon); daejeon.setOnClickListener(onClickListener);
        gangwon = findViewById(R.id.gangwon); gangwon.setOnClickListener(onClickListener);
        ulsan = findViewById(R.id.ulsan); ulsan.setOnClickListener(onClickListener);
        jeju = findViewById(R.id.jeju); jeju.setOnClickListener(onClickListener);



    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SelectDistrictActivity.this, SelectCafeActivity.class);
            switch (v.getId()){
                case R.id.incheon:
                    intent.putExtra("지역이름", "인천");
                    startActivity(intent);
                    break;
                case R.id.seoul:
                    intent.putExtra("지역이름", "서울");
                    startActivity(intent);
                    break;
                case R.id.gyeonggi:
                    intent.putExtra("지역이름", "경기");
                    startActivity(intent);
                    break;
                case R.id.chungnam:
                    intent.putExtra("지역이름", "충남");
                    startActivity(intent);
                    break;
                case R.id.chungbuk:
                    intent.putExtra("지역이름", "충북");
                    startActivity(intent);
                    break;
                case R.id.jeonbuk:
                    intent.putExtra("지역이름", "전북");
                    startActivity(intent);
                    break;
                case R.id.jeonnam:
                    intent.putExtra("지역이름", "전남");
                    startActivity(intent);
                    break;
                case R.id.gyeongbuk:
                    intent.putExtra("지역이름", "경북");
                    startActivity(intent);
                    break;
                case R.id.gyeongnam:
                    intent.putExtra("지역이름", "경남");
                    startActivity(intent);
                    break;
                case R.id.busan:
                    intent.putExtra("지역이름", "부산");
                    startActivity(intent);
                    break;
                case R.id.gwangju:
                    intent.putExtra("지역이름", "광주");
                    startActivity(intent);
                    break;
                case R.id.daegu:
                    intent.putExtra("지역이름", "대구");
                    startActivity(intent);
                    break;
                case R.id.daejeon:
                    intent.putExtra("지역이름", "대전");
                    startActivity(intent);
                    break;
                case R.id.gangwon:
                    intent.putExtra("지역이름", "강원");
                    startActivity(intent);
                    break;
                case R.id.ulsan:
                    intent.putExtra("지역이름", "울산");
                    startActivity(intent);
                    break;
                case R.id.jeju:
                    intent.putExtra("지역이름", "제주");
                    startActivity(intent);
                    break;
            }
        }
    };
}
