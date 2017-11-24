package com.maoaberta.vinicius.maoaberta.presentation.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.FragmentMenuAnunciosVoluntario;
import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.FragmentMenuEntidadesVoluntario;
import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.FragmentMenuMeusAnunciosVoluntario;
import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.TabSobre;

/**
 * Created by Vinicius on 03/09/2017.
 */

public class TabsPagerAdapterCliente extends FragmentPagerAdapter {

    private Context context;

    final String[] tabTitles = {
            "Meus Interesses",
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

        switch (position){
            case 0:
                return new FragmentMenuMeusAnunciosVoluntario();
            case 1:
                return new FragmentMenuAnunciosVoluntario();
            case 2:
                return new FragmentMenuEntidadesVoluntario();
            case 3:
                return new TabSobre();
            default:
                return null;
        }
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
