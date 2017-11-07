package com.maoaberta.vinicius.maoaberta.presentation.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Organizacao;
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.AnunciosOrganizacaoActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by vinicius on 31/10/17.
 */

public class OrganizacaoEntidadesAdapter extends RecyclerView.Adapter<OrganizacaoEntidadesAdapter.MyViewHolder>{

    private Context context;
    private List<Organizacao> organizacoes;
    private ImageView image_view_close_dialog;
    private CircleImageView image_view_entidade_information;
    private TextView text_view_nome_fantasia_text;
    private TextView text_view_cnpj_text;
    private TextView text_view_email_text;
    private TextView text_view_nome_responsavel_text;
    private TextView text_view_endereco_entidade_text;
    private TextView text_view_descricao_entidade_text;
    private Button button_ver_anuncios_entidade;

    public OrganizacaoEntidadesAdapter(Context context){
        this.context = context;
        organizacoes = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_entidades_organizacao, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Organizacao organizacao = organizacoes.get(position);

        holder.text_view_nome_instituicao.setText(organizacao.getNomeFantasia());
        if(organizacao.getPhotoUrl() != null){
            try{
                Glide.with(context).load(organizacao.getPhotoUrl()).diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).into(holder.image_view_logo_entidade);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }else{
            try{
                Glide.with(context).load(R.drawable.ic_account_circle).diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).into(holder.image_view_logo_entidade);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_entidade_information);

                image_view_close_dialog = (ImageView) dialog.findViewById(R.id.image_view_close_dialog);
                image_view_entidade_information = (CircleImageView) dialog.findViewById(R.id.image_view_entidade_information);
                text_view_nome_fantasia_text = (TextView) dialog.findViewById(R.id.text_view_nome_fantasia_text);
                text_view_cnpj_text = (TextView) dialog.findViewById(R.id.text_view_cnpj_text);
                text_view_email_text = (TextView) dialog.findViewById(R.id.text_view_email_text);
                text_view_nome_responsavel_text = (TextView) dialog.findViewById(R.id.text_view_nome_responsavel_text);
                text_view_endereco_entidade_text = (TextView) dialog.findViewById(R.id.text_view_endereco_entidade_text);
                text_view_descricao_entidade_text = (TextView) dialog.findViewById(R.id.text_view_descricao_entidade_text);
                button_ver_anuncios_entidade = (Button) dialog.findViewById(R.id.button_ver_anuncios_entidade);

                image_view_close_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                if(organizacao.getPhotoUrl() != null){
                    try{
                        Glide.with(context).load(organizacao.getPhotoUrl()).diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true).into(image_view_entidade_information);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }else{
                    try{
                        Glide.with(context).load(R.drawable.ic_account_circle).diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true).into(image_view_entidade_information);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }

                text_view_nome_fantasia_text.setText(organizacao.getNomeFantasia());
                text_view_cnpj_text.setText(organizacao.getCnpj());
                text_view_email_text.setText(organizacao.getEmail());
                text_view_nome_responsavel_text.setText(organizacao.getNomeResponsavel());
                text_view_endereco_entidade_text.setText(organizacao.getEndereco());
                text_view_descricao_entidade_text.setText(organizacao.getDescricao());

                button_ver_anuncios_entidade.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        abrirTelaAnunciosOrganizacao(organizacao);
                    }
                });
                dialog.show();
            }
        });
    }

    public void abrirTelaAnunciosOrganizacao(Organizacao organizacao){
        Intent intent = new Intent(context, AnunciosOrganizacaoActivity.class);
        intent.putExtra("organizacao", organizacao);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return organizacoes.size();
    }

    public void setItems(List<Organizacao> organizacoes){
        this.organizacoes.clear();
        this.organizacoes.addAll(organizacoes);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.image_view_logo_entidade)
        ImageView image_view_logo_entidade;
        @BindView(R.id.text_view_nome_instituicao)
        TextView text_view_nome_instituicao;

        public MyViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
