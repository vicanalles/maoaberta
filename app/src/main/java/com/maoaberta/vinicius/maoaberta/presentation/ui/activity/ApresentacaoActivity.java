package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.repository.TipoRepository;
import com.maoaberta.vinicius.maoaberta.presentation.ui.adapter.ViewPagerConteudoSobreAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vinicius on 29/08/17.
 */

public class ApresentacaoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TipoRepository tipoRepository;

    @BindView(R.id.toolbar_layout_sobre)
    Toolbar toolbar_layout_sobre;
    @BindView(R.id.view_pager_conteudo_sobre)
    ViewPager view_pager_conteudo_sobre;

    private ProgressDialog progressDialog;
    private ViewPagerConteudoSobreAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apresentacao);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_layout_sobre);

        progressDialog = new ProgressDialog(this);

        adapter = new ViewPagerConteudoSobreAdapter(getSupportFragmentManager());

        view_pager_conteudo_sobre.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        tipoRepository = new TipoRepository();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    showProgressDialog("Aguarde", "Obtendo informações do Usuário");
                    tipoRepository.getTipoById(user.getUid(), new TipoRepository.OnGetTipoById() {
                        @Override
                        public void onGetTipoByIdSuccess(String tipo) {
                            if (tipo != null) {
                                if (tipo.equals("voluntario")) {
                                    abrirMenuPrincipalCliente();
                                } else {
                                    abrirMenuPrincipalOrganizacao();
                                }
                            } else {
                                hideProgressDialog();
                                abrirTelaLogin();
                            }
                        }

                        @Override
                        public void onGetTipoByIdError(String error) {
                            hideProgressDialog();
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
        switch (item.getItemId()) {

            case R.id.item_login:
                abrirTelaLogin();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void abrirTelaLogin() {
        Intent intent = new Intent(ApresentacaoActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirMenuPrincipalCliente() {
        hideProgressDialog();
        Intent intent = new Intent(getApplicationContext(), MenuPrincipalVoluntarioActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirMenuPrincipalOrganizacao() {
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

    public void showProgressDialog(String title, String content) {
        progressDialog.setTitle(title);
        progressDialog.setMessage(content);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideProgressDialog() {
        progressDialog.dismiss();
    }
}
