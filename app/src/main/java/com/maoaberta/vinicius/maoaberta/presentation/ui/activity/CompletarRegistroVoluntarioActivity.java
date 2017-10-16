package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Voluntario;
import com.maoaberta.vinicius.maoaberta.domain.repository.TipoRepository;
import com.maoaberta.vinicius.maoaberta.domain.repository.UsuarioRepository;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by vinicius on 16/10/17.
 */

public class CompletarRegistroVoluntarioActivity extends AppCompatActivity {

    private static final int SELECT_IMAGE = 1598;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private UsuarioRepository usuarioRepository;
    private Voluntario voluntario;
    private ProgressDialog progressDialog;

    @BindView(R.id.relative_layout_image_logo_completar_registro_voluntario)
    RelativeLayout relative_layout_image_logo_completar_registro_voluntario;
    @BindView(R.id.text_view_escolher_foto_completar_registro_voluntario)
    TextView text_view_escolher_foto_completar_registro_voluntario;
    @BindView(R.id.image_view_logo_completar_registro_voluntario)
    CircleImageView image_view_logo_completar_registro_voluntario;
    @BindView(R.id.edit_text_nome_completar_registro_voluntario)
    EditText edit_text_nome_completar_registro_voluntario;
    @BindView(R.id.edit_text_email_completar_registro_voluntario)
    EditText edit_text_email_completar_registro_voluntario;
    @BindView(R.id.edit_text_telefone_completar_registro_voluntario)
    EditText edit_text_telefone_completar_registro_voluntario;
    @BindView(R.id.botao_salvar_completar_registro_voluntario)
    Button botao_salvar_completar_registro_voluntario;
    @BindView(R.id.toolbar_layout_menu_completar_registro_voluntario)
    Toolbar toolbar_layout_menu_completar_registro_voluntario;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completar_cadastro_voluntario);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_layout_menu_completar_registro_voluntario);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        usuarioRepository = new UsuarioRepository();
        progressDialog = new ProgressDialog(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.pre_cadastro);
        }

        if(user != null){
            edit_text_nome_completar_registro_voluntario.setText(user.getDisplayName());
            edit_text_email_completar_registro_voluntario.setText(user.getEmail());
            edit_text_telefone_completar_registro_voluntario.setText(user.getPhoneNumber());
        }

        botao_salvar_completar_registro_voluntario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog("Cadastrando Voluntário", "Aguarde enquanto finalizamos o cadastro");
                voluntario = new Voluntario();
                voluntario.setNome(edit_text_nome_completar_registro_voluntario.getText().toString());
                voluntario.setEmail(edit_text_email_completar_registro_voluntario.getText().toString());
                voluntario.setTelefone(edit_text_telefone_completar_registro_voluntario.getText().toString());
                usuarioRepository.salvarDadosVoluntario(voluntario, null, new UsuarioRepository.OnSaveVoluntario() {
                    @Override
                    public void onSaveVoluntarioSuccess(Voluntario voluntario) {
                        TipoRepository tipoRepository = new TipoRepository();
                        tipoRepository.cadastrarTipo(user.getUid(), "tipo", "voluntario", new TipoRepository.OnCadastrarTipo() {
                            @Override
                            public void onCadastrarTipoSuccess(String sucesso) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(CompletarRegistroVoluntarioActivity.this, R.style.AppTheme));
                                builder.setMessage(sucesso);
                                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        abrirMenuPrincipalCliente();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }

                            @Override
                            public void onCadastrarTipoError(String error) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(CompletarRegistroVoluntarioActivity.this, R.style.AppTheme));
                                builder.setMessage(error);
                                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        hideProgressDialog();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        });
                    }

                    @Override
                    public void onSaveVoluntarioError(String error) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(CompletarRegistroVoluntarioActivity.this, R.style.AppTheme));
                        builder.setMessage(error);
                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }
        });
    }

    private void abrirMenuPrincipalCliente() {
        hideProgressDialog();
        Intent intent = new Intent(CompletarRegistroVoluntarioActivity.this, MenuPrincipalClienteActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        sairDoApp();
    }

    private void sairDoApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme));
        builder.setMessage(R.string.sair_app);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        abrirTelaLogin();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(CompletarRegistroVoluntarioActivity.this, R.style.AppTheme));
                        builder.setMessage("Ocorreu um erro. Por favor, tente novamente!!");
                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
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

    private void abrirTelaLogin() {
        Intent intent = new Intent(CompletarRegistroVoluntarioActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void showProgressDialog(String title, String content){
        progressDialog.setTitle(title);
        progressDialog.setMessage(content);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideProgressDialog(){
        progressDialog.dismiss();
    }
}
