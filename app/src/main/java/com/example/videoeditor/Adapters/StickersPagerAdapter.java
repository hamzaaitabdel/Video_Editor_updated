package com.example.videoeditor.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class StickersPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> fragments= new ArrayList<>();
    public void addFragment(Fragment f){
        fragments.add(f);
    }

    public StickersPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public StickersPagerAdapter(FragmentManager childFragmentManager) {
        super(childFragmentManager);

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
