package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.presentation.component.CustomViewPager;
import com.maoaberta.vinicius.maoaberta.presentation.ui.adapter.ViewPagerAdapterCadastro;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinicius Canalles on 16/08/2017.
 */

public class CadastroActivity extends AppCompatActivity {

    @BindView(R.id.pager_cadastro)
    CustomViewPager pager;
    @BindView(R.id.tabs_cadastro)
    TabLayout tabs;
    @BindView(R.id.toolbar_layout_cadastro)
    Toolbar toolbar;

    ViewPagerAdapterCadastro adapter;
    CharSequence titles[] = {"Cliente", "Organização"};
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        // Creating The ViewPagerAdapterCadastro and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapterCadastro(getSupportFragmentManager(), titles);

        // Assigning ViewPager View and setting the adapter
        pager.setAdapter(adapter);
        pager.setPagingEnabled(false);
        // Assiging the Sliding Tab Layout View
        tabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.tabLayoutBottomColor));
        tabs.setupWithViewPager(pager);

        //Definição da fonte e da cor do texto das tabs
        for (int i = 0; i < tabs.getTabCount(); i++) {
            tv = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tv.setTypeface(Typeface.SANS_SERIF);
            tabs.getTabAt(i).setCustomView(tv);
        }

        //Criação do texto cliente na cor branco para iniciar nas tabs
        final SpannableStringBuilder cliente = new SpannableStringBuilder();
        SpannableString spannableCliente = new SpannableString(titles[0]);
        spannableCliente.setSpan(new ForegroundColorSpan(Color.WHITE), 0, titles[0].length(), 0);
        cliente.append(spannableCliente);
        tabs.getTabAt(0).setText(cliente);

        //Criação do texto cliente em preto para ser usado após a primeira troca de tabs
        final SpannableStringBuilder clienteBlack = new SpannableStringBuilder();
        SpannableString spannableClienteBlack = new SpannableString(titles[0]);
        spannableClienteBlack.setSpan(new ForegroundColorSpan(Color.BLACK), 0, titles[0].length(), 0);
        clienteBlack.append(spannableClienteBlack);

        //Criação do texto organização em preto para ser usado no início das tabs
        final SpannableStringBuilder organizacao = new SpannableStringBuilder();
        SpannableString spannableOrganizacao = new SpannableString(titles[1]);
        spannableOrganizacao.setSpan(new ForegroundColorSpan(Color.BLACK), 0, titles[1].length(), 0);
        organizacao.append(spannableOrganizacao);
        tabs.getTabAt(1).setText(organizacao);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
                String texto = String.valueOf(tab.getText()); //pega o texto de acordo com a posição da tab
                SpannableStringBuilder builder = new SpannableStringBuilder(); //cria um spannableBuilder
                SpannableString spannableString = new SpannableString(texto); //cria um spannableString
                spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, texto.length(), 0); //define a nova cor
                builder.append(spannableString); //adiciona o novo texto ao builder
                if (tab.getPosition() == 0) {
                    tabs.getTabAt(0).setText(builder);
                    tabs.getTabAt(1).setText(organizacao);
                } else if (tab.getPosition() == 1) {
                    tabs.getTabAt(0).setText(clienteBlack);
                    tabs.getTabAt(1).setText(builder);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.item_configuracoes:
                abrirConfiguracoes();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void abrirConfiguracoes(){
        startActivity(new Intent(Settings.ACTION_SETTINGS));
    }
}
