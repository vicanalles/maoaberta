package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.presentation.component.CustomViewPager;
import com.maoaberta.vinicius.maoaberta.presentation.ui.adapter.TabsPagerAdapterOrganizacao;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinicius on 04/09/2017.
 */

public class MenuPrincipalOrganizacaoActivity extends AppCompatActivity {

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

        final String[] tabTitles = {
                "ANÚNCIOS",
                "ENTIDADES",
                "MEUS ANÚNCIOS",
                "PERFIL"
        };

        pager_menu_principal_organizacao.setPagingEnabled(false);
        pager_menu_principal_organizacao.setAdapter(new TabsPagerAdapterOrganizacao(getSupportFragmentManager(), this));
        tab_layout_menu_principal_organizacao.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.textColorWhite)));
        tab_layout_menu_principal_organizacao.setSelectedTabIndicatorColor(getResources().getColor(R.color.textColorWhite));
        tab_layout_menu_principal_organizacao.setupWithViewPager(pager_menu_principal_organizacao);

        for(int i = 0; i < tabTitles.length; i++){
            tab_layout_menu_principal_organizacao.getTabAt(i).setText(tabTitles[i]);
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
                Toast.makeText(this, "Sair do Aplicativo", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void abrirConfiguracoes(){
        startActivity(new Intent(Settings.ACTION_SETTINGS));
    }
}
