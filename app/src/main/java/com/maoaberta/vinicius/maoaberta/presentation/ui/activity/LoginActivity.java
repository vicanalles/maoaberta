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
import com.maoaberta.vinicius.maoaberta.presentation.ui.adapter.ViewPagerAdapterLogin;

/**
 * Created by Vinicius on 12/08/2017.
 */

public class LoginActivity extends AppCompatActivity {

    ViewPager pagerLogin;
    ViewPagerAdapterLogin adapterLogin;
    TabLayout tabsLogin;
    CharSequence titlesLogin[]={"Cliente","Organização"};
    int numbOftabsLogin =2;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Creating The ViewPagerAdapterCadastro and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapterLogin =  new ViewPagerAdapterLogin(getSupportFragmentManager(), titlesLogin, numbOftabsLogin);

        // Assigning ViewPager View and setting the adapterLogin
        pagerLogin = (ViewPager) findViewById(R.id.pager_login);
        pagerLogin.setAdapter(adapterLogin);

        // Assiging the Sliding Tab Layout View
        tabsLogin = (TabLayout) findViewById(R.id.tabs_login);
        tabsLogin.setSelectedTabIndicatorColor(getResources().getColor(R.color.tabLayoutBottomColor));
        tabsLogin.setupWithViewPager(pagerLogin);

        //Definição da fonte e da cor do texto das tabsLogin
        for(int i = 0; i < tabsLogin.getTabCount(); i++){
            tv = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tv.setTypeface(Typeface.SANS_SERIF);
            tabsLogin.getTabAt(i).setCustomView(tv);
        }

        //Criação do texto cliente na cor branco para iniciar nas tabsLogin
        final SpannableStringBuilder cliente = new SpannableStringBuilder();
        SpannableString spannableCliente = new SpannableString(titlesLogin[0]);
        spannableCliente.setSpan(new ForegroundColorSpan(Color.WHITE), 0, titlesLogin[0].length(), 0);
        cliente.append(spannableCliente);
        tabsLogin.getTabAt(0).setText(cliente);

        //Criação do texto cliente em preto para ser usado após a primeira troca de tabsLogin
        final SpannableStringBuilder clienteBlack = new SpannableStringBuilder();
        SpannableString spannableClienteBlack = new SpannableString(titlesLogin[0]);
        spannableClienteBlack.setSpan(new ForegroundColorSpan(Color.BLACK), 0, titlesLogin[0].length(), 0);
        clienteBlack.append(spannableClienteBlack);

        //Criação do texto organização em preto para ser usado no início das tabsLogin
        final SpannableStringBuilder organizacao = new SpannableStringBuilder();
        SpannableString spannableOrganizacao = new SpannableString(titlesLogin[1]);
        spannableOrganizacao.setSpan(new ForegroundColorSpan(Color.BLACK), 0, titlesLogin[1].length(), 0);
        organizacao.append(spannableOrganizacao);
        tabsLogin.getTabAt(1).setText(organizacao);

        tabsLogin.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pagerLogin.setCurrentItem(tab.getPosition());
                String texto = String.valueOf(tab.getText()); //pega o texto de acordo com a posição da tab
                SpannableStringBuilder builder = new SpannableStringBuilder(); //cria um spannableBuilder
                SpannableString spannableString = new SpannableString(texto); //cria um spannableString
                spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, texto.length(), 0); //define a nova cor
                builder.append(spannableString); //adiciona o novo texto ao builder
                if(tab.getPosition() == 0) {
                    tabsLogin.getTabAt(0).setText(builder);
                    tabsLogin.getTabAt(1).setText(organizacao);
                }else if(tab.getPosition() == 1){
                    tabsLogin.getTabAt(0).setText(clienteBlack);
                    tabsLogin.getTabAt(1).setText(builder);
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
