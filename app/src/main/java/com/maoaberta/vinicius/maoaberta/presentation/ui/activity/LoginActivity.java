package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.maoaberta.vinicius.maoaberta.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinicius on 12/08/2017.
 */

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.text_view_abrir_cadastro)
    TextView textViewAbrirCadastro;
    @BindView(R.id.edit_text_login)
    EditText editTextEmail;
    @BindView(R.id.edit_text_password)
    EditText editTextPassword;
    @BindView(R.id.button_login_app)
    Button buttonLogin;
    @BindView(R.id.toolbar_layout_login)
    Toolbar toolbar_layout_login;
    @BindView(R.id.text_view_esqueceu_senha)
    TextView text_view_esqueceu_senha;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_layout_login);

        textViewAbrirCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        });

        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, PerfilClienteActivity.class);
                startActivity(intent);
                finish();
            }
        });

        text_view_esqueceu_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirRecuperarSenha();
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

    public void abrirRecuperarSenha(){
        Intent intent = new Intent(LoginActivity.this, EsqueceuSenhaActivity.class);
        startActivity(intent);
    }

    public void abrirConfiguracoes(){
        startActivity(new Intent(Settings.ACTION_SETTINGS));
    }
}
