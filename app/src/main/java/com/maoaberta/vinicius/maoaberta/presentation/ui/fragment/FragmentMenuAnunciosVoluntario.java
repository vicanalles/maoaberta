package com.maoaberta.vinicius.maoaberta.presentation.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Anuncio;
import com.maoaberta.vinicius.maoaberta.domain.models.Voluntario;
import com.maoaberta.vinicius.maoaberta.domain.repository.AnuncioRepository;
import com.maoaberta.vinicius.maoaberta.domain.repository.UsuarioRepository;
import com.maoaberta.vinicius.maoaberta.presentation.ui.adapter.VoluntarioAnunciosAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinicius on 03/09/2017.
 */

public class FragmentMenuAnunciosVoluntario extends Fragment {

    @BindView(R.id.recycler_view_anuncios_voluntario)
    RecyclerView mRecycler;

    private List<Anuncio> anuncios;
    private AnuncioRepository anuncioRepository;
    private VoluntarioAnunciosAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_anuncios_cliente, container, false);
        ButterKnife.bind(this, view);

        anuncios = new ArrayList<>();

        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new VoluntarioAnunciosAdapter(getContext());
        mRecycler.setAdapter(mAdapter);

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
