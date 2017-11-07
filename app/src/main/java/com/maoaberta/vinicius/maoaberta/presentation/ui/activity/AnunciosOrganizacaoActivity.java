package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Anuncio;
import com.maoaberta.vinicius.maoaberta.domain.models.Organizacao;
import com.maoaberta.vinicius.maoaberta.domain.repository.AnuncioRepository;
import com.maoaberta.vinicius.maoaberta.presentation.ui.adapter.AnunciosOrganizacaoAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vinicius on 06/11/17.
 */

public class AnunciosOrganizacaoActivity extends AppCompatActivity{

    @BindView(R.id.toolbar_anuncios_organizacao)
    Toolbar toolbar_anuncios_organizacao;
    @BindView(R.id.recycler_view_anuncios_organizacao)
    RecyclerView mRecyclerView;

    private Organizacao organizacao;
    private List<Anuncio> mAnuncios;
    private AnuncioRepository anuncioRepository;
    private AnunciosOrganizacaoAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncios_organizacao);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_anuncios_organizacao);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            organizacao = (Organizacao) extras.get("organizacao");
        }

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.app_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }

        mAnuncios = new ArrayList<>();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AnunciosOrganizacaoAdapter(this, organizacao);
        mRecyclerView.setAdapter(mAdapter);

        anuncioRepository = new AnuncioRepository();
        anuncioRepository.getAllAnunciosOrganizacao(organizacao, new AnuncioRepository.OnGetAllAnuncios() {
            @Override
            public void onGetAllAnunciosSuccess(List<Anuncio> anuncios) {
                mAdapter.setItems(anuncios);
            }

            @Override
            public void onGetAllAnunciosError(DatabaseError erro) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home:
                finish();
                break;
            case R.id.item_exit_menu_principal:
                sairDoApp();
        }
        return true;
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

    private void abrirTelaLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_perfil, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
