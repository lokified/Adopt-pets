package com.loki.yourpet.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loki.yourpet.Constants;
import com.loki.yourpet.adapter.AdoptPetListAdapter;
import com.loki.yourpet.R;
import com.loki.yourpet.models.Animal;
import com.loki.yourpet.models.Animals;
import com.loki.yourpet.network.PetFinderAPI;
import com.loki.yourpet.network.PetFinderClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetListActivity extends AppCompatActivity{

    private SharedPreferences mSharedPreference;
    private SharedPreferences.Editor mEditor;
    private String mRecentPet;

    @BindView(R.id.recyclerView) RecyclerView petRecyclerView;
    @BindView(R.id.errorTextView) TextView mErrorTextView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    @BindView(R.id.welcomeTextView) TextView profileName;


    private AdoptPetListAdapter mAdapter;
    public List<Animal> animals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        profileName.setText("Welcome \n" + name + "!");

        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(this);
        mRecentPet = mSharedPreference.getString(Constants.PET_TYPE_KEY, null);


        //connects to API
        PetFinderAPI client = PetFinderClient.getClient();

        Call<Animals> call = client.getAnimals(mRecentPet);

        call.enqueue(new Callback<Animals>() {
            @Override
            public void onResponse(Call<Animals> call, Response<Animals> response) {

                hideProgressBar();
                if (response.isSuccessful()) {
                    animals = response.body().getAnimals();

                    mAdapter= new AdoptPetListAdapter(PetListActivity.this,animals);
                    petRecyclerView.setAdapter(mAdapter);

                    RecyclerView.LayoutManager layoutManager =
                            new LinearLayoutManager(PetListActivity.this);
                    petRecyclerView.setLayoutManager(layoutManager);
                    petRecyclerView.setHasFixedSize(true);

                    showAnimals();
                }

                else {
                    showUnsuccessfulMessage();
                }

            }

            @Override
            public void onFailure(Call<Animals> call, Throwable t) {
                Log.e("Error Message", "onFailure: ",t );
                hideProgressBar();
                showFailureMessage();
            }
        });
    }

   //shows progress to the user
    private void showFailureMessage() {
        mErrorTextView.setText("Something went wrong. Please check your Internet connection and try again later");
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void showUnsuccessfulMessage() {
        mErrorTextView.setText("Something went wrong. Please try again later");
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void showAnimals() {
        petRecyclerView.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    //adds to shared preference
    private void addToSharedPreference(String type) {
        mEditor.putString(Constants.PET_TYPE_KEY,type).apply();
    }

}