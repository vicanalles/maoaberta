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
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.MenuPrincipalClienteActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinicius Canalles on 16/08/2017.
 */

public class TabCadastroCliente extends Fragment {

    FirebaseAuth mAuth;
    private UsuarioRepository usuarioRepository;

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

        mAuth = FirebaseAuth.getInstance();
        usuarioRepository = new UsuarioRepository();

        cadastrarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CadastroActivity) getActivity()).showProgressDialog("Cadastro", "Cadastrando voluntário...");
                if (String.valueOf(emailCliente.getText()).equals("") || String.valueOf(senhaCliente.getText()).equals("") ||
                        String.valueOf(confirmarSenha.getText()).equals("")) {
                    alertaCamposNaoPreenchidos();
                } else {
                    if (String.valueOf(senhaCliente.getText()).equals(String.valueOf(confirmarSenha.getText()))) {
                        if(senhaCliente.getText().length() >= 6 && confirmarSenha.getText().length() >= 6){
                            createAccountWithEmailAndPassword(String.valueOf(emailCliente.getText()), String.valueOf(senhaCliente.getText()));
                        }else{
                            alertaSenhaCurta();
                        }
                    } else {
                        alertaSenhasDiferentes();
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
                            builder.setMessage("Não foi possível salvar o voluntário. Por favor, tente novamente!");
                            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    ((CadastroActivity) getActivity()).hideProgressDialog();
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            AuthResult result = task.getResult();
                            FirebaseUser user = result.getUser();
                            usuarioRepository.getUserByUid(user.getUid(), new UsuarioRepository.OnGetUserById() {
                                @Override
                                public void onGetUserByIdSuccess(Voluntario voluntario) {
                                    if(voluntario != null){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
                                        builder.setMessage("Este endereço de e-mail já esta cadastrado em nosso banco de dados!");
                                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int i) {
                                                ((CadastroActivity) getActivity()).hideProgressDialog();
                                                dialog.dismiss();
                                            }
                                        });
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }else{
                                        abrirTelaRegistro();
                                    }
                                }

                                @Override
                                public void onGetUserByIdError(String error) {
                                    ((CadastroActivity) getActivity()).hideProgressDialog();
                                    Toast.makeText(getActivity(), "Erro ao cadastrar os dados. Por favor tente novamente", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
    }

    private void abrirTelaRegistro() {
        Intent intent = new Intent(getContext(), CompletarRegistroVoluntarioActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void alertaCamposNaoPreenchidos() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setMessage(R.string.campos_nao_preenchidos);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((CadastroActivity) getActivity()).hideProgressDialog();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void alertaSenhasDiferentes() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setMessage(R.string.senhas_diferentes);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((CadastroActivity) getActivity()).hideProgressDialog();
                senhaCliente.setText("");
                confirmarSenha.setText("");
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void alertaSenhaCurta() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setMessage(R.string.senha_curta);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((CadastroActivity) getActivity()).hideProgressDialog();
                senhaCliente.setText("");
                confirmarSenha.setText("");
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void abrirMenuPrincipalCliente() {
        Intent intent = new Intent(getContext(), MenuPrincipalClienteActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
