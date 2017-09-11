package com.maoaberta.vinicius.maoaberta.presentation.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private FirebaseAuth mAuth;

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

        mAuth = FirebaseAuth.getInstance();

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
                String emailLogin = String.valueOf(edit_text_login_email_cliente.getText());
                String senhaLogin = String.valueOf(edit_text_login_senha_cliente.getText());
                if(!emailLogin.equals("") && !senhaLogin.equals("")){
                    signIn(emailLogin, senhaLogin);
                }else{
                    campoVazio();
                }

            }
        });

        return v;
    }

    public void abrirRecuperarSenha() {
        Intent intent = new Intent(getContext(), EsqueceuSenhaActivity.class);
        startActivity(intent);
    }

    public void abrirActivityCadastro() {
        Intent intent = new Intent(getContext(), CadastroActivity.class);
        startActivity(intent);
    }

    public void abrirMenuPrincipalCliente(FirebaseUser user){
        Intent intent = new Intent(getContext(), MenuPrincipalClienteActivity.class);
        intent.putExtra("userName", user.getUid());
        startActivity(intent);
        getActivity().finish();
    }

    public void signIn(final String email, String senha) {
        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            alertaDadosIncorretos();
                        } else {
                            AuthResult result = task.getResult();
                            FirebaseUser user = result.getUser();
                            abrirMenuPrincipalCliente(user);
                        }
                    }
                });
    }

    private void alertaDadosIncorretos() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setMessage(R.string.dados_incorretos);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void campoVazio() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setMessage(R.string.preencher_campos);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
