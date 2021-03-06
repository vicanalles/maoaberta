package com.maoaberta.vinicius.maoaberta.presentation.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.FragmentMenuInteresseOrganizacao;
import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.FragmentMenuEntidadesOrganizacao;
import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.FragmentMenuMeusAnunciosOrganizacao;
import com.maoaberta.vinicius.maoaberta.presentation.ui.activity.SobreActivity;

/**
 * Created by Vinicius on 04/09/2017.
 */

public class TabsPagerAdapterOrganizacao extends FragmentPagerAdapter {

    private Context context;

    final String[] tabTitles = {
            "Meus Anúncios",
            "Meus Interesses",
            "Entidades"
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
                frag = new FragmentMenuInteresseOrganizacao();
                break;
            case 2:
                frag = new FragmentMenuEntidadesOrganizacao();
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
