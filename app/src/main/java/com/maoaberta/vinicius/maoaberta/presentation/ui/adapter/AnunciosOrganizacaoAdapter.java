package com.maoaberta.vinicius.maoaberta.presentation.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Anuncio;
import com.maoaberta.vinicius.maoaberta.domain.models.Organizacao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vinicius on 06/11/17.
 */

public class AnunciosOrganizacaoAdapter extends RecyclerView.Adapter<AnunciosOrganizacaoAdapter.MyViewHolder>{

    private Context context;
    private List<Anuncio> anuncios;
    private Organizacao ong;
    private ImageView image_view_close_dialog_interesse;
    private TextView text_view_titulo_ad_text_interesse;
    private TextView text_view_tipo_ad_text_interesse;
    private TextView text_view_descricao_ad_text_interesse;
    private TextView text_view_start_date_text_interesse;
    private TextView text_view_end_date_text_interesse;
    private Button button_demonstrar_interesse;

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
        final Anuncio anuncio = anuncios.get(position);

        holder.text_view_nome_organizacao.setText(ong.getNomeFantasia());
        holder.text_view_titulo_anuncio.setText(anuncio.getTitulo());
        holder.text_view_valido_de.setText(anuncio.getDataInicio());
        holder.text_view_valido_ate.setText(anuncio.getDataFim());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_anuncio_organizacao_interesse);

                image_view_close_dialog_interesse = (ImageView) dialog.findViewById(R.id.image_view_close_dialog_interesse);
                text_view_titulo_ad_text_interesse = (TextView) dialog.findViewById(R.id.text_view_titulo_ad_text_interesse);
                text_view_tipo_ad_text_interesse = (TextView) dialog.findViewById(R.id.text_view_tipo_ad_text_interesse);
                text_view_descricao_ad_text_interesse = (TextView) dialog.findViewById(R.id.text_view_descricao_ad_text_interesse);
                text_view_start_date_text_interesse = (TextView) dialog.findViewById(R.id.text_view_start_date_text_interesse);
                text_view_end_date_text_interesse = (TextView) dialog.findViewById(R.id.text_view_end_date_text_interesse);
                button_demonstrar_interesse = (Button) dialog.findViewById(R.id.button_demonstrar_interesse);

                image_view_close_dialog_interesse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                text_view_titulo_ad_text_interesse.setText(anuncio.getTitulo());
                text_view_tipo_ad_text_interesse.setText(anuncio.getTipo());
                text_view_descricao_ad_text_interesse.setText(anuncio.getDescricao());
                text_view_start_date_text_interesse.setText(anuncio.getDataInicio());
                text_view_end_date_text_interesse.setText(anuncio.getDataFim());

                button_demonstrar_interesse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "Demonstrando interesse no anúncio", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            }
        });
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
