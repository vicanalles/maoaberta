package com.maoaberta.vinicius.maoaberta.presentation.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Anuncio;
import com.maoaberta.vinicius.maoaberta.domain.models.Organizacao;
import com.maoaberta.vinicius.maoaberta.domain.repository.AnuncioRepository;
import com.maoaberta.vinicius.maoaberta.domain.repository.OrganizacaoRepository;
import com.maoaberta.vinicius.maoaberta.presentation.ui.adapter.OrganizacaoAnunciosAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinicius on 05/09/2017.
 */

public class FragmentMenuAnunciosOrganizacao extends Fragment {

    @BindView(R.id.recycler_view_anuncios)
    RecyclerView mRecyclerView;

    private List<Anuncio> mAnuncios;
    private AnuncioRepository anuncioRepository;
    private OrganizacaoRepository organizacaoRepository;
    private OrganizacaoAnunciosAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu_anuncios_organizacao, container, false);
        ButterKnife.bind(this, view);

        mAnuncios = new ArrayList<>();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new OrganizacaoAnunciosAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);

        anuncioRepository = new AnuncioRepository();
        anuncioRepository.getAllAnuncios(new AnuncioRepository.OnGetAllAnuncios() {
            @Override
            public void onGetAllAnunciosSuccess(List<Anuncio> anuncios) {
                mAdapter.setItems(anuncios);
            }

            @Override
            public void onGetAllAnunciosError(DatabaseError erro) {

            }
        });

        return view;
    }
}
