package com.maoaberta.vinicius.maoaberta.presentation.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Voluntario;
import com.maoaberta.vinicius.maoaberta.domain.repository.UsuarioRepository;
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.MenuPrincipalClienteActivity;

import java.security.AuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vinicius on 29/08/17.
 */

public class FragmentPerfilCliente extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private UsuarioRepository usuarioRepository;
    private Voluntario vol;

    @BindView(R.id.edit_text_nome_perfil_cliente)
    EditText edit_text_nome_perfil_cliente;
    @BindView(R.id.edit_text_email_perfil_cliente)
    EditText edit_text_email_perfil_cliente;
    @BindView(R.id.edit_text_telefone_perfil_cliente)
    EditText edit_text_telefone_perfil_cliente;
    @BindView(R.id.edit_text_senha_perfil_cliente)
    EditText edit_text_senha_perfil_cliente;
    @BindView(R.id.edit_text_confirmar_senha_perfil_cliente)
    EditText edit_text_confirmar_senha_perfil_cliente;
    @BindView(R.id.botao_salvar_perfil_cliente)
    Button botao_salvar_perfil_cliente;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_perfil_cliente, container, false);
        ButterKnife.bind(this, view);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        usuarioRepository = new UsuarioRepository();

        if(user != null){
            String uid = user.getUid();
            usuarioRepository.getUserByUid(uid, new UsuarioRepository.OnGetUserById() {
                @Override
                public void onGetUserByIdSuccess(Voluntario voluntario) {
                    edit_text_nome_perfil_cliente.setText(voluntario.getNome());
                    edit_text_email_perfil_cliente.setText(voluntario.getEmail());
                    edit_text_telefone_perfil_cliente.setText(voluntario.getTelefone());
                }

                @Override
                public void onGetUserByIdError(String error) {
                    Log.d("onGetUserByIdError", error);
                    Toast.makeText(getActivity(), "Usuário não existe", Toast.LENGTH_LONG).show();
                }
            });
        }

        botao_salvar_perfil_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(edit_text_nome_perfil_cliente.getText()).equals("") || String.valueOf(edit_text_telefone_perfil_cliente.getText()).equals("") ||
                        String.valueOf(edit_text_email_perfil_cliente.getText()).equals("") || String.valueOf(edit_text_senha_perfil_cliente.getText()).equals("") ||
                        String.valueOf(edit_text_confirmar_senha_perfil_cliente.getText()).equals("")) {
                    alertaCamposNaoPreenchidos();
                } else {
                    if (String.valueOf(edit_text_senha_perfil_cliente.getText()).equals(String.valueOf(edit_text_confirmar_senha_perfil_cliente.getText()))) {
                        if(edit_text_senha_perfil_cliente.getText().length() >= 6 && edit_text_confirmar_senha_perfil_cliente.getText().length() >= 6){
                            user = mAuth.getCurrentUser();
                            if(user != null){
                                usuarioRepository.getUserByUid(user.getUid(), new UsuarioRepository.OnGetUserById() {
                                    @Override
                                    public void onGetUserByIdSuccess(Voluntario voluntario) {
                                        vol = new Voluntario();
                                        vol.setNome(String.valueOf(edit_text_nome_perfil_cliente.getText()));
                                        vol.setEmail(voluntario.getEmail());
                                        vol.setTelefone(String.valueOf(edit_text_telefone_perfil_cliente.getText()));

                                        user.updatePassword(String.valueOf(edit_text_senha_perfil_cliente.getText())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    edit_text_senha_perfil_cliente.setText("");
                                                    edit_text_confirmar_senha_perfil_cliente.setText("");
                                                }else{
                                                    Toast.makeText(getActivity(), "Não foi possível alterar a senha de autenticação", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                                        usuarioRepository.atualizarUser(vol, user);

                                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
                                        builder.setMessage("Usuário atualizado com sucesso!");
                                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int i) {
                                                abrirMenuPrincipal();
                                            }
                                        });
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }

                                    @Override
                                    public void onGetUserByIdError(String error) {
                                        Log.d("onGetUserByIdError", error);
                                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
                                        builder.setMessage("Não foi possível atualizar os dados do usuário. Por favor, tente novamente!");
                                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int i) {
                                                dialog.dismiss();
                                            }
                                        });
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                });
                            }
                        }else{
                            alertaSenhaCurta();
                        }
                    } else {
                        alertaSenhasDiferentes();
                    }
                }
            }
        });

        return view;
    }

    private void alertaCamposNaoPreenchidos() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
        builder.setMessage(R.string.campos_nao_preenchidos);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void abrirMenuPrincipal(){
        Intent intent = new Intent(getContext(), MenuPrincipalClienteActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
