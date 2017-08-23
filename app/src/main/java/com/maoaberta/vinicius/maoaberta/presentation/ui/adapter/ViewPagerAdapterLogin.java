package com.maoaberta.vinicius.maoaberta.presentation.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.maoaberta.vinicius.maoaberta.R;

/**
 * Created by Vinicius Canalles on 16/08/2017.
 */

public class ViewPagerAdapterLogin extends RecyclerView.Adapter<ViewPagerAdapterLogin.ViewHolder>{

    private static final int TAB_LEFT = 0;
    private static final int TAB_RIGHT = 1;
    int numOfTabs = 2;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case TAB_LEFT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_login_cliente, parent, false);
                return new ViewHolder(view, TAB_LEFT);
            case TAB_RIGHT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_login_organizacao, parent, false);
                return new ViewHolder(view, TAB_RIGHT);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return numOfTabs;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        EditText emailCliente;
        EditText senhaCliente;
        Button logarCliente;
        TextView cadastrarCliente;

        EditText cnpjOrganizacao;
        EditText senhaOrganizacao;
        Button logarOrganizacao;
        TextView cadastrarOrganizacao;

        public ViewHolder(View v, int type){
            super(v);
            switch (type){
                case TAB_LEFT:
                    emailCliente = (EditText) v.findViewById(R.id.edit_text_email_cliente_login);
                    senhaCliente = (EditText) v.findViewById(R.id.edit_text_password_cliente_login);
                    logarCliente = (Button) v.findViewById(R.id.button_login_app_cliente);
                    cadastrarCliente = (TextView) v.findViewById(R.id.text_view_abrir_cadastro_cliente);
                    break;
                case TAB_RIGHT:
                    cnpjOrganizacao = (EditText) v.findViewById(R.id.edit_text_cnpj_organizacao_login);
                    senhaOrganizacao = (EditText) v.findViewById(R.id.edit_text_password_organizacao_login);
                    logarOrganizacao = (Button) v.findViewById(R.id.button_login_app_organizacao);
                    cadastrarOrganizacao = (TextView) v.findViewById(R.id.text_view_abrir_cadastro_organizacao);
                    break;
            }
        }
    }
}
