package com.loki.yourpet.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loki.yourpet.R;
import com.loki.yourpet.models.Animal;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PetDetailFragment extends Fragment {

    @BindView(R.id.petImage) ImageView mPetImageLabel;
    @BindView(R.id.petNameTextView) TextView mPetNameLabel;
    @BindView(R.id.ageTextView) TextView mPetAgeLabel;
    @BindView(R.id.genderTextView) TextView mPetGenderLabel;
    @BindView(R.id.sizeTextView) TextView mPetSizeLabel;
    @BindView(R.id.petDescriptionTextView) TextView mDescriptionLabel;

    private Animal mAnimal;

    public PetDetailFragment() {
        // Required empty public constructor
    }

    public static PetDetailFragment newInstance(Animal animal) {
        PetDetailFragment fragment = new PetDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("animals", Parcels.wrap(animal));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert  getArguments() != null;
        mAnimal = Parcels.unwrap(getArguments().getParcelable("animals"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet_detail,container,false);
        ButterKnife.bind(this,view);

        Picasso.get().load(mAnimal.getPhotos().get(0).getLarge()).into(mPetImageLabel);
        mPetNameLabel.setText("Name :" + mAnimal.getName());
        mPetAgeLabel.setText("Age :"+ mAnimal.getAge());
        mPetGenderLabel.setText("Gender :"+mAnimal.getGender());
        mPetSizeLabel.setText("Size :"+mAnimal.getSize());
        mDescriptionLabel.setText("Description :"+ mAnimal.getDescription());

        return view;
    }
}