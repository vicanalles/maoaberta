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
import com.maoaberta.vinicius.maoaberta.domain.repository.OrganizacaoRepository;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by vinicius on 07/11/17.
 */

public class OrganizacaoAnunciosAdapter extends RecyclerView.Adapter<OrganizacaoAnunciosAdapter.MyViewHolder>{

    private Context context;
    private List<Anuncio> anuncios;
    private OrganizacaoRepository organizacaoRepository;

    public OrganizacaoAnunciosAdapter(Context context){
        this.context = context;
        anuncios = new ArrayList<>();
        organizacaoRepository = new OrganizacaoRepository();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_anuncios_entidades_organizacao, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Anuncio anuncio = anuncios.get(position);

        holder.text_view_titulo_anuncio_organizacao.setText(anuncio.getTitulo());
        holder.text_view_valido_de_anuncios.setText(anuncio.getDataInicio());
        holder.text_view_valido_ate_anuncios.setText(anuncio.getDataFim());

        organizacaoRepository.getOrganizacaoById(anuncio.getIdProprietario(), new OrganizacaoRepository.OnGetOrganizacaoById() {
            @Override
            public void onGetOrganizacaoByIdSuccess(Organizacao organizacao) {
                if(organizacao.getPhotoUrl() != null){
                    try{
                        Glide.with(context).load(organizacao.getPhotoUrl()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                                .into(holder.image_view_logo_organizacao);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }else{
                    Glide.with(context).load(R.drawable.ic_account_circle).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                            .into(holder.image_view_logo_organizacao);
                }
                holder.text_view_nome_organizacao_anuncios.setText(organizacao.getNomeFantasia());
            }

            @Override
            public void onGetOrganizacaoByIdError(String error) {

            }
        });
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

        @BindView(R.id.image_view_logo_organizacao)
        CircleImageView image_view_logo_organizacao;
        @BindView(R.id.text_view_nome_organizacao_anuncios)
        TextView text_view_nome_organizacao_anuncios;
        @BindView(R.id.text_view_titulo_anuncio_organizacao)
        TextView text_view_titulo_anuncio_organizacao;
        @BindView(R.id.text_view_valido_de_anuncios)
        TextView text_view_valido_de_anuncios;
        @BindView(R.id.text_view_valido_ate_anuncios)
        TextView text_view_valido_ate_anuncios;

        public MyViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
