package com.maoaberta.vinicius.maoaberta.presentation.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.maoaberta.vinicius.maoaberta.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinicius Canalles on 16/08/2017.
 */

public class TabCadastroOrganizacao extends Fragment {

    @BindView(R.id.edit_text_razao_social_organizacao)
    EditText razaoSocial;
    @BindView(R.id.edit_text_nome_fantasia_organizacao)
    EditText nomeFantasia;
    @BindView(R.id.edit_text_cnpj_organizacao)
    EditText cnpjOrganizacao;
    @BindView(R.id.edit_text_nome_responsavel_organizacao)
    EditText nomeResponsavel;
    @BindView(R.id.edit_text_email_organizacao)
    EditText emailOrganizacao;
    @BindView(R.id.edit_text_telefone_organizacao)
    EditText telefoneOrganizacao;
    @BindView(R.id.edit_text_senha_organizacao)
    EditText senhaOrganizacao;
    @BindView(R.id.edit_text_confirmar_senha_organizacao)
    EditText confirmarSenhaOrganizacao;
    @BindView(R.id.button_cadastrar_organizacao)
    Button cadastrarOrganizacao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab_cadastro_organizacao, container, false);
        ButterKnife.bind(this, v);

        cadastrarOrganizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Cadastrar Organizacao", Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }
}
