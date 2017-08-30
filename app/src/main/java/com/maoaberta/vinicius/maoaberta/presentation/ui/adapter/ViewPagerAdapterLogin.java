package com.maoaberta.vinicius.maoaberta.presentation.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.TabLoginCliente;
import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.TabLoginOrganizacao;

/**
 * Created by vinicius on 30/08/17.
 */

public class ViewPagerAdapterLogin extends FragmentStatePagerAdapter {

    CharSequence titles[];

    public ViewPagerAdapterLogin(FragmentManager fm, CharSequence titles[]){
        super(fm);
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {

        if(position == 0){
            return new TabLoginCliente();
        }else{
            return new TabLoginOrganizacao();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}
