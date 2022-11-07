package com.lit.riseup;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                    SharedPreference data = new SharedPreference();
                    if(data.getURL(MainActivity.this).length() == 0){
                        startActivity(new Intent(MainActivity.this, SignIn.class));
                        finish();
                    }else{
                        startActivity(new Intent(MainActivity.this, Home.class));
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}