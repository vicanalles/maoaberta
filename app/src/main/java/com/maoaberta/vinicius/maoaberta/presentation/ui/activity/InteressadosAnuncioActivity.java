package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.maoaberta.vinicius.maoaberta.domain.models.Voluntario;
import com.maoaberta.vinicius.maoaberta.domain.repository.InteressadosRepository;
import com.maoaberta.vinicius.maoaberta.domain.repository.InteressesRepository;
import com.maoaberta.vinicius.maoaberta.domain.repository.OrganizacaoRepository;
import com.maoaberta.vinicius.maoaberta.domain.repository.TipoRepository;
import com.maoaberta.vinicius.maoaberta.domain.repository.UsuarioRepository;
import com.maoaberta.vinicius.maoaberta.presentation.ui.adapter.InteressadosAnuncioAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinicius on 16/11/2017.
 */

public class InteressadosAnuncioActivity extends AppCompatActivity{

    @BindView(R.id.toolbar_interesses_anuncio)
    Toolbar toolbar_interesses_anuncio;
    @BindView(R.id.recycler_view_interesses_anuncio)
    RecyclerView mRecycler;

    private Anuncio anuncio;
    private InteressadosAnuncioAdapter mAdapter;
    private List<Voluntario> voluntarios;
    private List<Organizacao> organizacoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interessados_anuncio);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_interesses_anuncio);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            anuncio = (Anuncio) extras.get("anuncio");
        }

        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.app_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }

        voluntarios = new ArrayList<>();
        organizacoes = new ArrayList<>();

        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new InteressadosAnuncioAdapter(this);
        mRecycler.setAdapter(mAdapter);

        InteressesRepository interessesRepository = new InteressesRepository();
        interessesRepository.getInteressadosAnuncio(anuncio, new InteressesRepository.OnGetInteressadosAnuncio() {
            @Override
            public void onGetInteressadosAnuncioSuccess(final List<String> voluntarios) {
                mAdapter.setItems(voluntarios);
            }

            @Override
            public void onGetInteressadosAnuncioError(DatabaseError databaseError) {

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

            case android.R.id.home:
                abrirMenuPrincipalOrganizacao();
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

    private void abrirMenuPrincipalOrganizacao() {
        Intent intent = new Intent(InteressadosAnuncioActivity.this, MenuPrincipalOrganizacaoActivity.class);
        startActivity(intent);
        finish();
    }
}
