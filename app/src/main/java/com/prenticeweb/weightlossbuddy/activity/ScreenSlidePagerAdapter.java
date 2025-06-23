package com.prenticeweb.weightlossbuddy.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.prenticeweb.weightlossbuddy.calculations.WeightConverter;
import com.prenticeweb.weightlossbuddy.room.entity.WeightMeasurement;
import com.prenticeweb.weightlossbuddy.unit.weight.Kilogram;
import com.prenticeweb.weightlossbuddy.unit.weight.Pound;
import com.prenticeweb.weightlossbuddy.unit.weight.StoneAndPounds;

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
                return new ViewWeightMeasurementsFragment((WeightMeasurement wm) -> new Kilogram(wm.getWeightKg()));
            case 1:
                return new ViewWeightMeasurementsFragment((WeightMeasurement wm) -> new Pound(wm.getWeightLb()));
            case 2:
                return new ViewWeightMeasurementsFragment((WeightMeasurement wm) -> WeightConverter.convertPoundsToStoneAndPounds(new Pound(wm.getWeightLb())));
            default:
                return new ViewWeightMeasurementsFragment((WeightMeasurement wm) -> new Kilogram(wm.getWeightKg()));
        }
    }

    @Override
    public int getItemCount() {
        return totalTabs;
    }
}
