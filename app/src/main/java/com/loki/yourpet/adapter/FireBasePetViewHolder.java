package com.loki.yourpet.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loki.yourpet.Constants;
import com.loki.yourpet.R;
import com.loki.yourpet.models.Animal;
import com.loki.yourpet.ui.PetDetailActivity;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;

public class FireBasePetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mView;
    Context mContext;

    @BindView(R.id.petImageView) ImageView mPetImage;
    @BindView(R.id.petName) TextView mPetName;
    @BindView(R.id.descriptionPetText) TextView mPetDescription;

    public FireBasePetViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);

    }

    public void bindAnimal(Animal animal) {
        //Picasso.get().load(animal.getPrimaryPhotoCropped().getSmall()).into(mPetImage);
        mPetName.setText(animal.getName());
        mPetDescription.setText(animal.getDescription());
    }

    @Override
    public void onClick(View view) {
        final ArrayList<Animal> animals = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_PET);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    animals.add(snapshot.getValue(Animal.class));
                }

                int itemPosition = getLayoutPosition();

                Intent intent = new Intent(mContext, PetDetailActivity.class);
                intent.putExtra("position",itemPosition + "");
                intent.putExtra("pet", Parcels.wrap(animals));

                mContext.startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
