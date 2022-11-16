package com.acube.jims.presentation.Report;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.acube.jims.databinding.FragmentZoneReportBinding;
import com.acube.jims.presentation.Report.ViewModel.ZoneReportFragment;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public SampleFragmentPagerAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                StoreReportFragment homeFragment = new StoreReportFragment();
                return homeFragment;
            case 1:
                CategoryReportFragment movieFragment = new CategoryReportFragment();
                return movieFragment;
            case 2:

                ZoneReportFragment sportFragment = new ZoneReportFragment();
                return sportFragment;
            default:
                return null;
        }
    }

    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}