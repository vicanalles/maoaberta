package com.maoaberta.vinicius.maoaberta.presentation.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.CadastroActivity;
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.EsqueceuSenhaActivity;
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.MenuPrincipalClienteActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vinicius on 30/08/17.
 */

public class TabLoginCliente extends Fragment {

    @BindView(R.id.edit_text_login_email_cliente)
    EditText edit_text_login_email_cliente;
    @BindView(R.id.edit_text_login_senha_cliente)
    EditText edit_text_login_senha_cliente;
    @BindView(R.id.button_app_login_cliente)
    Button button_app_login_cliente;
    @BindView(R.id.text_view_abrir_cadastro_login_cliente)
    TextView text_view_abrir_cadastro_login_cliente;
    @BindView(R.id.text_view_esqueceu_senha_login_cliente)
    TextView text_view_esqueceu_senha_login_cliente;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab_login_cliente, container, false);
        ButterKnife.bind(this, v);

        text_view_abrir_cadastro_login_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirActivityCadastro();
            }
        });

        text_view_esqueceu_senha_login_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirRecuperarSenha();
            }
        });
        
        button_app_login_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MenuPrincipalClienteActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

    public void abrirRecuperarSenha(){
        Intent intent = new Intent(getContext(), EsqueceuSenhaActivity.class);
        startActivity(intent);
    }

    public void abrirActivityCadastro(){
        Intent intent = new Intent(getContext(), CadastroActivity.class);
        startActivity(intent);
    }
}
