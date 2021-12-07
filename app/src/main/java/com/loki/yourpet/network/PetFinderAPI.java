package com.loki.yourpet.network;

import com.loki.yourpet.models.Animals;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PetFinderAPI {

    @GET("animals")
    Call<Animals> getAnimals(
            @Query("type") String type
    );
}
