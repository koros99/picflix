package com.kilel.picflix;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.kilel.picflix.api.ApiClient;
import com.kilel.picflix.api.UnsplashInterface;
import com.kilel.picflix.model.UnsplashAPIResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    public static final String API_KEY = BuildConfig.ApiKey;
    private List<UnsplashAPIResponse> photos = new ArrayList<>();

    RecyclerViewAdapter adapter;

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        LoadJson();
    }

    public void LoadJson(){
        UnsplashInterface apiInterface = ApiClient.getApiClient().create(UnsplashInterface.class);
        Call<List<UnsplashAPIResponse>> call = apiInterface.getPhotos(API_KEY);

        call.enqueue(new Callback<List<UnsplashAPIResponse>>() {
            @Override
            public void onResponse(Call<List<UnsplashAPIResponse>> call, Response<List<UnsplashAPIResponse>> response) {
                if (response.isSuccessful() && response.body() != null){

                    if(!photos.isEmpty()){
                        photos.clear();
                    }

                    photos = response.body();

                    adapter = new RecyclerViewAdapter(photos, MainActivity.this);
                    mRecyclerView.setAdapter(adapter);
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 1);
                    mRecyclerView.setLayoutManager(layoutManager);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "No Result!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UnsplashAPIResponse>> call, Throwable t) {
            }
        });

    }
}
