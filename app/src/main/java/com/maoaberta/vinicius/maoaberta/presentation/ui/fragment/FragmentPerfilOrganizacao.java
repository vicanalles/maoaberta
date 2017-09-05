package com.maoaberta.vinicius.maoaberta.presentation.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maoaberta.vinicius.maoaberta.R;

import butterknife.ButterKnife;

/**
 * Created by Vinicius on 23/08/2017.
 */

public class FragmentPerfilOrganizacao extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_perfil_organizacao, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
