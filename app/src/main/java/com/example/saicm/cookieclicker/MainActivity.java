package com.example.saicm.cookieclicker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.saicm.cookieclicker.Menu.backgroundUpdate;

public class MainActivity extends AppCompatActivity {

    ImageView cookie;
    TextView tvcookiecounter;
    Button menubutton;
    static AtomicInteger cookiecounter;
    SharedPreferences sharedPreferences;

    ArrayList<Upgrade> list;

    public String COOKIECOUNTERKEY = "COOKIECOUNTERKEY";

    @Override
    protected void onStop() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(COOKIECOUNTERKEY, cookiecounter.get());
        list = Menu.list;


        editor.commit();
        super.onStop();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        cookiecounter = new AtomicInteger(sharedPreferences.getInt(COOKIECOUNTERKEY,0));

        final ConstraintLayout layout = findViewById(R.id.layout);

        final ImageView cookie = findViewById(R.id.cookie);
        final TextView tvcookiecounter = findViewById(R.id.cookiecounter);
        menubutton = findViewById(R.id.button);
        tvcookiecounter.setText(cookiecounter + " Cookies");
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
                    cookiecounter.getAndAdd(10);
                    tvcookiecounter.setText(cookiecounter + " Cookies");
                    TextView pluscookies = new TextView(MainActivity.this);
                    pluscookies.setId(View.generateViewId());
                    ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    pluscookies.setLayoutParams(lp);
                    pluscookies.setText("+1");
                    pluscookies.setTypeface(Typeface.DEFAULT_BOLD);
                    pluscookies.setTextSize(18);
                    pluscookies.setTextColor(Color.WHITE);
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

        menubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Menu.class);
                intent.putExtra(COOKIECOUNTERKEY, cookiecounter);
                startActivity(intent);

            }
        });



    }

    public void addPassiveIncome(int add) {
        tvcookiecounter = findViewById(R.id.cookiecounter);

        cookiecounter.getAndAdd(add);
        tvcookiecounter.setText(cookiecounter + " Cookies");
    }

    public class PassiveIncome extends Thread{
        @Override
        public void run() {
            while(true) {
                list = Menu.list;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (list != null && list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                addPassiveIncome(list.get(i).getCounter() * list.get(i).getCookiesPerSecond());
//                                for (int j = 0; j < list.get(i).getCounter(); j++) {
//                                    Add images from upgrade counter
//                                }
                            }
                        }
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

    public static class Upgrade {

        String name;
        int cookiespersecond;
        int price;
        int counter;
        int image;


        public Upgrade(String name, int cookiespersecond, int price, int counter, int image) {
            this.name = name;
            this.cookiespersecond = cookiespersecond;
            this.price = price;
            this.counter = counter;
            this.image = image;
        }

        public int getPrice() {
            return price;
        }

        public int getCookiesPerSecond() {
            return cookiespersecond;
        }

        public String getName() {
            return name;
        }

        public int getCounter() { return counter;}

        public int getImage() { return image; }

        public void addCounter() {
            counter++;
        }

        public void setCounter(int counter) {
            this.counter = counter;
        }

    }



}