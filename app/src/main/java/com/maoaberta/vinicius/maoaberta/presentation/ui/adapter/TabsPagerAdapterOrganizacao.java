package com.maoaberta.vinicius.maoaberta.presentation.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.FragmentMenuAnunciosOrganizacao;
import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.FragmentMenuEntidadesOrganizacao;
import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.FragmentMenuMeusAnunciosOrganizacao;
import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.TabSobre;

/**
 * Created by Vinicius on 04/09/2017.
 */

public class TabsPagerAdapterOrganizacao extends FragmentPagerAdapter {

    private Context context;

    final String[] tabTitles = {
            "Meus Anúncios",
            "Anúncios",
            "Entidades",
            "Sobre"
    };

    public TabsPagerAdapterOrganizacao(FragmentManager fm, Context context){
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment frag = null;

        switch (position){
            case 0:
                frag = new FragmentMenuMeusAnunciosOrganizacao();
                break;
            case 1:
                frag = new FragmentMenuAnunciosOrganizacao();
                break;
            case 2:
                frag = new FragmentMenuEntidadesOrganizacao();
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
