package com.example.saicm.cookieclicker;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    ImageView cookie;
    TextView tvcookiecounter;
    AtomicInteger cookiecounter = new AtomicInteger(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ConstraintLayout layout = findViewById(R.id.layout);

        final ImageView cookie = findViewById(R.id.cookie);
        final TextView tvcookiecounter = findViewById(R.id.cookiecounter);

        final ScaleAnimation cookieanimation = new ScaleAnimation(0.8f, 1.f, 0.8f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        cookieanimation.setDuration(200);

        final TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 50, -2000);
        translateAnimation.setDuration(3000);

        PassiveIncome income = new PassiveIncome();
        income.start();

        cookie.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    cookiecounter.getAndAdd(1);
                    tvcookiecounter.setText(cookiecounter + " Cookies");
                    TextView pluscookies = new TextView(MainActivity.this);
                    pluscookies.setId(View.generateViewId());
                    ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    pluscookies.setLayoutParams(lp);
                    pluscookies.setText("+1");
                    pluscookies.setTextSize(18);
                    pluscookies.setX(event.getX()+200+(int)(Math.random()*200)-100);
                    pluscookies.setY(event.getY());
                    cookie.startAnimation(cookieanimation);
                    layout.addView(pluscookies);

                    Animation fadeIn;
                    Animation fadeOut;

                    AnimationSet plusOneAnim = new AnimationSet(true);
                    final TranslateAnimation translateAnimation = new TranslateAnimation(0,0,50,-2000);
                    translateAnimation.setDuration(3000);
                    fadeIn = new AlphaAnimation(0,1);
                    fadeIn.setInterpolator(new DecelerateInterpolator());
                    fadeIn.setDuration(500);
                    fadeOut = new AlphaAnimation(1,0);
                    fadeOut.setInterpolator(new AccelerateInterpolator());
                    fadeOut.setDuration(1500);
                    plusOneAnim.addAnimation(translateAnimation);
                    plusOneAnim.addAnimation(fadeOut);
                    pluscookies.startAnimation(plusOneAnim);
                    pluscookies.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        });
    }

    public class PassiveIncome extends Thread{
        @Override
        public void run() {
            while(true) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
