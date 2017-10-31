package com.maoaberta.vinicius.maoaberta.presentation.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Organizacao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vinicius on 31/10/17.
 */

public class OrganizacaoEntidadesAdapter extends RecyclerView.Adapter<OrganizacaoEntidadesAdapter.MyViewHolder>{

    private Context context;
    private List<Organizacao> organizacoes;
    private FirebaseUser currentUser;

    public OrganizacaoEntidadesAdapter(Context context){
        this.context = context;
        organizacoes = new ArrayList<>();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_entidades_organizacao, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Organizacao organizacao = organizacoes.get(position);

        holder.text_view_nome_instituicao.setText(organizacao.getNomeFantasia());
        if(organizacao.getPhotoUrl() != null){
            try{
                Glide.with(context).load(organizacao.getPhotoUrl()).diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).into(holder.image_view_logo_entidade);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
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
