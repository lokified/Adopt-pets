package com.loki.yourpet.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.loki.yourpet.Constants;
import com.loki.yourpet.R;
import com.loki.yourpet.adapter.AdoptPetListAdapter;
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

public class PetListFragment extends Fragment {

    @BindView(R.id.recyclerView) RecyclerView petRecyclerView;
    @BindView(R.id.errorTextView) TextView mErrorTextView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    @BindView(R.id.welcomeTextView) TextView profileName;

    private SharedPreferences mSharedPreference;
    private SharedPreferences.Editor mEditor;
    private String mRecentPet;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private AdoptPetListAdapter mAdapter;
    public List<Animal> animals;


    public PetListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEditor = mSharedPreference.edit();

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

        // Instructs fragment to include menu options:
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pet_list, container, false);
        ButterKnife.bind(this,view);

        mRecentPet = mSharedPreference.getString(Constants.PET_TYPE_KEY,null);

        if (mRecentPet != null) {
            fetchPets(mRecentPet);
        }



        return view;
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

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter= new AdoptPetListAdapter(getActivity(),animals);
                            petRecyclerView.setAdapter(mAdapter);

                            RecyclerView.LayoutManager layoutManager =
                                    new LinearLayoutManager(getActivity());
                            petRecyclerView.setLayoutManager(layoutManager);
                            petRecyclerView.setHasFixedSize(true);

                        }
                    });

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

    //inflate the search menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);

        inflater.inflate(R.menu.menu_search, menu);
        inflater.inflate(R.menu.menu_main,menu);

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

    }

    //selects the log out item in menu
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if(id == R.id.action_logout) {
            logOut();
            Toast.makeText(getContext(), "You are logged out", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (id == R.id.savedPets) {
            Intent intent = new Intent(getActivity(), SavedPetListActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    //logs out the user
    public void logOut() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
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