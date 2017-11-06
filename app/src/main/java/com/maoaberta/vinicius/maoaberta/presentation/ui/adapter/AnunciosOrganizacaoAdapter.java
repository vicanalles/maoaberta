package com.maoaberta.vinicius.maoaberta.presentation.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Anuncio;
import com.maoaberta.vinicius.maoaberta.domain.models.Organizacao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by vinicius on 06/11/17.
 */

public class AnunciosOrganizacaoAdapter extends RecyclerView.Adapter<AnunciosOrganizacaoAdapter.MyViewHolder>{

    private Context context;
    private List<Anuncio> anuncios;
    private Organizacao ong;

    public AnunciosOrganizacaoAdapter(Context context, Organizacao ong){
        this.context = context;
        anuncios = new ArrayList<>();
        this.ong = ong;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_anuncios_organizacao, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Anuncio anuncio = anuncios.get(position);

        holder.text_view_nome_organizacao.setText(ong.getNomeFantasia());
        holder.text_view_titulo_anuncio.setText(anuncio.getTitulo());
        holder.text_view_valido_de.setText(anuncio.getDataInicio());
        holder.text_view_valido_ate.setText(anuncio.getDataFim());
    }

    public void setItems(List<Anuncio> anuncios){
        this.anuncios.clear();
        this.anuncios.addAll(anuncios);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return anuncios.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.text_view_nome_organizacao)
        TextView text_view_nome_organizacao;
        @BindView(R.id.text_view_titulo_anuncio)
        TextView text_view_titulo_anuncio;
        @BindView(R.id.text_view_valido_de)
        TextView text_view_valido_de;
        @BindView(R.id.text_view_valido_ate)
        TextView text_view_valido_ate;

        public MyViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
