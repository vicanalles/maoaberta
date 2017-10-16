package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Voluntario;
import com.maoaberta.vinicius.maoaberta.domain.repository.TipoRepository;
import com.maoaberta.vinicius.maoaberta.domain.repository.UsuarioRepository;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vinicius on 29/08/17.
 */

public class ApresentacaoActivity extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TipoRepository tipoRepository;
    private UsuarioRepository usuarioRepository;

    @BindView(R.id.toolbar_layout_sobre)
    Toolbar toolbar_layout_sobre;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apresentacao);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_layout_sobre);

        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        usuarioRepository = new UsuarioRepository();
        tipoRepository = new TipoRepository();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    showProgressDialog("Aguarde", "Obtendo informações do Usuário");
                    usuarioRepository.getUserByUid(user.getUid(), new UsuarioRepository.OnGetUserById() {
                        @Override
                        public void onGetUserByIdSuccess(Voluntario voluntario) {
                            if(voluntario != null){
                                tipoRepository.getTipoById(user.getUid(), new TipoRepository.OnGetTipoById() {
                                    @Override
                                    public void onGetTipoByIdSuccess(String tipo) {
                                        if(tipo != null){
                                            if(tipo.equals("voluntario")){
                                                abrirMenuPrincipalCliente();
                                            }else{
                                                abrirMenuPrincipalOrganizacao();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onGetTipoByIdError(String error) {
                                        Toast.makeText(ApresentacaoActivity.this, "Erro na recuperação dos dados", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onGetUserByIdError(String error) {
                            Toast.makeText(ApresentacaoActivity.this, "Erro na recuperação dos dados", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tela_sobre, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.item_login:
                abrirTelaLogin();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void abrirTelaLogin(){
        Intent intent = new Intent(ApresentacaoActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirMenuPrincipalCliente(){
        hideProgressDialog();
        Intent intent = new Intent(getApplicationContext(), MenuPrincipalClienteActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirMenuPrincipalOrganizacao(){
        hideProgressDialog();
        Intent intent = new Intent(getApplicationContext(), MenuPrincipalOrganizacaoActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
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
