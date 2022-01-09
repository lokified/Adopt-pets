package com.loki.yourpet.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.Query;
import com.loki.yourpet.Constants;
import com.loki.yourpet.R;
import com.loki.yourpet.models.Animal;
import com.loki.yourpet.ui.PetDetailActivity;
import com.loki.yourpet.ui.PetDetailFragment;
import com.loki.yourpet.util.ItemTouchHelperAdapter;
import com.loki.yourpet.util.OnSelectedPetListener;
import com.loki.yourpet.util.OnStartDragListener;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdoptPetListAdapter extends RecyclerView.Adapter<AdoptPetListAdapter.PetViewHolder>{

    private List<Animal> mAnimals;
    private Context mContext;
    private OnSelectedPetListener mOnSelectedPetListener;

    public AdoptPetListAdapter(Context context, List<Animal> animals, OnSelectedPetListener onSelectedPetListener) {
        mContext = context;
        mAnimals = animals;
        mOnSelectedPetListener = onSelectedPetListener;
    }

    @Override
    public AdoptPetListAdapter.PetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adopt_pet_list_item,parent,false);
        PetViewHolder viewHolder = new PetViewHolder(view, mAnimals,mOnSelectedPetListener);
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
        private int mOrientation;

        public PetViewHolder(View itemView, List<Animal> animals, OnSelectedPetListener onSelectedPetListener) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            mContext = itemView.getContext();
            mAnimals = animals;
            mOnSelectedPetListener = onSelectedPetListener;


            //determines the current orientation
            mOrientation = itemView.getResources().getConfiguration().orientation;

            if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                createDetailFragment(0);
            }

            itemView.setOnClickListener(this);
        }

        public void bindAnimal(Animal animal) {
            //Picasso.get().load(animal.getPrimaryPhotoCropped().getMedium()).into(mPetImage);
            mPetName.setText(animal.getName());
            mPetDescription.setText(animal.getDescription());
        }

        @Override
        public void onClick(View v) {
            int itemPosition = getLayoutPosition();
            mOnSelectedPetListener.onPetSelected(itemPosition, mAnimals, Constants.SOURCE_FIND);

            if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                createDetailFragment(itemPosition);
            }
            else {
                Intent intent = new Intent(mContext, PetDetailActivity.class);
                intent.putExtra(Constants.EXTRA_KEY_POSITION, itemPosition);
                intent.putExtra(Constants.EXTRA_KEY_ANIMALS, Parcels.wrap(mAnimals));
                intent.putExtra(Constants.KEY_SOURCE, Constants.SOURCE_FIND);
                mContext.startActivity(intent);
            }
        }

        private void createDetailFragment(int position) {
            PetDetailFragment detailFragment = PetDetailFragment.newInstance(mAnimals, position, Constants.SOURCE_FIND);

            FragmentTransaction ft = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.petDetailContainer, detailFragment);
            ft.commit();
        }

    }
}
