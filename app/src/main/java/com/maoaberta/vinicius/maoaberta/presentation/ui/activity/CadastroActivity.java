package com.maoaberta.vinicius.maoaberta.presentation.ui.activity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.maoaberta.vinicius.maoaberta.R;
import com.maoaberta.vinicius.maoaberta.presentation.ui.adapter.ViewPagerAdapter;
import com.maoaberta.vinicius.maoaberta.util.SlidingTabLayout;

/**
 * Created by Vinicius Canalles on 16/08/2017.
 */

public class CadastroActivity extends AppCompatActivity {

    ViewPager pager;
    ViewPagerAdapter adapter;
    TabLayout tabs;
    CharSequence titles[]={"Cliente","Organização"};
    int numbOftabs =2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),titles,numbOftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setTabTextColors(ColorStateList.valueOf(Color.WHITE));
        tabs.setSelectedTabIndicatorColor(Color.WHITE);
        tabs.setupWithViewPager(pager);

        //Definição da fonte e da cor do texto das tabs
        for(int i = 0; i < tabs.getTabCount(); i++){
            TextView tv = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tv.setTypeface(Typeface.SANS_SERIF);
            tabs.getTabAt(i).setCustomView(tv);
        }
    }
}
