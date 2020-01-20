package com.example.myapplication.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myapplication.NagraniaFragment;
import com.example.myapplication.NotatkaEntity;
import com.example.myapplication.R;
import com.example.myapplication.TextEditFragment;
import com.example.myapplication.ZdjeciaFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;
    private NotatkaEntity notatka;

    public SectionsPagerAdapter(Context context, FragmentManager fm, NotatkaEntity notatka) {
        super(fm);
        mContext = context;
        this.notatka = notatka;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment = null;
        switch(position) {
            case 0:
                fragment = new TextEditFragment(notatka);
                break;
            case 1:
                fragment = new NagraniaFragment();
                break;
            case 2:
                fragment = new ZdjeciaFragment(notatka);
                break;
            default:
                fragment = null;

        }
        return fragment;

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }
}