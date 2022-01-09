package com.loki.yourpet.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import com.loki.yourpet.ui.PetDetailActivity;
import com.loki.yourpet.util.ItemTouchHelperViewHolder;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;

public class FireBasePetViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

    View mView;
    Context mContext;
    public ImageView mPetImageView;

    public FireBasePetViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
    }

    public void bindAnimal(Animal animal) {

        mPetImageView = (ImageView) mView.findViewById(R.id.myPetImageView);
        TextView mPetName =(TextView) mView.findViewById(R.id.petName);
        TextView mPetDescription = (TextView) mView.findViewById(R.id.descriptionPetText);

        //Picasso.get().load(animal.getPrimaryPhotoCropped().getMedium()).into(mPetImageView);
        mPetName.setText(animal.getName());
        mPetDescription.setText(animal.getDescription());
    }

    @Override
    public void onItemSelected(){
        //Log.d("Animation", "onItemSelected");
        // we will add animations here
        itemView.animate()
                .alpha(0.7f)
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(500);
    }

    @Override
    public void onItemClear(){
        //Log.d("Animation", "onItemClear");
        // we will add animations here
        itemView.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f);
    }
}
