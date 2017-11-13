package com.maoaberta.vinicius.maoaberta.presentation.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.CadastroActivity;
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.EsqueceuSenhaActivity;
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.LoginActivity;
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.MenuPrincipalOrganizacaoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vinicius on 30/08/17.
 */

public class TabLoginOrganizacao extends Fragment {

    private FirebaseAuth mAuth;

    @BindView(R.id.edit_text_login_email_organizacao)
    EditText edit_text_login_email_organizacao;
    @BindView(R.id.edit_text_login_senha_organizacao)
    EditText edit_text_login_senha_organizacao;
    @BindView(R.id.button_app_login_organizacao)
    Button button_app_login_organizacao;
    @BindView(R.id.text_view_abrir_cadastro_login_organizacao)
    TextView text_view_abrir_cadastro_login_organizacao;
    @BindView(R.id.text_view_esqueceu_senha_login_organizacao)
    TextView text_view_esqueceu_senha_login_organizacao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab_login_organizacao, container, false);
        ButterKnife.bind(this, v);

        mAuth = FirebaseAuth.getInstance();

        text_view_abrir_cadastro_login_organizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirActivityCadastro();
            }
        });

        text_view_esqueceu_senha_login_organizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirRecuperarSenha();
            }
        });

        button_app_login_organizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailOrganizacao = String.valueOf(edit_text_login_email_organizacao.getText());
                String senhaOrganizacao = String.valueOf(edit_text_login_senha_organizacao.getText());
                if(!emailOrganizacao.equals("") && !senhaOrganizacao.equals("")){
                    signIn(emailOrganizacao, senhaOrganizacao);
                }else{
                    campoVazio();
                }
            }
        });

        return v;
    }

    private void signIn(String email, String senha){
        ((LoginActivity) getActivity()).showProgressDialog("Aguarde", "Obtendo informações da Organização");
        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            alertaDadosIncorretos();
                        }else{
                            abrirMenuPrincipalOrganizacao();
                        }
                    }
                });
    }

    private void alertaDadosIncorretos(){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setMessage(R.string.dados_incorretos);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((LoginActivity) getActivity()).hideProgressDialog();
                edit_text_login_senha_organizacao.setText("");
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void abrirMenuPrincipalOrganizacao(){
        ((LoginActivity) getActivity()).hideProgressDialog();
        Intent intent = new Intent(getContext(), MenuPrincipalOrganizacaoActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    public void abrirRecuperarSenha(){
        Intent intent = new Intent(getContext(), EsqueceuSenhaActivity.class);
        startActivity(intent);
    }

    public void abrirActivityCadastro(){
        Intent intent = new Intent(getContext(), CadastroActivity.class);
        startActivity(intent);
    }

    private void campoVazio(){
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
