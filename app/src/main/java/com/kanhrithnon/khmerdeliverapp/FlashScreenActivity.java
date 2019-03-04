package com.kanhrithnon.khmerdeliverapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FlashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(FlashScreenActivity.this,SignInActivity.class);
                startActivity(intent);
                finish();
            }
        };
        new Handler().postDelayed(runnable,3000);
    }
}
