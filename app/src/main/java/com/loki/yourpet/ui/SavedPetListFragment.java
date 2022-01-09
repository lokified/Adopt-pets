package com.loki.yourpet.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.database.Query;
import com.loki.yourpet.Constants;
import com.loki.yourpet.R;
import com.loki.yourpet.adapter.FireBasePetViewHolder;
import com.loki.yourpet.adapter.FirebasePetListAdapter;
import com.loki.yourpet.models.Animal;
import com.loki.yourpet.util.OnStartDragListener;
import com.loki.yourpet.util.SimpleItemTouchHelperCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedPetListFragment extends Fragment implements OnStartDragListener {

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.fireProgressBar) ProgressBar mProgressBar;

    private FirebasePetListAdapter mFireBaseAdapter;
    private ItemTouchHelper mItemTouchHelper;

    public SavedPetListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved_pet_list, container, false);
        ButterKnife.bind(this, view);

        setUpFireBaseAdapter();

          hideProgressBar();
          showPets();

        return view;
    }

    private void setUpFireBaseAdapter() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        Query query = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_PET)
                .child(uid)
                .orderByChild(Constants.FIREBASE_QUERY_INDEX);

        FirebaseRecyclerOptions<Animal> options =
                new FirebaseRecyclerOptions.Builder<Animal>()
                        .setQuery(query,Animal.class)
                        .build();

        mFireBaseAdapter = new FirebasePetListAdapter(options, query, this,getActivity());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mFireBaseAdapter);
        mRecyclerView.setHasFixedSize(true);

        mFireBaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                mFireBaseAdapter.notifyDataSetChanged();
            }
        });

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mFireBaseAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onStart() {
        super.onStart();
        mFireBaseAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mFireBaseAdapter!= null) {
            mFireBaseAdapter.stopListening();
        }
    }

    @Override
    //method is now public
    public void onDestroy() {
        super.onDestroy();
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