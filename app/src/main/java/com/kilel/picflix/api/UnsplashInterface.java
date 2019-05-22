package com.kilel.picflix.api;

import com.kilel.picflix.model.UnsplashAPIResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UnsplashInterface {

    @GET("/photos")
    Call<List<UnsplashAPIResponse>> getPhotos(@Query("client_id") String apiKey);
}
