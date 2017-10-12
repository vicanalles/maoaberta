package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Voluntario;
import com.maoaberta.vinicius.maoaberta.domain.repository.TipoRepository;
import com.maoaberta.vinicius.maoaberta.domain.repository.UsuarioRepository;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by vinicius on 29/08/17.
 */

public class PerfilVoluntarioActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private UsuarioRepository usuarioRepository;
    private Voluntario vol;

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
    @BindView(R.id.toolbar_layout_menu_perfil_cliente)
    Toolbar toolbar_layout_menu_perfil_cliente;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_cliente);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_layout_menu_perfil_cliente);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
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
                    Toast.makeText(PerfilVoluntarioActivity.this, R.string.usuario_inexistente, Toast.LENGTH_LONG).show();
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

                                    if (senha != null) {
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
                                            } else {
                                                alertaSenhaCurta();
                                            }
                                        }
                                    }


                                    usuarioRepository.atualizarUser(vol, user);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PerfilVoluntarioActivity.this, R.style.AppTheme));
                                    builder.setMessage(R.string.atualizacao_usuario_sucesso);
                                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int i) {
                                            abrirMenuPrincipal();
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

                                    if (senha != null) {
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
                                            } else {
                                                alertaSenhaCurta();
                                            }
                                        }
                                    }

                                    usuarioRepository.cadastrarUsuario(vol, user);
                                    TipoRepository tipoRepository = new TipoRepository();
                                    tipoRepository.cadastrarTipo(user, "tipo", "voluntario");

                                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PerfilVoluntarioActivity.this, R.style.AppTheme));
                                    builder.setMessage(R.string.usuario_cadastrado_sucesso);
                                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int i) {
                                            abrirMenuPrincipal();
                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            }

                            @Override
                            public void onGetUserByIdError(String error) {
                                Log.d("onGetUserByIdError", error);
                                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PerfilVoluntarioActivity.this, R.style.AppTheme));
                                builder.setMessage(R.string.erro_atualizar_dados_usuario);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_perfil, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.item_exit_menu_principal:
                sairDoApp();
                break;
            case android.R.id.home:
                abrirMenuPrincipal();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        abrirMenuPrincipal();
    }

    private void sairDoApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme));
        builder.setMessage(R.string.sair_app);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                abrirTelaLogin();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void abrirTelaLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void alertaCamposNaoPreenchidos() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PerfilVoluntarioActivity.this, R.style.AppTheme));
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
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PerfilVoluntarioActivity.this, R.style.AppTheme));
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

    private void abrirMenuPrincipal() {
        Intent intent = new Intent(PerfilVoluntarioActivity.this, MenuPrincipalClienteActivity.class);
        startActivity(intent);
        finish();
    }
}