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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Anuncio;
import com.maoaberta.vinicius.maoaberta.domain.repository.InteressadosRepository;
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.CriacaoAnunciosActivity;
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.MenuPrincipalOrganizacaoActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vinicius on 13/11/17.
 */

public class VoluntarioMeusAnunciosAdapter extends RecyclerView.Adapter<VoluntarioMeusAnunciosAdapter.MyViewHolder>{

    private Context context;
    private List<Anuncio> mAnuncios;
    private ImageView image_view_close_dialog;
    private TextView text_view_titulo_ad_text;
    private TextView text_view_tipo_ad_text;
    private TextView text_view_descricao_ad_text;
    private TextView text_view_start_date_text;
    private TextView text_view_end_date_text;
    private Button button_edit_ad;
    private FirebaseUser currentUser;

    public VoluntarioMeusAnunciosAdapter(Context context){
        this.context = context;
        mAnuncios = new ArrayList<>();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_meus_anuncios_voluntario, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Anuncio anuncio = mAnuncios.get(position);

        holder.text_view_title_anuncio_voluntario.setText(anuncio.getTitulo());
        holder.text_view_data_inicio_anuncio_voluntario.setText(anuncio.getDataInicio());
        holder.text_view_data_termino_anuncio_voluntario.setText(anuncio.getDataFim());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_meus_anuncios_information);

                image_view_close_dialog = (ImageView) dialog.findViewById(R.id.image_view_close_dialog);
                text_view_titulo_ad_text = (TextView) dialog.findViewById(R.id.text_view_titulo_ad_text);
                text_view_tipo_ad_text = (TextView) dialog.findViewById(R.id.text_view_tipo_ad_text);
                text_view_descricao_ad_text = (TextView) dialog.findViewById(R.id.text_view_descricao_ad_text);
                text_view_start_date_text = (TextView) dialog.findViewById(R.id.text_view_start_date_text);
                text_view_end_date_text = (TextView) dialog.findViewById(R.id.text_view_end_date_text);
                button_edit_ad = (Button) dialog.findViewById(R.id.button_edit_ad);

                text_view_titulo_ad_text.setText(anuncio.getTitulo());
                text_view_tipo_ad_text.setText(anuncio.getTipo());
                text_view_descricao_ad_text.setText(anuncio.getDescricao());
                text_view_start_date_text.setText(anuncio.getDataInicio());
                text_view_end_date_text.setText(anuncio.getDataFim());

                button_edit_ad.setText(R.string.cancelar_interesse);

                button_edit_ad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InteressadosRepository interessadosRepository = new InteressadosRepository();
                        interessadosRepository.removerInteresseAnuncio(anuncio.getId(), new InteressadosRepository.OnRemoverInteresse() {
                            @Override
                            public void onRemoverInteresseSuccess(String sucesso) {
                                Toast.makeText(context, sucesso, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }

                            @Override
                            public void onRemoverInteresseError(String error) {
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                image_view_close_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void abrirTelaEdicaoAnuncio(Anuncio anuncio) {
        Intent intent = new Intent(context, CriacaoAnunciosActivity.class);
        intent.putExtra("anuncio", anuncio);
        context.startActivity(intent);
        ((MenuPrincipalOrganizacaoActivity)context).finish();
    }

    @Override
    public int getItemCount() {
        return mAnuncios.size();
    }

    public void setItems(List<Anuncio> anuncios){
        this.mAnuncios.clear();
        this.mAnuncios.addAll(anuncios);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.text_view_title_anuncio_voluntario)
        TextView text_view_title_anuncio_voluntario;
        @BindView(R.id.text_view_data_inicio_anuncio_voluntario)
        TextView text_view_data_inicio_anuncio_voluntario;
        @BindView(R.id.text_view_data_termino_anuncio_voluntario)
        TextView text_view_data_termino_anuncio_voluntario;

        public MyViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
