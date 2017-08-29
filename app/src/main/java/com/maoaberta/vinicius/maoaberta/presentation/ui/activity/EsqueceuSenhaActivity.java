package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.maoaberta.vinicius.maoaberta.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vinicius on 29/08/17.
 */

public class EsqueceuSenhaActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_layout_esqueceu_senha)
    Toolbar toolbar_layout_esqueceu_senha;
    @BindView(R.id.botao_recuperar_senha)
    Button botao_recuperar_senha;
    @BindView(R.id.edit_text_email_nova_senha)
    EditText edit_text_nova_senha;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceu_senha);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_layout_esqueceu_senha);

        edit_text_nova_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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
                abrirConfiguracoes();
        }

        return super.onOptionsItemSelected(item);
    }

    public void abrirConfiguracoes(){
        Toast.makeText(this, "Abrir configuracoes do dispositivo", Toast.LENGTH_LONG).show();
        //startActivity(new Intent(Settings.ACTION_SETTINGS));
    }
}