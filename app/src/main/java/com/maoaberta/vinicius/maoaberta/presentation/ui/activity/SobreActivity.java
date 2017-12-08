package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maoaberta.vinicius.maoaberta.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vinicius on 29/08/17.
 */

public class SobreActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_tela_sobre)
    Toolbar toolbar_tela_sobre;
    @BindView(R.id.text_view_versao_app)
    TextView text_view_versao_app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_tela_sobre);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.app_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }

        try{
            text_view_versao_app.setText(getString(R.string.versao_app, getPackageManager().getPackageInfo(getPackageName(), 0).versionName));
        }catch(PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }
}
