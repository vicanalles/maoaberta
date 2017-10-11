package com.maoaberta.vinicius.maoaberta.presentation.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.FragmentMenuAnunciosCliente;
import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.FragmentMenuEntidadesCliente;
import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.ActivityPerfilCliente;
import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.TabSobre;

/**
 * Created by Vinicius on 03/09/2017.
 */

public class TabsPagerAdapterCliente extends FragmentPagerAdapter {

    private Context context;

    final String[] tabTitles = {
            "Perfil",
            "Anúncios",
            "Entidades",
            "Sobre"
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
                frag = new ActivityPerfilCliente();
                break;
            case 1:
                frag = new FragmentMenuAnunciosCliente();
                break;
            case 2:
                frag = new FragmentMenuEntidadesCliente();
                break;
            case 3:
                frag = new TabSobre();
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
