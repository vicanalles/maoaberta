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
import com.maoaberta.vinicius.maoaberta.presentation.ui.adapter.ViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinicius Canalles on 16/08/2017.
 */

public class CadastroActivity extends AppCompatActivity {

    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.text_view_toolbar)
    TextView text_view_toolbar_cadastro;

    ViewPagerAdapter adapter;
    CharSequence titles[] = {"Cliente", "Organização"};
    int numbOftabs = 2;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        ButterKnife.bind(this);

        //Define o texto da toolbar
        text_view_toolbar_cadastro.setText(R.string.cadastro);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, numbOftabs);

        // Assigning ViewPager View and setting the adapter
        pager.setAdapter(adapter);

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
}
