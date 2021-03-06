package com.loki.yourpet.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.loki.yourpet.models.Animal;
import com.loki.yourpet.ui.PetDetailFragment;

import java.util.List;

public class PetPagerAdapter extends FragmentPagerAdapter {

    private List<Animal> mAnimals;
    private String mSource;


    public PetPagerAdapter(@NonNull FragmentManager fm, int behavior, List<Animal> animals, String source) {
        super(fm, behavior);
        mAnimals = animals;
        mSource = source;
    }

    @Override
    public Fragment getItem(int position) {
        return PetDetailFragment.newInstance(mAnimals, position, mSource);
    }

    @Override
    public int getCount() {
        return mAnimals.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mAnimals.get(position).getName();
    }
}
