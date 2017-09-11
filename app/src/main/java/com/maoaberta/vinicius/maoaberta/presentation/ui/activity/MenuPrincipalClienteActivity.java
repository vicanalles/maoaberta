package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.presentation.component.CustomViewPager;
import com.maoaberta.vinicius.maoaberta.presentation.ui.adapter.TabsPagerAdapterCliente;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinicius on 02/09/2017.
 */

public class MenuPrincipalClienteActivity extends AppCompatActivity{

    private FirebaseAuth mAuth = null;

    @BindView(R.id.toolbar_layout_menu_cliente)
    Toolbar toolbar_layout_menu_cliente;
    @BindView(R.id.tab_layout_menu_principal_cliente)
    TabLayout tab_layout_menu_principal_cliente;
    @BindView(R.id.pager_menu_principal_cliente)
    CustomViewPager pager_menu_principal_cliente;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal_cliente);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_layout_menu_cliente);

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String nome = intent.getStringExtra("userName");
        toolbar_layout_menu_cliente.setTitle(nome);

        final String[] tabTitles = {
                "ANÚNCIOS",
                "ENTIDADES",
                "MEUS ANÚNCIOS",
                "PERFIL"
        };

        pager_menu_principal_cliente.setPagingEnabled(false);
        pager_menu_principal_cliente.setAdapter(new TabsPagerAdapterCliente(getSupportFragmentManager(), this));
        tab_layout_menu_principal_cliente.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.textColorWhite)));
        tab_layout_menu_principal_cliente.setSelectedTabIndicatorColor(getResources().getColor(R.color.textColorWhite));
        tab_layout_menu_principal_cliente.setupWithViewPager(pager_menu_principal_cliente);

        for(int i = 0; i < tabTitles.length; i++){
            tab_layout_menu_principal_cliente.getTabAt(i).setText(tabTitles[i]);
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

            case R.id.item_configuracoes_menu_principal:
                abrirConfiguracoes();
                break;
            case R.id.item_exit_menu_principal:
                sairDoApp();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void abrirConfiguracoes(){
        startActivity(new Intent(Settings.ACTION_SETTINGS));
    }

    @Override
    public void onBackPressed() {
        sairDoApp();
    }

    private void sairDoApp() {
        FirebaseAuth.getInstance().signOut();
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme));
        builder.setMessage(R.string.sair_app);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
