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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Anuncio;
import com.maoaberta.vinicius.maoaberta.domain.models.Organizacao;
import com.maoaberta.vinicius.maoaberta.domain.repository.InteressadosRepository;
import com.maoaberta.vinicius.maoaberta.domain.repository.InteressesRepository;
import com.maoaberta.vinicius.maoaberta.domain.repository.OrganizacaoRepository;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by vinicius on 07/11/17.
 */

public class OrganizacaoInteressesAdapter extends RecyclerView.Adapter<OrganizacaoInteressesAdapter.MyViewHolder>{

    private Context context;
    private List<Anuncio> anuncios;
    private OrganizacaoRepository organizacaoRepository;
    private ImageView image_view_close_dialog_interesse;
    private TextView text_view_titulo_ad_text_interesse;
    private TextView text_view_tipo_ad_text_interesse;
    private TextView text_view_descricao_ad_text_interesse;
    private TextView text_view_start_date_text_interesse;
    private TextView text_view_end_date_text_interesse;
    private Button button_demonstrar_interesse;

    public OrganizacaoInteressesAdapter(Context context){
        this.context = context;
        anuncios = new ArrayList<>();
        organizacaoRepository = new OrganizacaoRepository();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_anuncios_interesses_organizacao, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Anuncio anuncio = anuncios.get(position);

        holder.text_view_titulo_anuncio_organizacao.setText(anuncio.getAnuncio());
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

                text_view_titulo_ad_text_interesse.setText(anuncio.getAnuncio());
                text_view_tipo_ad_text_interesse.setText(anuncio.getTipo());
                text_view_descricao_ad_text_interesse.setText(anuncio.getInstituicao());
                text_view_start_date_text_interesse.setText(anuncio.getDataInicio());
                text_view_end_date_text_interesse.setText(anuncio.getDataFim());

                button_demonstrar_interesse.setText(R.string.cancelar_interesse);

                button_demonstrar_interesse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final InteressesRepository interessesRepository = new InteressesRepository();
                        InteressadosRepository interessadosRepository = new InteressadosRepository();
                        interessadosRepository.removerInteresseAnuncio(anuncio.getId(), new InteressadosRepository.OnRemoverInteresse() {
                            @Override
                            public void onRemoverInteresseSuccess(String sucesso) {
                                interessesRepository.removerInteresseOrganizacao(anuncio, new InteressesRepository.OnRemoveInteresse() {
                                    @Override
                                    public void onRemoveInteresseSuccess() {
                                        removeItems(anuncio);
                                        Toast.makeText(context, "Interesse removido com sucesso", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onRemoveInteresseError() {
                                        Toast.makeText(context, "Não foi possível remover seu interesse. Por favor, tente novamente!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onRemoverInteresseError(String error) {
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                dialog.show();
            }
        });
    }

    private void removeItems(Anuncio anuncio) {
        this.anuncios.remove(anuncio);
        notifyDataSetChanged();
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
