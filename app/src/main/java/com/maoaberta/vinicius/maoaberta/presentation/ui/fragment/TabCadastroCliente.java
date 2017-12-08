package com.maoaberta.vinicius.maoaberta.presentation.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.maoaberta.vinicius.maoaberta.domain.models.Voluntario;
import com.maoaberta.vinicius.maoaberta.domain.repository.UsuarioRepository;
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.CadastroActivity;
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.CompletarRegistroVoluntarioActivity;
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.MenuPrincipalVoluntarioActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinicius Canalles on 16/08/2017.
 */

public class TabCadastroCliente extends Fragment {

    FirebaseAuth mAuth;
    private UsuarioRepository usuarioRepository;
    private Context context;
    private ProgressDialog progressDialog;

    @BindView(R.id.edit_text_email_cadastro_cliente)
    EditText emailCliente;
    @BindView(R.id.edit_text_senha_cadastro_cliente)
    EditText senhaCliente;
    @BindView(R.id.edit_text_confirmar_senha_cadastro_cliente)
    EditText confirmarSenha;
    @BindView(R.id.button_cadastrar_cliente)
    Button cadastrarCliente;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab_cadastro_cliente, container, false);
        ButterKnife.bind(this, v);
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Cadastro");
        progressDialog.setMessage("Cadastrando voluntário...");
        mAuth = FirebaseAuth.getInstance();
        usuarioRepository = new UsuarioRepository();

        cadastrarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                if (String.valueOf(emailCliente.getText()).equals("") || String.valueOf(senhaCliente.getText()).equals("") ||
                        String.valueOf(confirmarSenha.getText()).equals("")) {
                    progressDialog.hide();
                    Toast.makeText(context, "Alguns campos não foram preenchidos. Por favor, preencher todos os campos!", Toast.LENGTH_SHORT).show();
                } else {
                    if (String.valueOf(senhaCliente.getText()).equals(String.valueOf(confirmarSenha.getText()))) {
                        if (senhaCliente.getText().length() >= 6 && confirmarSenha.getText().length() >= 6) {
                            createAccountWithEmailAndPassword(String.valueOf(emailCliente.getText()), String.valueOf(senhaCliente.getText()));
                        } else {
                            progressDialog.hide();
                            senhaCliente.setText("");
                            confirmarSenha.setText("");
                            Toast.makeText(context, "Senha curta. Digite pelo menos 6 caracteres para definir sua senha.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        progressDialog.hide();
                        senhaCliente.setText("");
                        confirmarSenha.setText("");
                        Toast.makeText(context, "Senha e Confirmação de Senha devem ser iguais. Digite novamente!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return v;
    }

    private void createAccountWithEmailAndPassword(final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            progressDialog.hide();
                            Toast.makeText(context, "E-mail já existente!! Por favor, tente novamente.", Toast.LENGTH_SHORT).show();
                        } else {
                            AuthResult result = task.getResult();
                            FirebaseUser user = result.getUser();
                            usuarioRepository.getUserByUid(user.getUid(), new UsuarioRepository.OnGetUserById() {
                                @Override
                                public void onGetUserByIdSuccess(Voluntario voluntario) {
                                    if (voluntario != null) {
                                        progressDialog.hide();
                                    } else {
                                        Intent intent = new Intent(context, CompletarRegistroVoluntarioActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                }

                                @Override
                                public void onGetUserByIdError(String error) {
                                    progressDialog.hide();
                                }
                            });
                        }
                    }
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
