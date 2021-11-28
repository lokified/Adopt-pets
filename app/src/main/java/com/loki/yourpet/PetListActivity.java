package com.loki.yourpet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PetListActivity extends AppCompatActivity{

    @BindView(R.id.petList) ListView petList;

    @BindView(R.id.welcomeTextView) TextView profileName;

    String[] petName ={
            "Dog","Cat",
            "Turtle","Mice",
            "Dog","Dog","cat",
            "chiwawa","German Shepherd"
    };

    String[] location ={
            "Nakuru","Mombasa",
            "Thika","Roysambu",
            "Utawala","Umoja","Kilimani",
            "Kitusuru","Loresho"
    };

    Integer[] imgid={
            R.drawable.logo,R.drawable.logo,
            R.drawable.logo,R.drawable.logo,
            R.drawable.logo,R.drawable.logo,
            R.drawable.logo,R.drawable.logo,
            R.drawable.logo
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);
        ButterKnife.bind(this);

        PetListAdapter adapter = new PetListAdapter(this,petName,location,imgid);
        petList.setAdapter(adapter);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        profileName.setText("Welcome \n" + name + "!");

    }

}