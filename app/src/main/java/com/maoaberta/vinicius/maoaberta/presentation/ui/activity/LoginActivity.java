package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.presentation.component.CustomViewPager;
import com.maoaberta.vinicius.maoaberta.presentation.ui.adapter.ViewPagerAdapterLogin;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinicius on 12/08/2017.
 */

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.pager_login)
    CustomViewPager pager_login;
    @BindView(R.id.tabs_login)
    TabLayout tabs_login;
    @BindView(R.id.toolbar_layout_login)
    Toolbar toolbar_layout_login;

    ViewPagerAdapterLogin adapter;
    CharSequence titles[] = {"Voluntário", "Organização"};
    TextView tv;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_layout_login);

        adapter = new ViewPagerAdapterLogin(getSupportFragmentManager(), titles);

        pager_login.setAdapter(adapter);
        pager_login.setPagingEnabled(false);
        tabs_login.setSelectedTabIndicatorColor(getResources().getColor(R.color.tabLayoutBottomColor));
        tabs_login.setupWithViewPager(pager_login);

        for(int i = 0; i < tabs_login.getTabCount(); i++){
            tv = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tv.setTypeface(Typeface.SANS_SERIF);
            tabs_login.getTabAt(i).setCustomView(tv);
        }

        //Criação do texto cliente na cor branco para iniciar nas tabs
        final SpannableStringBuilder cliente = new SpannableStringBuilder();
        SpannableString spannableCliente = new SpannableString(titles[0]);
        spannableCliente.setSpan(new ForegroundColorSpan(Color.WHITE), 0, titles[0].length(), 0);
        cliente.append(spannableCliente);
        tabs_login.getTabAt(0).setText(cliente);

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
        tabs_login.getTabAt(1).setText(organizacao);

        tabs_login.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager_login.setCurrentItem(tab.getPosition());
                String texto = String.valueOf(tab.getText()); //pega o texto de acordo com a posição da tab
                SpannableStringBuilder builder = new SpannableStringBuilder(); //cria um spannableBuilder
                SpannableString spannableString = new SpannableString(texto); //cria um spannableString
                spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, texto.length(), 0); //define a nova cor
                builder.append(spannableString); //adiciona o novo texto ao builder
                if (tab.getPosition() == 0) {
                    tabs_login.getTabAt(0).setText(builder);
                    tabs_login.getTabAt(1).setText(organizacao);
                } else if (tab.getPosition() == 1) {
                    tabs_login.getTabAt(0).setText(clienteBlack);
                    tabs_login.getTabAt(1).setText(builder);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager_login.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    hideKeyboard(view);
                }
            }
        });

        progressDialog = new ProgressDialog(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.item_esqueceu_sua_senha:
                abrirEsqueceuSuaSenha();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void abrirEsqueceuSuaSenha(){
        Intent intent = new Intent(getApplicationContext(), EsqueceuSenhaActivity.class);
        startActivity(intent);
    }

    public void hideKeyboard(View v){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void showProgressDialog(String title, String content){
        progressDialog.setTitle(title);
        progressDialog.setMessage(content);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideProgressDialog(){
        progressDialog.dismiss();
    }

}
