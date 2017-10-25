package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Anuncio;
import com.maoaberta.vinicius.maoaberta.domain.repository.AnuncioRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vinicius on 19/10/17.
 */

public class CriacaoAnunciosActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_layout_menu_novo_ad)
    Toolbar toolbar_layout_menu_novo_ad;
    @BindView(R.id.text_view_novo_ad)
    TextView text_view_novo_ad;
    @BindView(R.id.edit_text_titulo_ad)
    EditText edit_text_titulo_ad;
    @BindView(R.id.text_view_start_date)
    TextView text_view_start_date;
    @BindView(R.id.edit_text_start_date)
    EditText edit_text_start_date;
    @BindView(R.id.edit_text_descricao_criacao_anuncio)
    EditText edit_text_descricao_criacao_anuncio;
    @BindView(R.id.text_view_end_date)
    TextView text_view_end_date;
    @BindView(R.id.edit_text_end_date)
    EditText edit_text_end_date;
    @BindView(R.id.spinner_novo_ad)
    Spinner spinner_novo_ad;
    @BindView(R.id.botao_criar_novo_anuncio)
    Button botao_criar_novo_anuncio;

    private int mes, dia, ano;
    private Date startDate;
    private Date endDate;
    private Anuncio anuncio;
    private AnuncioRepository anuncioRepository;
    private ProgressDialog progressDialog;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private Date currentTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criacao_anuncios);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_layout_menu_novo_ad);

        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }

        String[] items = new String[]{"Produto", "Serviço"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner_novo_ad.setAdapter(adapter);

        edit_text_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                ano = c.get(Calendar.YEAR);
                mes = c.get(Calendar.MONTH);
                dia = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CriacaoAnunciosActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int ano, int mes, int dia) {
                        edit_text_start_date.setText(dia + "/" + (mes + 1) + "/" + ano);
                    }
                }, ano, mes, dia);
                datePickerDialog.show();
            }
        });

        edit_text_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                ano = c.get(Calendar.YEAR);
                mes = c.get(Calendar.MONTH);
                dia = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CriacaoAnunciosActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int ano, int mes, int dia) {
                        edit_text_end_date.setText(dia + "/" + (mes + 1) + "/" + ano);
                    }
                }, ano, mes, dia);
                datePickerDialog.show();
            }
        });

        botao_criar_novo_anuncio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edit_text_titulo_ad.getText().toString().equals("") && !edit_text_start_date.getText().toString().equals("") &&
                        !edit_text_end_date.getText().toString().equals("")) {
                    DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        startDate = format.parse(edit_text_start_date.getText().toString());
                        endDate = format.parse(edit_text_end_date.getText().toString());
                        currentTime = format.parse(format.format(new Date()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (startDate.after(endDate)) {
                        alertaDataInicioMaiorFim();
                    }else if(startDate.before(currentTime)){
                        alertaDataInicioUltrapassada();
                    }else {
                        showProgressDialog("Aguarde", "Criando anúncio...");
                        anuncio = new Anuncio();
                        anuncio.setTitulo(edit_text_titulo_ad.getText().toString());
                        anuncio.setTipo(spinner_novo_ad.getSelectedItem().toString());
                        anuncio.setDescricao(edit_text_descricao_criacao_anuncio.getText().toString());
                        anuncio.setDataInicio(edit_text_start_date.getText().toString());
                        anuncio.setDataFim(edit_text_end_date.getText().toString());
                        anuncio.setIdProprietario(user.getUid());
                        anuncioRepository = new AnuncioRepository();
                        anuncioRepository.salvarDadosAnuncio(anuncio, new AnuncioRepository.OnSaveAnuncio() {
                            @Override
                            public void onSaveAnuncioSuccess(String sucesso) {
                                alertaCriacaoAnuncioSucesso(sucesso);
                            }

                            @Override
                            public void onSaveAnuncioError(String error) {
                                alertaCriacaoAnuncioErro(error);
                            }
                        });
                    }
                } else {
                    alertaCamposNaoPreenchidos();
                }
            }
        });
    }

    private void alertaDataInicioUltrapassada() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(CriacaoAnunciosActivity.this, R.style.AppTheme));
        builder.setMessage("Atenção! Data de início deve ser igual ou depois a data atual!");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hideProgressDialog();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void alertaCamposNaoPreenchidos() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(CriacaoAnunciosActivity.this, R.style.AppTheme));
        builder.setMessage(R.string.campos_nao_preenchidos);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hideProgressDialog();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void alertaCriacaoAnuncioSucesso(String mensagem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(CriacaoAnunciosActivity.this, R.style.AppTheme));
        builder.setMessage(mensagem);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hideProgressDialog();
                dialog.dismiss();
                Intent intent = new Intent(CriacaoAnunciosActivity.this, MenuPrincipalOrganizacaoActivity.class);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void alertaCriacaoAnuncioErro(String mensagem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(CriacaoAnunciosActivity.this, R.style.AppTheme));
        builder.setMessage(mensagem);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hideProgressDialog();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void alertaDataInicioMaiorFim() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(CriacaoAnunciosActivity.this, R.style.AppTheme));
        builder.setMessage(R.string.alerta_selecao_data);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hideProgressDialog();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showProgressDialog(String title, String message) {
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                abrirMenuPrincipal();
        }
        return true;
    }

    private void abrirMenuPrincipal() {
        Intent intent = new Intent(CriacaoAnunciosActivity.this, MenuPrincipalOrganizacaoActivity.class);
        startActivity(intent);
        finish();
    }
}
