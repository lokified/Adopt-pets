package com.loki.yourpet.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loki.yourpet.Constants;
import com.loki.yourpet.R;
import com.loki.yourpet.adapter.FireBasePetViewHolder;
import com.loki.yourpet.models.Animal;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedPetListActivity extends AppCompatActivity {

    private DatabaseReference mPetReference;
    private FirebaseRecyclerAdapter<Animal, FireBasePetViewHolder> mFireBaseAdapter;

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);
        ButterKnife.bind(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        mPetReference = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_PET)
                .child(uid);
        setUpFireBaseAdapter();

        hideProgressBar();
        showPets();
    }


    private void setUpFireBaseAdapter() {
        FirebaseRecyclerOptions<Animal> options =
                new FirebaseRecyclerOptions.Builder<Animal>()
                .setQuery(mPetReference,Animal.class)
                .build();

        mFireBaseAdapter = new FirebaseRecyclerAdapter<Animal, FireBasePetViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FireBasePetViewHolder holder,
                                            int position, @NonNull Animal animal) {
                holder.bindAnimal(animal);
            }

            @NonNull
            @Override
            public FireBasePetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adopt_pet_list_item,parent,false);
                return new FireBasePetViewHolder(view);
            }
        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mFireBaseAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFireBaseAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mFireBaseAdapter!= null) {
            mFireBaseAdapter.stopListening();
        }
    }

    private void showPets() {
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }
}