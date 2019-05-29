package com.kilel.picflix.ui;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.kilel.picflix.BuildConfig;
import com.kilel.picflix.R;
import com.kilel.picflix.adapters.RecyclerViewAdapter;
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

public class MainActivity extends AppCompatActivity {

    public static final String API_KEY = BuildConfig.ApiKey;
    public static final int LIMIT = 100;
    public static final String PREFERENCES_SEARCH_KEY = "image-search";
    private List<UnsplashAPIResponse> photos = new ArrayList<>();

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String mRecentSearch;

    RecyclerViewAdapter adapter;

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        LoadJson("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.length()>2) {
                    addToSharedPreferences(s);
                    LoadJson(s);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                LoadJson(s);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void LoadJson(final String keyword){
        UnsplashInterface apiInterface = ApiClient.getApiClient().create(UnsplashInterface.class);
        Call<List<UnsplashAPIResponse>> call;

        if (keyword.length() > 0) {
            call = apiInterface.getSearchPhotos(API_KEY, LIMIT, keyword);
        }
        else {
            call = apiInterface.getPhotos(API_KEY, LIMIT);
        }

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

    private void addToSharedPreferences(String location) {
        mEditor.putString(PREFERENCES_SEARCH_KEY, location).apply();
    }
}
