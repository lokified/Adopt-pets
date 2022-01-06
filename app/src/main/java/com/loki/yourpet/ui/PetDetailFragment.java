package com.loki.yourpet.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loki.yourpet.Constants;
import com.loki.yourpet.R;
import com.loki.yourpet.models.Animal;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

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
    @BindView(R.id.adoptPetButton) Button mAdoptPetButton;

    private List<Animal> mAnimals;
    private Animal mAnimal;
    private String mSource;
    private int mPosition;

    public PetDetailFragment() {
        // Required empty public constructor
    }

    public static PetDetailFragment newInstance(List<Animal> animal, int position, String source) {
        PetDetailFragment fragment = new PetDetailFragment();
        Bundle args = new Bundle();

        args.putParcelable(Constants.EXTRA_KEY_ANIMALS, Parcels.wrap(animal));
        args.putInt(Constants.EXTRA_KEY_POSITION, position);
        args.putString(Constants.KEY_SOURCE, source);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert  getArguments() != null;
        mAnimals = Parcels.unwrap(getArguments().getParcelable(Constants.EXTRA_KEY_ANIMALS));
        mPosition = getArguments().getInt(Constants.EXTRA_KEY_POSITION);
        mAnimal = mAnimals.get(mPosition);
        mSource = getArguments().getString(Constants.KEY_SOURCE);

        setHasOptionsMenu(true);
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

        //hides or show save button
        if (mSource.equals(Constants.SOURCE_SAVED)) {
            mAdoptPetButton.setVisibility(View.GONE);
        }
        else {
            mAdoptPetButton.setOnClickListener(this);
        }

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

        //save button
        if (v == mAdoptPetButton) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();
            DatabaseReference petRef = FirebaseDatabase
                    .getInstance()
                    .getReference(Constants.FIREBASE_CHILD_PET)
                    .child(uid);

            //notify if pet is already saved
            String name = mAnimal.getName();

            petRef.orderByChild("name").equalTo(name).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        Toast.makeText(getContext(), "Currently Selected Restaurant already exists", Toast.LENGTH_LONG).show();
                        return;
                    }
                    else {
                        DatabaseReference pushRef = petRef.push();
                        String pushId = pushRef.getKey();
                        mAnimal.setPushId(pushId);
                        pushRef.setValue(mAnimal);
                        Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
    }
}