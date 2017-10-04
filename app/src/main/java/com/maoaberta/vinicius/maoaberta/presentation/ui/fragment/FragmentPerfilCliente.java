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
import android.widget.ImageView;
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
import com.maoaberta.vinicius.maoaberta.domain.repository.TipoRepository;
import com.maoaberta.vinicius.maoaberta.domain.repository.UsuarioRepository;
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.MenuPrincipalClienteActivity;

import java.security.AuthProvider;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by vinicius on 29/08/17.
 */

public class FragmentPerfilCliente extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private UsuarioRepository usuarioRepository;
    private Voluntario vol;
    private String codigoAtivaCampos;

    @BindView(R.id.relative_layout_image_logo_perfil_cliente)
    RelativeLayout relative_layout_image_logo_perfil_cliente;
    @BindView(R.id.image_view_logo_perfil_cliente)
    CircleImageView image_view_logo_perfil_cliente;
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

        Bundle extras = getActivity().getIntent().getExtras();
        if(extras != null){
            codigoAtivaCampos = extras.getString("codigo");
        }else{
            codigoAtivaCampos = "0";
        }

        if(codigoAtivaCampos.equals("2")){
            edit_text_senha_perfil_cliente.setEnabled(false);
            edit_text_senha_perfil_cliente.setVisibility(View.GONE);
            edit_text_confirmar_senha_perfil_cliente.setEnabled(false);
            edit_text_confirmar_senha_perfil_cliente.setVisibility(View.GONE);
        }else if(codigoAtivaCampos.equals("0")){
            edit_text_senha_perfil_cliente.setEnabled(true);
            edit_text_senha_perfil_cliente.setVisibility(View.VISIBLE);
            edit_text_confirmar_senha_perfil_cliente.setEnabled(true);
            edit_text_confirmar_senha_perfil_cliente.setVisibility(View.VISIBLE);
        }

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        usuarioRepository = new UsuarioRepository();

        if (user != null) {
            String uid = user.getUid();
            usuarioRepository.getUserByUid(uid, new UsuarioRepository.OnGetUserById() {
                @Override
                public void onGetUserByIdSuccess(Voluntario voluntario) {
                    if (voluntario != null) {
                        edit_text_nome_perfil_cliente.setText(voluntario.getNome());
                        edit_text_email_perfil_cliente.setText(voluntario.getEmail());
                        edit_text_telefone_perfil_cliente.setText(voluntario.getTelefone());
                    } else {
                        edit_text_nome_perfil_cliente.setText(user.getDisplayName());
                        edit_text_email_perfil_cliente.setText(user.getEmail());
                        botao_salvar_perfil_cliente.setText(R.string.salvar);
                    }
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
                        String.valueOf(edit_text_email_perfil_cliente.getText()).equals("")) {
                    alertaCamposNaoPreenchidos();
                } else {
                    user = mAuth.getCurrentUser();
                    if (user != null) {
                        usuarioRepository.getUserByUid(user.getUid(), new UsuarioRepository.OnGetUserById() {
                            @Override
                            public void onGetUserByIdSuccess(Voluntario voluntario) {
                                if (voluntario != null) {
                                    vol = new Voluntario();
                                    vol.setNome(String.valueOf(edit_text_nome_perfil_cliente.getText()));
                                    vol.setEmail(voluntario.getEmail());
                                    vol.setTelefone(String.valueOf(edit_text_telefone_perfil_cliente.getText()));
                                    String senha = String.valueOf(edit_text_senha_perfil_cliente.getText());
                                    String confirmarSenha = String.valueOf(edit_text_confirmar_senha_perfil_cliente.getText());

                                    if(senha != null){
                                        if (Objects.equals(senha, confirmarSenha)) {
                                            if (senha.length() >= 6 && confirmarSenha.length() >= 6) {
                                                user.updatePassword(String.valueOf(edit_text_senha_perfil_cliente.getText())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            edit_text_senha_perfil_cliente.setText("");
                                                            edit_text_confirmar_senha_perfil_cliente.setText("");
                                                        }
                                                    }
                                                });
                                            }else {
                                                alertaSenhaCurta();
                                            }
                                        }
                                    }


                                    usuarioRepository.atualizarUser(vol, user);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
                                    builder.setMessage("Usuário atualizado com sucesso!");
                                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int i) {
                                            abrirMenuPrincipal("1");
                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                } else {
                                    vol = new Voluntario();
                                    vol.setNome(user.getDisplayName());
                                    vol.setEmail(user.getEmail());
                                    vol.setTelefone(String.valueOf(edit_text_telefone_perfil_cliente.getText()));
                                    String senha = String.valueOf(edit_text_senha_perfil_cliente.getText());
                                    String confirmarSenha = String.valueOf(edit_text_confirmar_senha_perfil_cliente.getText());

                                    if(senha != null){
                                        if (Objects.equals(senha, confirmarSenha)) {
                                            if (senha.length() >= 6 && confirmarSenha.length() >= 6) {
                                                user.updatePassword(String.valueOf(edit_text_senha_perfil_cliente.getText())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            edit_text_senha_perfil_cliente.setText("");
                                                            edit_text_confirmar_senha_perfil_cliente.setText("");
                                                        }
                                                    }
                                                });
                                            }else {
                                                alertaSenhaCurta();
                                            }
                                        }
                                    }

                                    usuarioRepository.cadastrarUsuario(vol, user);
                                    TipoRepository tipoRepository = new TipoRepository();
                                    tipoRepository.cadastrarTipo(user, "tipo", "voluntario");

                                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme));
                                    builder.setMessage("Usuário Cadastrado com sucesso!");
                                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int i) {
                                            abrirMenuPrincipal("2");
                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
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

    private void abrirMenuPrincipal(String codigoAtivaCampos) {
        Intent intent = new Intent(getContext(), MenuPrincipalClienteActivity.class);
        intent.putExtra("codigo", codigoAtivaCampos);
        startActivity(intent);
        getActivity().finish();
    }
}
