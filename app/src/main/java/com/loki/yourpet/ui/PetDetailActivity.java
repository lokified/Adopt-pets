package com.loki.yourpet.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.loki.yourpet.R;
import com.loki.yourpet.adapter.PetPagerAdapter;
import com.loki.yourpet.models.Animal;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PetDetailActivity extends AppCompatActivity {

    @BindView(R.id.viewPager) ViewPager mViewPager;

    private PetPagerAdapter adapterViewPager;

    List<Animal> mAnimals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_detail);

        ButterKnife.bind(this);

        mAnimals = Parcels.unwrap(getIntent().getParcelableExtra("animals"));
        int startingPosition = getIntent().getIntExtra("position",0);

        adapterViewPager = new PetPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mAnimals);
        mViewPager.setAdapter(adapterViewPager);
        mViewPager.setCurrentItem(startingPosition);
    }
}