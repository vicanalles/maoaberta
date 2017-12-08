package com.maoaberta.vinicius.maoaberta.presentation.ui.fragment;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Organizacao;
import com.maoaberta.vinicius.maoaberta.domain.repository.OrganizacaoRepository;
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.CadastroActivity;
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.CompletarRegistroOrganizacaoActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinicius Canalles on 16/08/2017.
 */

public class TabCadastroOrganizacao extends Fragment {

    FirebaseAuth mAuth;
    private OrganizacaoRepository organizacaoRepository;

    @BindView(R.id.edit_text_email_organizacao)
    EditText emailOrganizacao;
    @BindView(R.id.edit_text_senha_organizacao)
    EditText senhaOrganizacao;
    @BindView(R.id.edit_text_confirmar_senha_organizacao)
    EditText confirmarSenhaOrganizacao;
    @BindView(R.id.button_cadastrar_organizacao)
    Button cadastrarOrganizacao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab_cadastro_organizacao, container, false);
        ButterKnife.bind(this, v);

        organizacaoRepository = new OrganizacaoRepository();
        mAuth = FirebaseAuth.getInstance();

        cadastrarOrganizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CadastroActivity) getActivity()).showProgressDialog("Cadastro", "Cadastrando organização...");
                if(String.valueOf(emailOrganizacao.getText()).equals("") || String.valueOf(senhaOrganizacao.getText()).equals("") ||
                        String.valueOf(confirmarSenhaOrganizacao.getText()).equals("")){
                    ((CadastroActivity) getActivity()).hideProgressDialog();
                    Toast.makeText(getActivity(), "Alguns campos não foram preenchidos. Por favor, preencher todos os campos", Toast.LENGTH_SHORT).show();
                }else{
                    if(String.valueOf(senhaOrganizacao.getText()).equals(String.valueOf(confirmarSenhaOrganizacao.getText()))){
                        if(senhaOrganizacao.getText().length() >= 6 && confirmarSenhaOrganizacao.getText().length() >= 6){
                            createAccountWithEmailAndPassword(String.valueOf(emailOrganizacao.getText()), String.valueOf(senhaOrganizacao.getText()));
                        }else{
                            ((CadastroActivity) getActivity()).hideProgressDialog();
                            senhaOrganizacao.setText("");
                            confirmarSenhaOrganizacao.setText("");
                            Toast.makeText(getActivity(), "Senha curta. Digite pelo menos 6 caracteres para definir sua senha.", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        ((CadastroActivity) getActivity()).hideProgressDialog();
                        senhaOrganizacao.setText("");
                        confirmarSenhaOrganizacao.setText("");
                        Toast.makeText(getActivity(), "Senha e Confirmação de Senha devem ser iguais. Digite novamente!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return v;
    }

    private void createAccountWithEmailAndPassword(final String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            ((CadastroActivity) getActivity()).hideProgressDialog();
                            Toast.makeText(getActivity(), "E-mail já existente!! Por favor, tente novamente.", Toast.LENGTH_SHORT).show();
                        }else{
                            AuthResult result = task.getResult();
                            FirebaseUser user = result.getUser();
                            organizacaoRepository.getOrganizacaoById(user.getUid(), new OrganizacaoRepository.OnGetOrganizacaoById() {
                                @Override
                                public void onGetOrganizacaoByIdSuccess(Organizacao organizacao) {
                                    if(organizacao != null){
                                        ((CadastroActivity) getActivity()).hideProgressDialog();
                                        Toast.makeText(getActivity(), "Usuário já cadastrado no sistema!", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Intent intent = new Intent(getContext(), CompletarRegistroOrganizacaoActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                }

                                @Override
                                public void onGetOrganizacaoByIdError(String error) {
                                    ((CadastroActivity) getActivity()).hideProgressDialog();
                                    Toast.makeText(getActivity(), "Erro ao cadastrar os dados. Por favor tente novamente", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
    }
}
