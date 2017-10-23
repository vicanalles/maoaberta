package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.maoaberta.vinicius.maoaberta.R;

import java.util.Calendar;

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
    @BindView(R.id.text_view_end_date)
    TextView text_view_end_date;
    @BindView(R.id.edit_text_end_date)
    EditText edit_text_end_date;
    @BindView(R.id.spinner_novo_ad)
    Spinner spinner_novo_ad;

    private int mes, dia, ano;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criacao_anuncios);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_layout_menu_novo_ad);

        if(getSupportActionBar() != null){
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
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
