package com.openclassrooms.realestatemanager.data;

import com.openclassrooms.realestatemanager.data.model.remote.PlacesNearbyResult;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesService {

    @GET("maps/api/place/nearbysearch/json?radius=300&type=establishment")
    Observable<PlacesNearbyResult> getNearbyPlaces(@Query("key") String apiKey, @Query("location") String location);

    @GET("maps/api/place/nearbysearch/json?radius=300&type=establishment")
    Observable<PlacesNearbyResult> getNearbyPlacesNextPage(@Query("key") String apiKey, @Query("pagetoken") String pageToken);

    Retrofit retrofitGsonConverter = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
}
