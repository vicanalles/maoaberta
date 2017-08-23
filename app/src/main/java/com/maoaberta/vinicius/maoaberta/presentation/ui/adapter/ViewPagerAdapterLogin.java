package com.maoaberta.vinicius.maoaberta.presentation.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.TabCadastroCliente;
import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.TabCadastroOrganizacao;
import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.TabLoginCliente;
import com.maoaberta.vinicius.maoaberta.presentation.ui.fragment.TabLoginOrganizacao;

/**
 * Created by Vinicius Canalles on 16/08/2017.
 */

public class ViewPagerAdapterLogin extends FragmentStatePagerAdapter{

    CharSequence titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapterCadastro is created
    int numbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapterCadastro is created

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapterLogin(FragmentManager fm, CharSequence titles[], int numbOfTabsumb) {
        super(fm);
        this.titles = titles;
        this.numbOfTabs = numbOfTabsumb;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if(position == 0){ // if the position is 0 we are returning the First tab
            TabLoginCliente tabLoginCliente = new TabLoginCliente();
            return tabLoginCliente;
        }else{ // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
            TabLoginOrganizacao tabLoginOrganizacao = new TabLoginOrganizacao();
            return tabLoginOrganizacao;
        }
    }

    // This method return the titles for the Tabs in the Tab Strip
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    // This method return the Number of tabs for the tabs Strip
    @Override
    public int getCount() {
        return numbOfTabs;
    }
}