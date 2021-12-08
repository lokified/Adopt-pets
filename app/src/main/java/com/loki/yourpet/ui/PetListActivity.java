package com.loki.yourpet.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class PetListActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.recyclerView) RecyclerView petRecyclerView;
    @BindView(R.id.errorTextView) TextView mErrorTextView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    @BindView(R.id.welcomeTextView) TextView profileName;
    @BindView(R.id.searchButton) Button mSearchButton;
    @BindView(R.id.editTextPetType) EditText mPetType;


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

        mSearchButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v == mSearchButton) {
            hideProgressBar();

            String type = mPetType.getEditableText().toString();

            PetFinderAPI client = PetFinderClient.getClient();

            Call<Animals> call = client.getAnimals(type);

            call.enqueue(new Callback<Animals>() {
                @Override
                public void onResponse(Call<Animals> call, Response<Animals> response) {

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
    }

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

}