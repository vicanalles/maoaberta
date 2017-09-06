package com.maoaberta.vinicius.maoaberta.presentation.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maoaberta.vinicius.maoaberta.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vinicius on 05/09/2017.
 */

public class FragmentMenuEntidadesOrganizacao extends Fragment {

    @BindView(R.id.text_view_entidades_fragment_organizacao)
    TextView text_view_entidades_fragment_organizacao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu_entidades_organizacao, container, false);
        ButterKnife.bind(this, view);
        text_view_entidades_fragment_organizacao.setText("Fragment Entidades Organização");
        return view;
    }
}