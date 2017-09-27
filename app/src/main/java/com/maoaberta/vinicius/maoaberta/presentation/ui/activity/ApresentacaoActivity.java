package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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
    private String codigoAtivaCampos;

    @BindView(R.id.toolbar_layout_sobre)
    Toolbar toolbar_layout_sobre;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apresentacao);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_layout_sobre);

        mAuth = FirebaseAuth.getInstance();
        tipoRepository = new TipoRepository();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    final String provider = String.valueOf(user.getProviders());
                    String uid = user.getUid();
                    tipoRepository.getTipoById(uid, new TipoRepository.OnGetTipoById() {
                        @Override
                        public void onGetTipoByIdSuccess(String tipo) {
                            if(tipo != null){
                                if(tipo.equals("organizaçao")){
                                    abrirMenuPrincipalOrganizacao();
                                }else{
                                    if(provider.equals("[google.com]")){
                                        codigoAtivaCampos = "2";
                                    }else{
                                        codigoAtivaCampos = "1";
                                    }
                                    abrirMenuPrincipalCliente(codigoAtivaCampos);
                                }
                            }else{

                            }
                        }

                        @Override
                        public void onGetTipoByIdError(String error) {

                        }
                    });
                } else {

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

    public void abrirMenuPrincipalCliente(String codigoAtivaCampos){
        Intent intent = new Intent(getApplicationContext(), MenuPrincipalClienteActivity.class);
        intent.putExtra("codigo", codigoAtivaCampos);
        startActivity(intent);
        finish();
    }

    public void abrirMenuPrincipalOrganizacao(){
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
}
