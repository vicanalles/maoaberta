package com.maoaberta.vinicius.maoaberta.presentation.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.domain.models.Anuncio;
import com.maoaberta.vinicius.maoaberta.domain.repository.AnuncioRepository;
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.CriacaoAnunciosActivity;
import com.maoaberta.vinicius.maoaberta.presentation.ui.adapter.OrganizacaoMeusAnunciosAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinicius on 05/09/2017.
 */

public class  FragmentMenuMeusAnunciosOrganizacao extends Fragment {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.recycler_ad_list)
    RecyclerView mRecyclerview;

    private FirebaseUser currentUser;
    private OrganizacaoMeusAnunciosAdapter mAdapter;
    private AnuncioRepository anuncioRepository;
    private List<Anuncio> mAnuncios;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu_meus_anuncios_organizacao, container, false);
        ButterKnife.bind(this, view);

        mAnuncios = new ArrayList<>();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new OrganizacaoMeusAnunciosAdapter(getContext());
        mRecyclerview.setAdapter(mAdapter);

        anuncioRepository = new AnuncioRepository();
        anuncioRepository.getAllMeusAnunciosOrganizacao(new AnuncioRepository.OnGetAllAnunciosOrganizacao() {
                    @Override
                    public void onGetAllAnunciosOrganizacaoSuccess(List<Anuncio> anuncios) {
                        mAdapter.setItems(anuncios);
                    }

                    @Override
                    public void onGetAllAnunciosOrganizacaoError(DatabaseError databaseError) {

                    }
                });

                fab.setImageResource(R.drawable.ic_action_add);
        
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCriacaoAnuncios();
            }
        });

        return view;
    }

    private void abrirCriacaoAnuncios(){
        Intent intent = new Intent(getActivity(), CriacaoAnunciosActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }
}
