package com.maoaberta.vinicius.maoaberta.presentation.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.CriacaoAnunciosActivity;
import com.maoaberta.vinicius.maoaberta.presentation.ui.adapter.OrganizacaoMeusAnunciosAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinicius on 05/09/2017.
 */

public class FragmentMenuMeusAnunciosOrganizacao extends Fragment {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.recycler_ad_list)
    RecyclerView recyclerAdList;

    private OrganizacaoMeusAnunciosAdapter organizacaoMeusAnunciosAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu_meus_anuncios_organizacao, container, false);
        ButterKnife.bind(this, view);

        organizacaoMeusAnunciosAdapter = new OrganizacaoMeusAnunciosAdapter(getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerAdList.setLayoutManager(layoutManager);
        recyclerAdList.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerAdList.getContext(), DividerItemDecoration.VERTICAL);
        recyclerAdList.addItemDecoration(dividerItemDecoration);
        recyclerAdList.setAdapter(organizacaoMeusAnunciosAdapter);



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
