package com.loki.yourpet.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.loki.yourpet.Constants;
import com.loki.yourpet.R;
import com.loki.yourpet.models.Animal;
import com.loki.yourpet.ui.PetDetailActivity;
import com.loki.yourpet.ui.PetDetailFragment;
import com.loki.yourpet.util.ItemTouchHelperAdapter;
import com.loki.yourpet.util.OnStartDragListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;

public class FirebasePetListAdapter extends FirebaseRecyclerAdapter<Animal, FireBasePetViewHolder> implements ItemTouchHelperAdapter {

    private Context mContext;
    private ArrayList<Animal> mAnimals = new ArrayList<>();
    private OnStartDragListener mOnStartDragListener;
    private Query mRef;
    private ChildEventListener mChildEventListener;
    private int mOrientation;

    public FirebasePetListAdapter(FirebaseRecyclerOptions<Animal> options, Query ref, OnStartDragListener onStartDragListener, Context context) {
        super(options);
        mRef = ref.getRef();
        mOnStartDragListener = onStartDragListener;
        mContext = context;

        mChildEventListener = mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                mAnimals.add(snapshot.getValue(Animal.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onBindViewHolder(@NonNull FireBasePetViewHolder fireBasePetViewHolder, int position, @NonNull Animal animal) {
        fireBasePetViewHolder.bindAnimal(animal);

        mOrientation = fireBasePetViewHolder.itemView.getResources().getConfiguration().orientation;

        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            createDetailFragment(0);
        }

        fireBasePetViewHolder.mPetImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    mOnStartDragListener.onStartDrag(fireBasePetViewHolder);
                }
                return false;
            }
        });

        fireBasePetViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = fireBasePetViewHolder.getAdapterPosition();

                if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    createDetailFragment(itemPosition);
                }
                else {
                    Intent intent = new Intent(mContext, PetDetailActivity.class);
                    intent.putExtra(Constants.EXTRA_KEY_POSITION, itemPosition);
                    intent.putExtra(Constants.EXTRA_KEY_ANIMALS, Parcels.wrap(mAnimals));
                    intent.putExtra(Constants.KEY_SOURCE, Constants.SOURCE_SAVED);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    private void createDetailFragment(int position) {
        PetDetailFragment detailFragment = PetDetailFragment.newInstance(mAnimals, position, Constants.SOURCE_SAVED);

        FragmentTransaction ft = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.petDetailContainer, detailFragment);
        ft.commit();
    }

    @NonNull
    @Override
    public FireBasePetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_list_item_drag, parent, false);
        return new FireBasePetViewHolder(view);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition){
        Collections.swap(mAnimals,fromPosition,toPosition);
        notifyItemMoved(fromPosition,toPosition);
        setIndexInFirebase();
        return false;
    }

    @Override
    public void onItemDismiss(int position){
        mAnimals.remove(position);
        getRef(position).removeValue();
    }

    private void setIndexInFirebase() {
        for (Animal animal : mAnimals) {
            int index = mAnimals.indexOf(animal);
            DatabaseReference ref = getRef(index);
            ref.child("index").setValue(Integer.toString(index));
        }
    }

    @Override
    public void stopListening() {
        super.stopListening();
        mRef.removeEventListener(mChildEventListener);
    }
}
