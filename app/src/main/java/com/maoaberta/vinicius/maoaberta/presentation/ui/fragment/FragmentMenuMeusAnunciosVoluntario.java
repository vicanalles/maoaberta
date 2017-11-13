package com.maoaberta.vinicius.maoaberta.presentation.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Anuncio;
import com.maoaberta.vinicius.maoaberta.domain.repository.AnuncioRepository;
import com.maoaberta.vinicius.maoaberta.domain.repository.InteressadosRepository;
import com.maoaberta.vinicius.maoaberta.presentation.ui.adapter.VoluntarioMeusAnunciosAdapter;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinicius on 03/09/2017.
 */

public class FragmentMenuMeusAnunciosVoluntario extends Fragment {

    @BindView(R.id.recycler_view_meus_anuncios_voluntario)
    RecyclerView mRecycler;

    private VoluntarioMeusAnunciosAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu_meus_anuncios_cliente, container, false);
        ButterKnife.bind(this, view);

        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new VoluntarioMeusAnunciosAdapter(getContext());
        mRecycler.setAdapter(mAdapter);

        InteressadosRepository interessadosRepository = new InteressadosRepository();
        interessadosRepository.getAnunciosInteressado(new InteressadosRepository.OnGetAllAnunciosInteresseVoluntario() {
            @Override
            public void onGetAllAnunciosInteresseVoluntarioSuccess(HashMap<String, Boolean> anuncios) {

            }

            @Override
            public void onGetAllAnunciosInteresseVoluntarioError(DatabaseError databaseError) {

            }
        });

        return view;
    }
}
