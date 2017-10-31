package com.maoaberta.vinicius.maoaberta.presentation.ui.fragment;

import android.os.Bundle;
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
import com.maoaberta.vinicius.maoaberta.domain.models.Organizacao;
import com.maoaberta.vinicius.maoaberta.domain.repository.OrganizacaoRepository;
import com.maoaberta.vinicius.maoaberta.presentation.ui.adapter.OrganizacaoEntidadesAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinicius on 05/09/2017.
 */

public class FragmentMenuEntidadesOrganizacao extends Fragment {

    @BindView(R.id.recycler_organizacao_list)
    RecyclerView mRecyclerview;

    private FirebaseUser currentUser;
    private List<Organizacao> mOrganizacoes;
    private OrganizacaoEntidadesAdapter mAdapter;
    private OrganizacaoRepository organizacaoRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu_entidades_organizacao, container, false);
        ButterKnife.bind(this, view);

        mOrganizacoes = new ArrayList<>();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new OrganizacaoEntidadesAdapter(getContext());
        mRecyclerview.setAdapter(mAdapter);

        organizacaoRepository = new OrganizacaoRepository();
        organizacaoRepository.getAllOrganizacoes(new OrganizacaoRepository.OnGetAllOrganizacoes() {
            @Override
            public void onGetAllOrganizacoesSuccess(List<Organizacao> organizacoes) {
                mAdapter.setItems(organizacoes);
            }

            @Override
            public void onGetAllOrganizacoesError(DatabaseError databaseError) {

            }
        });

        return view;
    }
}
