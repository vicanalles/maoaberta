package com.maoaberta.vinicius.maoaberta.presentation.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.TabInformacaoSobre1;
import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.TabInformacaoSobre2;

/**
 * Created by vinicius on 08/11/17.
 */

public class ViewPagerConteudoSobreAdapter extends FragmentPagerAdapter{

    public ViewPagerConteudoSobreAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0: return TabInformacaoSobre1.newInstance();
            case 1: return TabInformacaoSobre2.newInstance();
            default: return TabInformacaoSobre1.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
