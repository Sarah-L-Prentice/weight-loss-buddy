package com.prenticeweb.weightlossbuddy.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ScreenSlidePagerAdapter extends FragmentStateAdapter {
    private final int totalTabs;

    public ScreenSlidePagerAdapter(FragmentActivity fa, int totalTabs) {
        super(fa);
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0:
                return new Tab1();
            case 1:
                return new Tab2();
            case 2:
                return new Tab3();
            default:
                return new Tab1();
        }
    }

    @Override
    public int getItemCount() {
        return totalTabs;
    }
}
