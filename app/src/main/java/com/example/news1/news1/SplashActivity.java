package com.example.news1.news1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
private TextView tv;
private ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv = (TextView) findViewById(R.id.tvAppname);
        iv = (ImageView) findViewById(R.id.ivapplogo);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.transition);
        tv.startAnimation(animation);
        iv.startAnimation(animation);
        final Intent i = new Intent(this,PrefActivity.class);
        Thread timer = new Thread(){

            public void run(){
                try{
                    sleep(5000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }

            }
        };
        timer.start();
    }
}
