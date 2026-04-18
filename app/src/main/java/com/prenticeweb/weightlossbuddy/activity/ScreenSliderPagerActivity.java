package com.prenticeweb.weightlossbuddy.activity;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.prenticeweb.weightlossbuddy.R;
import com.prenticeweb.weightlossbuddy.room.entity.KeyInfo;
import com.prenticeweb.weightlossbuddy.room.view.KeyInfoViewModel;
import com.prenticeweb.weightlossbuddy.transformer.ZoomOutPageTransformer;

public class ScreenSliderPagerActivity extends FragmentActivity {
    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;
    private TabLayout tabLayout;
    private KeyInfoViewModel keyInfoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slider);

        // Initialize KeyInfoViewModel
        keyInfoViewModel = new ViewModelProvider(this).get(KeyInfoViewModel.class);

        initPagerAdapter();
        initViewPager();
        initTabLayout();

        // Set initial tab based on PreferredWeightUnit
        setInitialTabBasedOnPreference();

        // Handle back press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                setEnabled(false);
                getOnBackPressedDispatcher().onBackPressed();

            }
        });
    }

    private void initTabLayout() {
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });
    }

    private void initPagerAdapter() {
        pagerAdapter = new ScreenSlidePagerAdapter(this, 3);
    }

    private void initViewPager() {
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(new ZoomOutPageTransformer());
    }


    private void setInitialTabBasedOnPreference() {
        keyInfoViewModel.getReadAll().observe(this, keyInfo -> {
            if (keyInfo != null && keyInfo.getPreferredWeightUnit() != null) {
                int targetTabPosition = getTabPositionForWeightUnit(keyInfo.getPreferredWeightUnit());
                viewPager.setCurrentItem(targetTabPosition);
                tabLayout.getTabAt(targetTabPosition).select();
            }
        });
    }

    private int getTabPositionForWeightUnit(KeyInfo.PreferredWeightUnit preferredWeightUnit) {
        switch (preferredWeightUnit) {
            case KG:
                return 0; // Tab 0: Kg
            case STONE_AND_POUNDS:
                return 1; // Tab 1: Stone and pounds
            case LB:
                return 2; // Tab 2: Lb
            default:
                return 0; // Default to Kg
        }
    }
}
