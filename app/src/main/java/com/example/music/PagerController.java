package com.example.music;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerController extends FragmentPagerAdapter {

    int tabCount;

    public PagerController(@NonNull FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return AllSongs.getInstance();
            case 1:
                return NowPlay.getInstance();
            //case 2:
                //return new PlayList();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
