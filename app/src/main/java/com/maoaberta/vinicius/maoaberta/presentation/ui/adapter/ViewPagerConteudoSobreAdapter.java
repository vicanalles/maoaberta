package com.maoaberta.vinicius.maoaberta.presentation.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.TabInformacaoSobre1;
import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.TabInformacaoSobre2;

/**
 * Created by vinicius on 08/11/17.
 */

public class ViewPagerConteudoSobreAdapter extends FragmentStatePagerAdapter{

    public ViewPagerConteudoSobreAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0){
            return new TabInformacaoSobre1();
        }else{
            return new TabInformacaoSobre2();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
