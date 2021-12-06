package com.loki.yourpet.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.loki.yourpet.R;
import com.loki.yourpet.models.Animal;
import com.loki.yourpet.models.Photo;
import com.loki.yourpet.ui.PetDetailActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdoptPetListAdapter extends RecyclerView.Adapter<AdoptPetListAdapter.PetViewHolder> {

    private List<Animal> mAnimals;
    private Context mContext;

    public AdoptPetListAdapter(Context context, List<Animal> animals) {
        mContext = context;
        mAnimals = animals;
    }

    @Override
    public AdoptPetListAdapter.PetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adopt_pet_list_item,parent,false);
        PetViewHolder viewHolder = new PetViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdoptPetListAdapter.PetViewHolder holder, int position) {
        holder.bindAnimal(mAnimals.get(position));
    }

    @Override
    public int getItemCount() {
        return mAnimals.size();
    }

    public class PetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.petImageView) ImageView mPetImage;
        @BindView(R.id.petName) TextView mPetName;
        @BindView(R.id.descriptionPetText) TextView mPetDescription;

        private Context mContext;

        public PetViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(this);
        }

        public void bindAnimal(Animal animal) {
            //Picasso.get().load(animal.getPhotos().get(0).getFull()).into(mPetImage);
            mPetName.setText(animal.getName());
            mPetDescription.setText(animal.getDescription());
        }

        @Override
        public void onClick(View v) {
            int itemPosition = getLayoutPosition();
            Intent intent = new Intent(mContext, PetDetailActivity.class);
            intent.putExtra("position", itemPosition);
            intent.putExtra("animals", Parcels.wrap(mAnimals));
            mContext.startActivity(intent);
        }

    }
}
