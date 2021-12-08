package com.loki.yourpet.ui;

import android.content.Intent;
import android.net.Uri;
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

public class PetDetailFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.petImage) ImageView mPetImageLabel;
    @BindView(R.id.petNameTextView) TextView mPetNameLabel;
    @BindView(R.id.ageTextView) TextView mPetAgeLabel;
    @BindView(R.id.genderTextView) TextView mPetGenderLabel;
    @BindView(R.id.sizeTextView) TextView mPetSizeLabel;
    @BindView(R.id.speciesTextView) TextView mPetSpeciesLabel;
    @BindView(R.id.petDescriptionTextView) TextView mDescriptionLabel;
    @BindView(R.id.statusTextView) TextView mStatusLabel;
    @BindView(R.id.websiteTextView) TextView mWebsiteLabel;
    @BindView(R.id.phoneTextView) TextView mPhoneLabel;
    @BindView(R.id.emailTextView) TextView mEmailLabel;
    @BindView(R.id.addressTextView) TextView mAddressLabel;
    @BindView(R.id.breedTextView) TextView mBreedLabel;

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

        //Picasso.get().load(mAnimal.getPrimaryPhotoCropped().getSmall()).into(mPetImageLabel);

        mPetNameLabel.setText("Name : " + mAnimal.getName());
        mPetAgeLabel.setText("Age : "+ mAnimal.getAge());
        mPetGenderLabel.setText("Gender : "+mAnimal.getGender());
        mPetSizeLabel.setText("Size : "+mAnimal.getSize());
        mPetSpeciesLabel.setText("Species : "+ mAnimal.getSpecies());
        mBreedLabel.setText("Breed : "+ mAnimal.getBreeds().getPrimary());
        mDescriptionLabel.setText("Description : "+ mAnimal.getDescription());
        mAddressLabel.setText(mAnimal.getContact().getAddress().toString());
        mEmailLabel.setText(mAnimal.getContact().getEmail());
        mStatusLabel.setText(mAnimal.getStatus());
        mPhoneLabel.setText(mAnimal.getContact().getPhone());

        mWebsiteLabel.setOnClickListener(this);
        mPhoneLabel.setOnClickListener(this);

        return view;
    }

    //implicit intents
    @Override
    public void onClick(View v) {
        if (v == mWebsiteLabel) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(mAnimal.getUrl()));
            startActivity(webIntent);
        }

        if (v == mPhoneLabel) {
            Intent phoneIntent = new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:" + mAnimal.getContact().getPhone()));
            startActivity(phoneIntent);
        }
    }
}