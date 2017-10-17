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
import com.maoaberta.vinicius.maoaberta.domain.models.Organizacao;
import com.maoaberta.vinicius.maoaberta.domain.repository.OrganizacaoRepository;
import com.maoaberta.vinicius.maoaberta.domain.repository.TipoRepository;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by vinicius on 17/10/17.
 */

public class CompletarRegistroOrganizacaoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private OrganizacaoRepository organizacaoRepository;
    private ProgressDialog progressDialog;
    private Organizacao organizacao;

    @BindView(R.id.toolbar_layout_menu_completar_registro_organizacao)
    Toolbar toolbar_layout_menu_completar_registro_organizacao;
    @BindView(R.id.relative_layout_image_logo_completar_registro_organizacao)
    RelativeLayout relative_layout_image_logo_completar_registro_organizacao;
    @BindView(R.id.image_view_logo_completar_registro_organizacao)
    CircleImageView image_view_logo_completar_registro_organizacao;
    @BindView(R.id.text_view_escolher_foto_completar_registro_organizacao)
    TextView text_view_escolher_foto_completar_registro_organizacao;
    @BindView(R.id.edit_text_razao_social_completar_registro_organizacao)
    EditText edit_text_razao_social_completar_registro_organizacao;
    @BindView(R.id.edit_text_nome_fantasia_completar_registro_organizacao)
    EditText edit_text_nome_fantasia_completar_registro_organizacao;
    @BindView(R.id.edit_text_cnpj_completar_registro_organizacao)
    EditText edit_text_cnpj_completar_registro_organizacao;
    @BindView(R.id.edit_text_nome_responsavel_completar_registro_organizacao)
    EditText edit_text_nome_responsavel_completar_registro_organizacao;
    @BindView(R.id.edit_text_email_completar_registro_organizacao)
    EditText edit_text_email_completar_registro_organizacao;
    @BindView(R.id.edit_text_telefone_completar_registro_organizacao)
    EditText edit_text_telefone_completar_registro_organizacao;
    @BindView(R.id.edit_text_descricao_completar_registro_organizacao)
    EditText edit_text_descricao_completar_registro_organizacao;
    @BindView(R.id.button_cadastrar_completar_registro_organizacao)
    Button button_cadastrar_completar_registro_organizacao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completar_registro_organizacao);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_layout_menu_completar_registro_organizacao);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        organizacaoRepository = new OrganizacaoRepository();
        progressDialog = new ProgressDialog(this);

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.pre_cadastro);
        }

        if(user != null){
            edit_text_email_completar_registro_organizacao.setText(user.getEmail());
        }

        button_cadastrar_completar_registro_organizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!String.valueOf(edit_text_razao_social_completar_registro_organizacao.getText()).equals("") &&
                        !String.valueOf(edit_text_nome_fantasia_completar_registro_organizacao.getText()).equals("") &&
                        !String.valueOf(edit_text_cnpj_completar_registro_organizacao.getText()).equals("") &&
                        !String.valueOf(edit_text_nome_responsavel_completar_registro_organizacao.getText()).equals("") &&
                        !String.valueOf(edit_text_email_completar_registro_organizacao.getText()).equals("") &&
                        !String.valueOf(edit_text_telefone_completar_registro_organizacao.getText()).equals("") &&
                        !String.valueOf(edit_text_descricao_completar_registro_organizacao.getText()).equals("")){
                    showProgressDialog("Cadastrando Organização", "Aguarde enquanto finalizamos o cadastro");
                    organizacao = new Organizacao();
                    organizacao.setRazaoSocial(edit_text_razao_social_completar_registro_organizacao.getText().toString());
                    organizacao.setNomeFantasia(edit_text_nome_fantasia_completar_registro_organizacao.getText().toString());
                    organizacao.setCnpj(edit_text_cnpj_completar_registro_organizacao.getText().toString());
                    organizacao.setNomeResponsavel(edit_text_nome_responsavel_completar_registro_organizacao.getText().toString());
                    organizacao.setEmail(edit_text_email_completar_registro_organizacao.getText().toString());
                    organizacao.setTelefone(edit_text_telefone_completar_registro_organizacao.getText().toString());
                    organizacao.setDescricao(edit_text_descricao_completar_registro_organizacao.getText().toString());
                    organizacaoRepository.salvarDadosOrganizacao(organizacao, null, new OrganizacaoRepository.OnSaveOrganizacao() {
                        @Override
                        public void onSaveOrganizacaoSuccess(Organizacao organizacao) {
                            TipoRepository tipoRepository = new TipoRepository();
                            tipoRepository.cadastrarTipo(user.getUid(), "tipo", "organizacao", new TipoRepository.OnCadastrarTipo() {
                                @Override
                                public void onCadastrarTipoSuccess(String sucesso) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(CompletarRegistroOrganizacaoActivity.this, R.style.AppTheme));
                                    builder.setMessage(sucesso);
                                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            abrirMenuPrincipalOrganizacao();
                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }

                                @Override
                                public void onCadastrarTipoError(String error) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(CompletarRegistroOrganizacaoActivity.this, R.style.AppTheme));
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
                        public void onSaveOrganizacaoError(String error) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(CompletarRegistroOrganizacaoActivity.this, R.style.AppTheme));
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
                }else{
                    alertaCamposVazios();
                }
            }
        });
    }

    private void alertaCamposVazios() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(CompletarRegistroOrganizacaoActivity.this, R.style.AppTheme));
        builder.setMessage("Preencha todos os campos para realizar o cadastro!");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                hideProgressDialog();
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(CompletarRegistroOrganizacaoActivity.this, R.style.AppTheme));
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
        Intent intent = new Intent(CompletarRegistroOrganizacaoActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void abrirMenuPrincipalOrganizacao() {
        hideProgressDialog();
        Intent intent = new Intent(CompletarRegistroOrganizacaoActivity.this, MenuPrincipalOrganizacaoActivity.class);
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
