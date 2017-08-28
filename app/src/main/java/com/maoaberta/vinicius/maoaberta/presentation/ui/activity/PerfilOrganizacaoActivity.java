package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.maoaberta.vinicius.maoaberta.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinicius on 23/08/2017.
 */

public class PerfilOrganizacaoActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_layout_perfil_organizacao)
    Toolbar toolbar_layout_perfil_organizacao;
    @BindView(R.id.relative_layout_image_logo)
    RelativeLayout relative_layout_image_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_organizacao);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar_layout_perfil_organizacao);

        relative_layout_image_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PerfilOrganizacaoActivity.this, "Abrir galeria do dispositivo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.item_configuracoes:
                Toast.makeText(this, "Configuracoes do Dispositivo", Toast.LENGTH_LONG).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
