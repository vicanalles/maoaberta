package com.maoaberta.vinicius.maoaberta.presentation.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maoaberta.vinicius.maoaberta.R;

import butterknife.ButterKnife;

/**
 * Created by vinicius on 10/11/17.
 */

public class TabInformacaoSobre3 extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab_informacao_sobre_tres, container, false);
        ButterKnife.bind(this, v);

        return v;
    }

    public static TabInformacaoSobre3 newInstance(){
        TabInformacaoSobre3 tab3 = new TabInformacaoSobre3();
        return tab3;
    }
}
