package com.example.videoeditor.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class StudioPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> frags= new ArrayList<Fragment>();
    private final List<String> titles= new ArrayList<String>();
    public StudioPagerAdapter(@NonNull FragmentManager fm) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override

    public Fragment getItem(int position) {
        return frags.get(position);

    }

    @Override
    public int getCount() {
        return frags.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
    public void addFragment(Fragment f, String title){
        frags.add(f);
        titles.add(title);
    }
}

