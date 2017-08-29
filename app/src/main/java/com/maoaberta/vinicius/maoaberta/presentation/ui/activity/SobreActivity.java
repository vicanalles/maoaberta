package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.maoaberta.vinicius.maoaberta.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vinicius on 29/08/17.
 */

public class SobreActivity extends AppCompatActivity{

    @BindView(R.id.toolbar_layout_sobre)
    Toolbar toolbar_layout_sobre;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_layout_sobre);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tela_sobre, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.item_login:
                abrirTelaLogin();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void abrirTelaLogin(){
        Intent intent = new Intent(SobreActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
