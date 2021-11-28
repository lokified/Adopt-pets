package com.loki.yourpet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PetListActivity extends AppCompatActivity{

    @BindView(R.id.welcomeTextView) TextView profileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);
        ButterKnife.bind(this);



        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        profileName.setText("Welcome \n" + name + "!");

    }

}