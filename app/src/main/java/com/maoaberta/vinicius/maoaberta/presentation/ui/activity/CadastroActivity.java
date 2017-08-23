package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.presentation.ui.adapter.ViewPagerAdapterCadastro;

/**
 * Created by Vinicius Canalles on 16/08/2017.
 */

public class CadastroActivity extends AppCompatActivity {

    ViewPager pagerCadastro;
    ViewPagerAdapterCadastro adapterCadastro;
    TabLayout tabsCadastro;
    CharSequence titlesCadastro[]={"Cliente","Organização"};
    int numbOftabsCadastro =2;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // Creating The ViewPagerAdapterCadastro and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapterCadastro =  new ViewPagerAdapterCadastro(getSupportFragmentManager(), titlesCadastro, numbOftabsCadastro);

        // Assigning ViewPager View and setting the adapterLogin
        pagerCadastro = (ViewPager) findViewById(R.id.pager_cadastro);
        pagerCadastro.setAdapter(adapterCadastro);

        // Assiging the Sliding Tab Layout View
        tabsCadastro = (TabLayout) findViewById(R.id.tabs_cadastro);
        tabsCadastro.setSelectedTabIndicatorColor(getResources().getColor(R.color.tabLayoutBottomColor));
        tabsCadastro.setupWithViewPager(pagerCadastro);

        //Definição da fonte e da cor do texto das tabsLogin
        for(int i = 0; i < tabsCadastro.getTabCount(); i++){
            tv = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tv.setTypeface(Typeface.SANS_SERIF);
            tabsCadastro.getTabAt(i).setCustomView(tv);
        }

        //Criação do texto cliente na cor branco para iniciar nas tabsLogin
        final SpannableStringBuilder cliente = new SpannableStringBuilder();
        SpannableString spannableCliente = new SpannableString(titlesCadastro[0]);
        spannableCliente.setSpan(new ForegroundColorSpan(Color.WHITE), 0, titlesCadastro[0].length(), 0);
        cliente.append(spannableCliente);
        tabsCadastro.getTabAt(0).setText(cliente);

        //Criação do texto cliente em preto para ser usado após a primeira troca de tabsLogin
        final SpannableStringBuilder clienteBlack = new SpannableStringBuilder();
        SpannableString spannableClienteBlack = new SpannableString(titlesCadastro[0]);
        spannableClienteBlack.setSpan(new ForegroundColorSpan(Color.BLACK), 0, titlesCadastro[0].length(), 0);
        clienteBlack.append(spannableClienteBlack);

        //Criação do texto organização em preto para ser usado no início das tabsLogin
        final SpannableStringBuilder organizacao = new SpannableStringBuilder();
        SpannableString spannableOrganizacao = new SpannableString(titlesCadastro[1]);
        spannableOrganizacao.setSpan(new ForegroundColorSpan(Color.BLACK), 0, titlesCadastro[1].length(), 0);
        organizacao.append(spannableOrganizacao);
        tabsCadastro.getTabAt(1).setText(organizacao);

        tabsCadastro.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pagerCadastro.setCurrentItem(tab.getPosition());
                String texto = String.valueOf(tab.getText()); //pega o texto de acordo com a posição da tab
                SpannableStringBuilder builder = new SpannableStringBuilder(); //cria um spannableBuilder
                SpannableString spannableString = new SpannableString(texto); //cria um spannableString
                spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, texto.length(), 0); //define a nova cor
                builder.append(spannableString); //adiciona o novo texto ao builder
                if(tab.getPosition() == 0) {
                    tabsCadastro.getTabAt(0).setText(builder);
                    tabsCadastro.getTabAt(1).setText(organizacao);
                }else if(tab.getPosition() == 1){
                    tabsCadastro.getTabAt(0).setText(clienteBlack);
                    tabsCadastro.getTabAt(1).setText(builder);
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
}
