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
import com.maoaberta.vinicius.maoaberta.domain.models.Organizacao;
import com.maoaberta.vinicius.maoaberta.domain.models.Voluntario;
import com.maoaberta.vinicius.maoaberta.domain.repository.OrganizacaoRepository;
import com.maoaberta.vinicius.maoaberta.domain.repository.TipoRepository;
import com.maoaberta.vinicius.maoaberta.domain.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Vinicius on 16/11/2017.
 */

public class InteressadosAnuncioAdapter extends RecyclerView.Adapter<InteressadosAnuncioAdapter.MyViewHolder> {

    private Context context;
    private List<String> voluntarios;
    private TipoRepository tipoRepository;
    private UsuarioRepository usuarioRepository;
    private OrganizacaoRepository organizacaoRepository;

    public InteressadosAnuncioAdapter(Context context) {
        this.context = context;
        voluntarios = new ArrayList<>();
        tipoRepository = new TipoRepository();
        usuarioRepository = new UsuarioRepository();
        organizacaoRepository = new OrganizacaoRepository();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_interessados_anuncio, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final String pessoa = voluntarios.get(position);

        tipoRepository.getTipoById(pessoa, new TipoRepository.OnGetTipoById() {
            @Override
            public void onGetTipoByIdSuccess(String tipo) {
                if (tipo.equals("voluntario")) {
                    usuarioRepository.getUserByUid(pessoa, new UsuarioRepository.OnGetUserById() {
                        @Override
                        public void onGetUserByIdSuccess(Voluntario voluntario) {
                            if (voluntario != null) {
                                holder.text_view_nome_voluntario.setText(voluntario.getNome());
                                holder.text_view_telefone_voluntario.setText(voluntario.getTelefone());

                                if (voluntario.getPhotoUrl() != null) {
                                    try {
                                        Glide.with(context).load(voluntario.getPhotoUrl()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                                                .into(holder.image_view_profile_image_voluntario);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        Glide.with(context).load(R.drawable.ic_account_circle).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                                                .into(holder.image_view_profile_image_voluntario);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onGetUserByIdError(String error) {

                        }
                    });
                } else {
                    organizacaoRepository.getOrganizacaoById(pessoa, new OrganizacaoRepository.OnGetOrganizacaoById() {
                        @Override
                        public void onGetOrganizacaoByIdSuccess(Organizacao organizacao) {
                            if (organizacao != null) {
                                holder.text_view_nome_voluntario.setText(organizacao.getNomeFantasia());
                                holder.text_view_telefone_voluntario.setText(organizacao.getTelefone());

                                if (organizacao.getPhotoUrl() != null) {
                                    try {
                                        Glide.with(context).load(organizacao.getPhotoUrl()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                                                .into(holder.image_view_profile_image_voluntario);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        Glide.with(context).load(R.drawable.ic_account_circle).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                                                .into(holder.image_view_profile_image_voluntario);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onGetOrganizacaoByIdError(String error) {

                        }
                    });
                }
            }

            @Override
            public void onGetTipoByIdError(String error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return voluntarios.size();
    }

    public void setItems(List<String> voluntarios) {
        this.voluntarios.clear();
        this.voluntarios.addAll(voluntarios);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_view_profile_image_voluntario)
        CircleImageView image_view_profile_image_voluntario;
        @BindView(R.id.text_view_nome_voluntario)
        TextView text_view_nome_voluntario;
        @BindView(R.id.text_view_telefone_voluntario)
        TextView text_view_telefone_voluntario;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
