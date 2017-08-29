package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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
 * Created by vinicius on 29/08/17.
 */

public class PerfilClienteActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_layout_perfil_cliente)
    Toolbar toolbar_layout_perfil_cliente;
    @BindView(R.id.relative_layout_image_logo_perfil_cliente)
    RelativeLayout relative_layout_image_logo_perfil_cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_cliente);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar_layout_perfil_cliente);

        relative_layout_image_logo_perfil_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Abrir galeria do dispositivo", Toast.LENGTH_SHORT).show();
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
        switch (item.getItemId()) {

            case R.id.item_configuracoes:
                abrirConfiguracoes();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void abrirConfiguracoes(){
        startActivity(new Intent(Settings.ACTION_SETTINGS));
    }
}
