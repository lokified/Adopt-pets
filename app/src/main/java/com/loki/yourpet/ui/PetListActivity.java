package com.loki.yourpet.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.content.SharedPreferences;
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

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

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


        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(this);
        mRecentPet = mSharedPreference.getString(Constants.PET_TYPE_KEY, null);

        if (mRecentPet != null) {
            fetchPets(mRecentPet);
        }

        //display name of user
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    profileName.setText("Welcome \n "+ user.getDisplayName());
                }
            }
        };


    }

    //inflate the search menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        inflater.inflate(R.menu.menu_main,menu);
        ButterKnife.bind(this);

        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreference.edit();

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String type) {
                addToSharedPreference(type);
                fetchPets(type);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;

    }

    //connects to API
    public void fetchPets(String type) {
        PetFinderAPI client = PetFinderClient.getClient();

        Call<Animals> call = client.getAnimals(type);

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

    //selects the log out item in menu
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if(id == R.id.action_logout) {
            logOut();
            Toast.makeText(getApplicationContext(), "You are logged out", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }


    //logs out the user
    public void logOut() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(PetListActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}