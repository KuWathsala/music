package com.kuwathsala.music;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerController extends FragmentPagerAdapter {

    AllSongs allSongs;
    NowPlay nowPlay;
    int tabCount;

    public PagerController(@NonNull FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
        this.allSongs = AllSongs.getInstance();
        this.nowPlay = NowPlay.getInstance();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return allSongs;
            case 1:
                return nowPlay;
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
