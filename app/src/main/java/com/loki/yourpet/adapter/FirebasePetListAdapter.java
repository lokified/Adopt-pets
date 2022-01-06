package com.loki.yourpet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.loki.yourpet.R;
import com.loki.yourpet.models.Animal;

import java.util.ArrayList;

public class FirebasePetListAdapter extends FirebaseRecyclerAdapter<Animal, FireBasePetViewHolder> {

    private Context mContext;
    private ArrayList<Animal> mAnimals = new ArrayList<>();

    public FirebasePetListAdapter(FirebaseRecyclerOptions<Animal> options, Context context) {
        super(options);
        mContext = context;

    }

    @Override
    protected void onBindViewHolder(@NonNull FireBasePetViewHolder fireBasePetViewHolder, int position, @NonNull Animal animal) {
        fireBasePetViewHolder.bindAnimal(animal);
    }

    @NonNull
    @Override
    public FireBasePetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_pet_list,parent,false);

        return new FireBasePetViewHolder(view);
    }



}
