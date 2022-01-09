package com.loki.yourpet.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.loki.yourpet.Constants;
import com.loki.yourpet.adapter.AdoptPetListAdapter;
import com.loki.yourpet.R;
import com.loki.yourpet.models.Animal;
import com.loki.yourpet.models.Animals;
import com.loki.yourpet.network.PetFinderAPI;
import com.loki.yourpet.network.PetFinderClient;
import com.loki.yourpet.util.OnSelectedPetListener;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetListActivity extends AppCompatActivity implements OnSelectedPetListener {

    private Integer mPosition;
    private List<Animal> mAnimals;
    private String mSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);

        if (savedInstanceState != null) {

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mPosition = savedInstanceState.getInt(Constants.EXTRA_KEY_POSITION);
                mAnimals = Parcels.unwrap(savedInstanceState.getParcelable(Constants.EXTRA_KEY_ANIMALS));

                if (mPosition != null && mAnimals != null) {
                    Intent intent = new Intent(this, PetDetailActivity.class);
                    intent.putExtra(Constants.EXTRA_KEY_POSITION, mPosition);
                    intent.putExtra(Constants.EXTRA_KEY_ANIMALS, Parcels.wrap(mAnimals));
                    intent.putExtra(Constants.KEY_SOURCE, mSource);
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mPosition != null && mAnimals != null) {
            outState.putInt(Constants.EXTRA_KEY_POSITION, mPosition);
            outState.putParcelable(Constants.EXTRA_KEY_ANIMALS, Parcels.wrap(mAnimals));
            outState.putString(Constants.KEY_SOURCE, mSource);
        }
    }

    @Override
    public void onPetSelected(Integer position, List<Animal> animals, String source) {
        mPosition = position;
        mAnimals = animals;
        mSource = source;
    }
}