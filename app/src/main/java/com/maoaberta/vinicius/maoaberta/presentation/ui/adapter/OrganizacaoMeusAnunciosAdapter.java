package com.maoaberta.vinicius.maoaberta.presentation.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Anuncio;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vinicius on 25/10/17.
 */

public class OrganizacaoMeusAnunciosAdapter extends RecyclerView.Adapter<OrganizacaoMeusAnunciosAdapter.MyViewHolder>{

    private Context context;
    private List<Anuncio> anuncios;
    private FirebaseUser currentUser;

    public OrganizacaoMeusAnunciosAdapter(Context context){
        this.context = context;
        anuncios = new ArrayList<>();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public OrganizacaoMeusAnunciosAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meus_anuncios_organizacao, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Anuncio anuncio = anuncios.get(position);

        holder.text_view_title_anuncio.setText(anuncio.getTitulo());
        holder.text_view_data_inicio_anuncio.setText(anuncio.getDataInicio());
        holder.text_view_data_termino_anuncio.setText(anuncio.getDataFim());
    }

    @Override
    public int getItemCount() {
        return anuncios.size();
    }

    public void setItems(List<Anuncio> anuncios) {
        this.anuncios.clear();
        this.anuncios.addAll(anuncios);
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.text_view_title_anuncio)
        TextView text_view_title_anuncio;
        @BindView(R.id.text_view_data_inicio_anuncio)
        TextView text_view_data_inicio_anuncio;
        @BindView(R.id.text_view_data_termino_anuncio)
        TextView text_view_data_termino_anuncio;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
