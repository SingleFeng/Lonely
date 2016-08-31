package com.example.genlan.lonely.adapter;

        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentPagerAdapter;

        import java.util.List;

/**
 * Created by GenLan on 2016/8/26.
 */

public class MainFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;

    private static final String[] TITLE = new String[]{"Weather", "Music", "third", "fourth"};

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLE[position % TITLE.length].toUpperCase();
    }

    public MainFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
