package com.maoaberta.vinicius.maoaberta.presentation.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maoaberta.vinicius.maoaberta.R;

/**
 * Created by vinicius on 29/08/17.
 */

public class TabSobre extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab_sobre, container, false);
        setHasOptionsMenu(true);
        return v;
    }
}
