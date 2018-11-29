package com.example.a8306_03.graduationapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by 8306-03 on 2018-11-21.
 */

public class TabAdapter extends FragmentPagerAdapter {
    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                listsFragment listsFragment = new listsFragment();
                return listsFragment;
            case 1:
                findFragment findFragment = new findFragment();
                return findFragment;
            case 2:
                mypageFragment mypageFragment = new mypageFragment();
                return mypageFragment;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "lists";
            case 1:
                return "find";
            case 2:
                return "mypage";

            default:
                return null;
        }
    }
}
