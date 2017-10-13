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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Organizacao;
import com.maoaberta.vinicius.maoaberta.domain.repository.OrganizacaoRepository;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Vinicius on 23/08/2017.
 */

public class PerfilOrganizacaoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private OrganizacaoRepository organizacaoRepository;
    private Organizacao ong;

    @BindView(R.id.toolbar_layout_menu_perfil_organizacao)
    Toolbar toolbar_layout_menu_perfil_organizacao;
    @BindView(R.id.relative_layout_image_logo_perfil_organizacao)
    RelativeLayout relative_layout_image_logo_perfil_organizacao;
    @BindView(R.id.image_view_logo_perfil_organizacao)
    CircleImageView image_view_logo_perfil_organizacao;
    @BindView(R.id.text_view_escolher_foto_perfil_organizacao)
    TextView text_view_escolher_foto_perfil_organizacao;
    @BindView(R.id.edit_text_razao_social_perfil_organizacao)
    EditText edit_text_razao_social_perfil_organizacao;
    @BindView(R.id.edit_text_nome_fantasia_perfil_organizacao)
    EditText edit_text_nome_fantasia_perfil_organizacao;
    @BindView(R.id.edit_text_cnpj_perfil_organizacao)
    EditText edit_text_cnpj_perfil_organizacao;
    @BindView(R.id.edit_text_nome_responsavel_perfil_organizacao)
    EditText edit_text_nome_responsavel_perfil_organizacao;
    @BindView(R.id.edit_text_email_perfil_organizacao)
    EditText edit_text_email_perfil_organizacao;
    @BindView(R.id.edit_text_telefone_perfil_organizacao)
    EditText edit_text_telefone_perfil_organizacao;
    @BindView(R.id.edit_text_endereco_perfil_organizacao)
    EditText edit_text_endereco_perfil_organizacao;
    @BindView(R.id.edit_text_facebook_perfil_organizacao)
    EditText edit_text_facebook_perfil_organizacao;
    @BindView(R.id.edit_text_twitter_perfil_organizacao)
    EditText edit_text_twitter_perfil_organizacao;
    @BindView(R.id.edit_text_site_perfil_organizacao)
    EditText edit_text_site_perfil_organizacao;
    @BindView(R.id.edit_text_descricao_perfil_organizacao)
    EditText edit_text_descricao_perfil_organizacao;
    @BindView(R.id.edit_text_senha_perfil_organizacao)
    EditText edit_text_senha_perfil_organizacao;
    @BindView(R.id.edit_text_confirmar_senha_perfil_organizacao)
    EditText edit_text_confirmar_senha_perfil_organizacao;
    @BindView(R.id.button_atualizar_dados_organizacao)
    Button button_atualizar_dados_organizacao;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_organizacao);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_layout_menu_perfil_organizacao);

        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        organizacaoRepository = new OrganizacaoRepository();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }

        if(user != null){
            String uid = user.getUid();
            organizacaoRepository.getOrganizacaoById(uid, new OrganizacaoRepository.OnGetOrganizacaoById() {
                @Override
                public void onGetOrganizacaoByIdSuccess(Organizacao organizacao) {
                    if(organizacao != null){
                        edit_text_razao_social_perfil_organizacao.setText(organizacao.getRazaoSocial());
                        edit_text_nome_fantasia_perfil_organizacao.setText(organizacao.getNomeFantasia());
                        edit_text_cnpj_perfil_organizacao.setText(organizacao.getCnpj());
                        edit_text_nome_responsavel_perfil_organizacao.setText(organizacao.getNomeResponsavel());
                        edit_text_email_perfil_organizacao.setText(organizacao.getEmail());
                        edit_text_telefone_perfil_organizacao.setText(organizacao.getTelefone());
                        edit_text_endereco_perfil_organizacao.setText(organizacao.getEndereco());
                        edit_text_facebook_perfil_organizacao.setText(organizacao.getFacebookAccount());
                        edit_text_twitter_perfil_organizacao.setText(organizacao.getTwitterAccount());
                        edit_text_site_perfil_organizacao.setText(organizacao.getWebSite());
                        edit_text_descricao_perfil_organizacao.setText(organizacao.getDescricao());
                    }else{
                        edit_text_email_perfil_organizacao.setText(user.getEmail());
                    }
                }

                @Override
                public void onGetOrganizacaoByIdError(String error) {
                    Log.d("onGetUserByIdError", error);
                    Toast.makeText(PerfilOrganizacaoActivity.this, "Usuário não existe", Toast.LENGTH_LONG).show();
                }
            });
        }

        button_atualizar_dados_organizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog("Atualizando dados", "Aguarde enquanto os dados são atualizados");
                if(String.valueOf(edit_text_razao_social_perfil_organizacao.getText()).equals("") || String.valueOf(edit_text_nome_fantasia_perfil_organizacao.getText()).equals("") ||
                        String.valueOf(edit_text_cnpj_perfil_organizacao.getText()).equals("") || String.valueOf(edit_text_nome_responsavel_perfil_organizacao.getText()).equals("") ||
                        String.valueOf(edit_text_email_perfil_organizacao.getText()).equals("") || String.valueOf(edit_text_telefone_perfil_organizacao.getText()).equals("")){
                    alertaCamposNaoPreenchidos();
                }else{
                    user = mAuth.getCurrentUser();
                    if(user != null){
                        organizacaoRepository.getOrganizacaoById(user.getUid(), new OrganizacaoRepository.OnGetOrganizacaoById() {
                            @Override
                            public void onGetOrganizacaoByIdSuccess(Organizacao organizacao) {
                                if(organizacao != null){
                                    ong = new Organizacao();
                                    ong.setCnpj(String.valueOf(edit_text_cnpj_perfil_organizacao.getText()));
                                    ong.setDescricao(String.valueOf(edit_text_descricao_perfil_organizacao.getText()));
                                    ong.setEmail(String.valueOf(edit_text_email_perfil_organizacao.getText()));
                                    ong.setFacebookAccount(String.valueOf(edit_text_facebook_perfil_organizacao.getText()));
                                    ong.setNomeFantasia(String.valueOf(edit_text_nome_fantasia_perfil_organizacao.getText()));
                                    ong.setNomeResponsavel(String.valueOf(edit_text_nome_responsavel_perfil_organizacao.getText()));
                                    ong.setTelefone(String.valueOf(edit_text_telefone_perfil_organizacao.getText()));
                                    ong.setTwitterAccount(String.valueOf(edit_text_twitter_perfil_organizacao.getText()));
                                    ong.setWebSite(String.valueOf(edit_text_site_perfil_organizacao.getText()));
                                    ong.setRazaoSocial(String.valueOf(edit_text_razao_social_perfil_organizacao.getText()));
                                    ong.setEndereco(String.valueOf(edit_text_endereco_perfil_organizacao.getText()));
                                    String senha = String.valueOf(edit_text_senha_perfil_organizacao.getText());
                                    String confirmarSenha = String.valueOf(edit_text_confirmar_senha_perfil_organizacao.getText());

                                    if(senha != null){
                                        if(Objects.equals(senha, confirmarSenha)){
                                            if(senha.length() >= 6 && confirmarSenha.length() >= 6){
                                                user.updatePassword(senha).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            edit_text_senha_perfil_organizacao.setText("");
                                                            edit_text_confirmar_senha_perfil_organizacao.setText("");
                                                        }
                                                    }
                                                });
                                            }else{
                                                alertaSenhaCurta();
                                            }
                                        }
                                    }

                                    organizacaoRepository.atualizarOrganizacao(ong, user);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PerfilOrganizacaoActivity.this, R.style.AppTheme));
                                    builder.setMessage("Organização atualizada com sucesso!");
                                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int i) {
                                            hideProgressDialog();
                                            abrirMenuPerfilOrganizacao();
                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            }

                            @Override
                            public void onGetOrganizacaoByIdError(String error) {
                                Log.d("onGetUserByIdError", error);
                                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PerfilOrganizacaoActivity.this, R.style.AppTheme));
                                builder.setMessage("Não foi possível atualizar os dados da organização. Por favor, tente novamente!");
                                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        hideProgressDialog();
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

    private void alertaCamposNaoPreenchidos() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PerfilOrganizacaoActivity.this, R.style.AppTheme));
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
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PerfilOrganizacaoActivity.this, R.style.AppTheme));
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

    private void abrirMenuPerfilOrganizacao() {
        Intent intent = new Intent(PerfilOrganizacaoActivity.this, PerfilOrganizacaoActivity.class);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                abrirMenuPrincipal();
                break;
            case R.id.item_exit_menu_principal:
                sairDoApp();
                break;
        }
        return true;
    }

    private void abrirMenuPrincipal() {
        Intent intent = new Intent(PerfilOrganizacaoActivity.this, MenuPrincipalOrganizacaoActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_perfil, menu);

        return super.onCreateOptionsMenu(menu);
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
}
