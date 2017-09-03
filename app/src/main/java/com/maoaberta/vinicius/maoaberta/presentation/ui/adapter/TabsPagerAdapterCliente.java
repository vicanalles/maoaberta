package com.maoaberta.vinicius.maoaberta.presentation.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.FragmentMenuAnunciosCliente;
import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.FragmentMenuMeusAnunciosCliente;
import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.FragmentMenuEntidadeCliente;
import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.FragmentPerfilCliente;

/**
 * Created by Vinicius on 03/09/2017.
 */

public class TabsPagerAdapterCliente extends FragmentPagerAdapter {

    private Context context;

    final String[] tabTitles = {
            "ANÚNCIOS",
            "ENTIDADES",
            "MEUS ANÚNCIOS",
            "PERFIL"
    };

    public TabsPagerAdapterCliente(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment frag = null;

        switch (position) {
            case 0:
                frag = new FragmentMenuAnunciosCliente();
                break;
            case 1:
                frag = new FragmentMenuEntidadeCliente();
                break;
            case 2:
                frag = new FragmentMenuMeusAnunciosCliente();
                break;
            case 3:
                frag = new FragmentPerfilCliente();
                break;
        }

        Bundle b = new Bundle();
        b.putInt("position", position);

        if(frag != null){
            frag.setArguments(b);
        }

        return frag;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
