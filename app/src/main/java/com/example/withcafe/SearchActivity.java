package com.example.withcafe;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


public class SearchActivity extends AppCompatActivity {

    private ImageView subway_btn;
    private ImageView school_btn;
    private ImageView select_district;
    private ImageView write_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        select_district =  findViewById(R.id.select_district);
        write_btn =  findViewById(R.id.write_btn);
        findViewById(R.id.select_district).setOnClickListener(onClickListener);
        findViewById(R.id.write_btn).setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){

                case R.id.select_district:
                    Intent intent = new Intent(SearchActivity.this, SelectDistrictActivity.class);
                    startActivity(intent);
                    break;
                case R.id.write_btn :
                    intent = new Intent(SearchActivity.this, WriteCafeActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };
}
