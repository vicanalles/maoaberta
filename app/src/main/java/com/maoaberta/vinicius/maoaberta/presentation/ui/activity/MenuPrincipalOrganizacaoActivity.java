package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Organizacao;
import com.maoaberta.vinicius.maoaberta.domain.repository.OrganizacaoRepository;
import com.maoaberta.vinicius.maoaberta.presentation.component.CustomViewPager;
import com.maoaberta.vinicius.maoaberta.presentation.ui.adapter.TabsPagerAdapterOrganizacao;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinicius on 04/09/2017.
 */

public class MenuPrincipalOrganizacaoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = null;
    private OrganizacaoRepository organizacaoRepository;
    private FirebaseUser user;

    @BindView(R.id.toolbar_layout_menu_organizacao)
    Toolbar toolbar_layout_menu_organizacao;
    @BindView(R.id.pager_menu_principal_organizacao)
    CustomViewPager pager_menu_principal_organizacao;
    @BindView(R.id.tab_layout_menu_principal_organizacao)
    TabLayout tab_layout_menu_principal_organizacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal_organizacao);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_layout_menu_organizacao);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        organizacaoRepository = new OrganizacaoRepository();

        final String[] tabTitles = {
                getString(R.string.meus_anuncios_tab_title),
                getString(R.string.anuncios_tab_title),
                getString(R.string.entidades_tab_title),
                getString(R.string.sobre_tab_title)
        };

        pager_menu_principal_organizacao.setPagingEnabled(false);
        pager_menu_principal_organizacao.setAdapter(new TabsPagerAdapterOrganizacao(getSupportFragmentManager(), this));
        tab_layout_menu_principal_organizacao.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.textColorWhite)));
        tab_layout_menu_principal_organizacao.setSelectedTabIndicatorColor(getResources().getColor(R.color.textColorWhite));
        tab_layout_menu_principal_organizacao.setupWithViewPager(pager_menu_principal_organizacao);

        for(int i = 0; i < tabTitles.length; i++){
            tab_layout_menu_principal_organizacao.getTabAt(i).setText(tabTitles[i]);
        }

        if(user != null){
            String uid = user.getUid();
            organizacaoRepository.getOrganizacaoById(uid, new OrganizacaoRepository.OnGetOrganizacaoById() {
                @Override
                public void onGetOrganizacaoByIdSuccess(Organizacao organizacao) {
                    if(organizacao != null){
                        Organizacao ong = new Organizacao();
                        ong.setNomeFantasia(organizacao.getNomeFantasia());
                        toolbar_layout_menu_organizacao.setTitle(R.string.app_name);
                    }else{
                        toolbar_layout_menu_organizacao.setTitle(user.getDisplayName());
                        LinearLayout tabStrip = ((LinearLayout)tab_layout_menu_principal_organizacao.getChildAt(0));
                        for(int i = 0; i < tabStrip.getChildCount(); i++) {
                            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    Toast.makeText(MenuPrincipalOrganizacaoActivity.this, R.string.preencha_todos_campos, Toast.LENGTH_LONG).show();
                                    return true;
                                }
                            });
                        }
                    }
                }

                @Override
                public void onGetOrganizacaoByIdError(String error) {
                    Log.d("onGetUserByIdError", error);
                    Toast.makeText(getApplicationContext(), R.string.usuario_inexistente, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.item_exit_menu_principal:
                sairDoApp();
                break;
            case R.id.item_perfil:
                abrirPerfilOrganizacao();
        }

        return super.onOptionsItemSelected(item);
    }

    private void abrirPerfilOrganizacao() {
        Intent intent = new Intent(MenuPrincipalOrganizacaoActivity.this, PerfilOrganizacaoActivity.class);
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
