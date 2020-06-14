package com.example.withcafe;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView logo;
    private ImageView coffee_bag;
    private String mGlobalString;

    public String getGlobalString(){
        return mGlobalString;
    }

    public void setGlobalString(String globalString){
        this.mGlobalString = globalString;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logo = findViewById(R.id.logo);
        coffee_bag =  findViewById(R.id.coffee_bag);

        findViewById(R.id.logo).setOnClickListener(onClickListener);
        findViewById(R.id.coffee_bag).setOnClickListener(onClickListener);


        Animation animation = AnimationUtils.loadAnimation(this,R.anim.rotate);
        coffee_bag.startAnimation(animation);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.logo :
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(intent);

                case R.id.coffee_bag :
                    intent = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(intent);
            }
        }
        };
}
