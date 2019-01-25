package com.example.saicm.cookieclicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.saicm.cookieclicker.MainActivity.cookiecounter;

public class Menu extends AppCompatActivity {

    static ArrayList<MainActivity.Upgrade> list = new ArrayList<>();
    ListView listView;
    CustomAdapter adapter;
    static BackgroundUpdate backgroundUpdate;
    SharedPreferences sharedPreferences;

    public String AUTOCLICKCOUNTER = "AUTOCLICKCOUNTER";
    public String GRANDMACOUNTER = "GRANDMACOUNTER";
    public String CROBOTCOUNTER = "CROBOTCOUNTER";
    public String COOKIEFARMCOUNTER = "COOKIEFARMCOUNTER";
    public String CFACTORYCOUNTER = "CFACTORYCOUNTER";
    public String SFACTORYCOUNTER = "SFACTORYCOUNTER";

    @Override
    protected void onStop() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(AUTOCLICKCOUNTER, list.get(0).getCounter());
        editor.putInt(GRANDMACOUNTER, list.get(1).getCounter());
        editor.putInt(CROBOTCOUNTER, list.get(2).getCounter());
        editor.putInt(COOKIEFARMCOUNTER, list.get(3).getCounter());
        editor.putInt(CFACTORYCOUNTER, list.get(4).getCounter());
        editor.putInt(SFACTORYCOUNTER, list.get(5).getCounter());


        editor.commit();
        super.onStop();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        list.get(0).setCounter(sharedPreferences.getInt(AUTOCLICKCOUNTER, 0));
        list.get(1).setCounter(sharedPreferences.getInt(GRANDMACOUNTER, 0));
        list.get(2).setCounter(sharedPreferences.getInt(CROBOTCOUNTER, 0));
        list.get(3).setCounter(sharedPreferences.getInt(COOKIEFARMCOUNTER, 0));
        list.get(4).setCounter(sharedPreferences.getInt(CFACTORYCOUNTER, 0));
        list.get(5).setCounter(sharedPreferences.getInt(SFACTORYCOUNTER, 0));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuwindow);
        getWindow().setLayout(1000, 1400);

        sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        listView = findViewById(R.id.listView);
        if (list.size() == 0) {
            list.add(new MainActivity.Upgrade("AutoClick", 1, 50, 0, R.drawable.autoclick));
            list.add(new MainActivity.Upgrade("GrandMa", 3, 500, 0, R.drawable.grandma));
            list.add(new MainActivity.Upgrade("C-Robot", 10, 2000, 0, R.drawable.crobot));
            list.add(new MainActivity.Upgrade("CookieFarm", 20, 10000, 0, R.drawable.cookiefarm));
            list.add(new MainActivity.Upgrade("C-Factory", 40, 50000, 0, R.drawable.cfactory));
            list.add(new MainActivity.Upgrade("S-Factory", 100, 500000, 0, R.drawable.sfactory));

        }

        adapter = new CustomAdapter(this, R.layout.upgradelayout, list);

        listView.setAdapter(adapter);

        backgroundUpdate = new BackgroundUpdate();
        backgroundUpdate.start();


    }

    public class CustomAdapter extends ArrayAdapter<MainActivity.Upgrade> {

        Context context;
        int resource;
        ArrayList<MainActivity.Upgrade> list;

        public CustomAdapter(@NonNull Context context, int resource, @NonNull ArrayList<MainActivity.Upgrade> objects) {
            super(context, resource, objects);

            this.context = context;
            this.resource = resource;
            this.list = objects;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View adapterLayout = layoutInflater.inflate(resource, null);
            TextView tvname = adapterLayout.findViewById(R.id.tvname);
            TextView tvprice = adapterLayout.findViewById(R.id.tvprice);
            final TextView tvowned = adapterLayout.findViewById(R.id.tvowned);

            ImageView imageView = adapterLayout.findViewById(R.id.imageView);
            final Button button = adapterLayout.findViewById(R.id.buybutton);

            imageView.setImageResource(list.get(position).getImage());
            tvname.setText(list.get(position).getName()+"");
            tvprice.setText(list.get(position).getPrice()+"");
            tvowned.setText(list.get(position).getCounter()+"");

            if (!(MainActivity.cookiecounter.get() >= list.get(position).getPrice())) {
                button.setEnabled(false);
            } else {
                button.setEnabled(true);
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MainActivity.cookiecounter.get() >= list.get(position).getPrice()) {
                        list.get(position).addCounter();
                        tvowned.setText(list.get(position).getCounter()+"");
                        cookiecounter.getAndAdd(0-list.get(position).getPrice());
                        if (!(MainActivity.cookiecounter.get() >= list.get(position).getPrice())) {
                            button.setEnabled(false);
                        }
                    } else {
                        button.setEnabled(false);
                    }
                }
            });

            return adapterLayout;
        }
    }

    public class BackgroundUpdate extends Thread {
        @Override
        public void run() {
            while (true) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {

                }
            }
        }
    }
}
