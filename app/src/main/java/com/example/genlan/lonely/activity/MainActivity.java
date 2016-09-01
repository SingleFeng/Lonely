package com.example.genlan.lonely.activity;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.genlan.lonely.R;
import com.example.genlan.lonely.activity.mainfragment.FourthFragment;
import com.example.genlan.lonely.activity.mainfragment.MusicFragment;
import com.example.genlan.lonely.activity.mainfragment.ThirdFragment;
import com.example.genlan.lonely.activity.mainfragment.WeatherFragment;
import com.example.genlan.lonely.adapter.MainFragmentAdapter;
import com.example.genlan.lonely.util.LogUtil;
import com.example.genlan.lonely.util.ScreenUtil;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements WeatherFragment.OnFragmentInteractionListener, MusicFragment.OnFragmentInteractionListener,ThirdFragment.OnFragmentInteractionListener,FourthFragment.OnFragmentInteractionListener {

    WeatherFragment no1Fg;
    MusicFragment no2Fg;
    ThirdFragment no3Fg;
    FourthFragment no4Fg;
    ScreenUtil mScreen;
    private int mScreenX = 0;
    private int mScreenY = 0;
    private ViewPager viewPager;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        LogUtil.d("-------------------onCreate---------------------");
        getScreenParam();
        initFragment();
        initViewPager();
        initTabPage();
    }


    /**
     * 获取屏幕属性
     * */
    private void getScreenParam() {
        mScreen = new ScreenUtil(MainActivity.this);
        mScreenX = mScreen.getScreenWidth();
        mScreenY = mScreen.getScreenHeigth();
    }

    /**
     * 初始化主界面ViewPager
     * */
    private void initViewPager(){
        viewPager = (ViewPager) findViewById(R.id.main_fragment);
        List<Fragment> list = new ArrayList<>();
        list.add(no1Fg);
        list.add(no2Fg);
        list.add(no3Fg);
        list.add(no4Fg);
        MainFragmentAdapter adapter = new MainFragmentAdapter(mFragmentManager,list);
        viewPager.setAdapter(adapter);
    }

    /**
     * 初始化碎片
     * */
    private void initFragment() {
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        no1Fg = WeatherFragment.newInstance("","");
        no2Fg = MusicFragment.newInstance("","");
        no3Fg = ThirdFragment.newInstance("","");
        no4Fg = FourthFragment.newInstance("","");
    }

    /**
     * 设置按钮属性
     * */
    private void setBottomBtn(View view) {
        ViewGroup.LayoutParams param = view.getLayoutParams();
        param.width = mScreenX / 5;
        view.setLayoutParams(param);
    }

    /**
     * 初始化底部标签
     * */
    private void initTabPage(){
        TabPageIndicator tab;
        tab = (TabPageIndicator) findViewById(R.id.rl_main_bottom);
        tab.setViewPager(viewPager);
        tab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
