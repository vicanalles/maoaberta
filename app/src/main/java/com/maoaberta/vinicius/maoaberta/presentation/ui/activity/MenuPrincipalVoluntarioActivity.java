package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Voluntario;
import com.maoaberta.vinicius.maoaberta.domain.repository.UsuarioRepository;
import com.maoaberta.vinicius.maoaberta.presentation.ui.adapter.TabsPagerAdapterCliente;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinicius on 02/09/2017.
 */

public class MenuPrincipalVoluntarioActivity extends AppCompatActivity{

    private FirebaseAuth mAuth = null;
    private UsuarioRepository usuarioRepository;
    private FirebaseUser user;

    @BindView(R.id.toolbar_layout_menu_cliente)
    Toolbar toolbar_layout_menu_cliente;
    @BindView(R.id.tab_layout_menu_principal_cliente)
    TabLayout tab_layout_menu_principal_cliente;
    @BindView(R.id.pager_menu_principal_cliente)
    ViewPager pager_menu_principal_cliente;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_menu_principal_cliente);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_layout_menu_cliente);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        usuarioRepository = new UsuarioRepository();

        final String[] tabTitles = {
                "Meus Interesses",
                getString(R.string.anuncios_tab_title),
                getString(R.string.entidades_tab_title)
        };

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }

        pager_menu_principal_cliente.setAdapter(new TabsPagerAdapterCliente(getSupportFragmentManager(), this));
        tab_layout_menu_principal_cliente.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.textColorWhite)));
        tab_layout_menu_principal_cliente.setSelectedTabIndicatorColor(getResources().getColor(R.color.textColorWhite));
        tab_layout_menu_principal_cliente.setupWithViewPager(pager_menu_principal_cliente);

        for(int i = 0; i < tabTitles.length; i++){
            tab_layout_menu_principal_cliente.getTabAt(i).setText(tabTitles[i]);
        }

        if(user != null){
            String uid = user.getUid();
            usuarioRepository.getUserByUid(uid, new UsuarioRepository.OnGetUserById() {
                @Override
                public void onGetUserByIdSuccess(Voluntario voluntario) {
                    if(voluntario != null){
                        Voluntario vol = new Voluntario();
                        vol.setNome(voluntario.getNome());
                        toolbar_layout_menu_cliente.setTitle(R.string.app_name);
                    }else{
                        toolbar_layout_menu_cliente.setTitle(R.string.app_name);
                        LinearLayout tabStrip = ((LinearLayout)tab_layout_menu_principal_cliente.getChildAt(0));
                        for(int i = 0; i < tabStrip.getChildCount(); i++) {
                            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    Toast.makeText(MenuPrincipalVoluntarioActivity.this, R.string.preencha_todos_campos, Toast.LENGTH_SHORT).show();
                                    return true;
                                }
                            });
                        }
                    }
                }

                @Override
                public void onGetUserByIdError(String error) {
                    Log.d("onGetUserByIdError", error);
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
                abrirTelaPerfilVoluntario();
                break;
            case R.id.item_sobre:
                abrirTelaSobre();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void abrirTelaSobre() {
        Intent intent = new Intent(MenuPrincipalVoluntarioActivity.this, SobreActivity.class);
        startActivity(intent);
    }

    private void abrirTelaPerfilVoluntario() {
        Intent intent = new Intent(MenuPrincipalVoluntarioActivity.this, PerfilVoluntarioActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        sairDoApp();
    }

    private void sairDoApp() {
        @SuppressLint("RestrictedApi") AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme));
        builder.setMessage(R.string.sair_app);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
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
