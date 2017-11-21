package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.app_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }

        botao_recuperar_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(edit_text_nova_senha.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(EsqueceuSenhaActivity.this, R.style.AppTheme));
                            builder.setMessage("E-mail de recuperação de senha enviado com sucesso!");
                            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    Intent intent = new Intent(EsqueceuSenhaActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(EsqueceuSenhaActivity.this, R.style.AppTheme));
                            builder.setMessage("Falha no envio do E-mail. Por favor, tente novamente!");
                            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }

        return true;
    }
}
