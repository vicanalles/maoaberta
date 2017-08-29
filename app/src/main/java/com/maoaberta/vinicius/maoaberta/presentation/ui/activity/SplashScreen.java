package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.maoaberta.vinicius.maoaberta.R;

import butterknife.ButterKnife;

/**
 * Created by Vinicius on 12/08/2017.
 */

public class SplashScreen extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mostrarTelaSobre();
            }
        }, 5000);
    }

    private void mostrarTelaSobre(){
        Intent intent = new Intent(SplashScreen.this, ApresentacaoActivity.class);
        startActivity(intent);
        finish();
    }
}
