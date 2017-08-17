package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.maoaberta.vinicius.maoaberta.R;

/**
 * Created by Vinicius on 12/08/2017.
 */

public class LoginActivity extends AppCompatActivity{

    TextView textViewAbrirCadastro;
    EditText editTextEmail;
    EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = (EditText) findViewById(R.id.edit_text_login);
        editTextPassword = (EditText) findViewById(R.id.edit_text_password);
        textViewAbrirCadastro = (TextView) findViewById(R.id.text_view_abrir_cadastro);

        textViewAbrirCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        });
    }
}