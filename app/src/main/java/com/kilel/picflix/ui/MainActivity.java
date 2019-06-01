package com.kilel.picflix.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
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

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kilel.picflix.BuildConfig;
import com.kilel.picflix.R;
import com.kilel.picflix.adapters.RecyclerViewAdapter;
import com.kilel.picflix.api.ApiClient;
import com.kilel.picflix.api.UnsplashInterface;
import com.kilel.picflix.model.UnsplashAPIResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String API_KEY = BuildConfig.ApiKey;
    public static final int PER_PAGE = 30;
    public static final String PREFERENCES_SEARCH_KEY = "image-search";
    private List<UnsplashAPIResponse> photos = new ArrayList<>();

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String mRecentSearch;

    List<AuthUI.IdpConfig> providers;
    private static final int MY_REQUEST_CODE = 3002;

    RecyclerViewAdapter adapter;

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        showSignInOptions();

        ButterKnife.bind(this);

        adapter = new RecyclerViewAdapter(photos, MainActivity.this);
        mRecyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 1);
        mRecyclerView.setLayoutManager(layoutManager);
        loadJson("");
    }

    private void showSignInOptions(){
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.MyTheme)
                        .build(), MY_REQUEST_CODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_REQUEST_CODE){
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(this, "" + user.getEmail(), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "" + response.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        inflater.inflate(R.menu.menu_main, menu);
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
//                    loadJson(s);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
//                loadJson(s);
                UnsplashInterface apiInterface = ApiClient.getApiClient().create(UnsplashInterface.class);

                apiInterface.getSearchPhotos(API_KEY, PER_PAGE, s).enqueue(new Callback<List<UnsplashAPIResponse>>() {
                    @Override
                    public void onResponse(Call<List<UnsplashAPIResponse>> call, Response<List<UnsplashAPIResponse>> response) {
                        if (response.isSuccessful() && response.body() != null){
                            adapter.swapData(response.body());
                        } else {
                            Toast.makeText(MainActivity.this, "No Result!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<UnsplashAPIResponse>> call, Throwable t) {
                    }
                });
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout){
            logout();
            return true;
        }
        if (id == R.id.action_savedImages){
            showSaved();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        showSignInOptions();
    }

    public void showSaved(){
        Intent intent = new Intent(MainActivity.this, SavedImageListActivity.class);
        startActivity(intent);
    }

    public void loadJson(final String keyword){
        UnsplashInterface apiInterface = ApiClient.getApiClient().create(UnsplashInterface.class);
        Call<List<UnsplashAPIResponse>> call;

        if (keyword.length() != 0) {
            call = apiInterface.getSearchPhotos(API_KEY, PER_PAGE, keyword);
        }
        else {
            call = apiInterface.getPhotos(API_KEY, PER_PAGE);
        }

        call.enqueue(new Callback<List<UnsplashAPIResponse>>() {
            @Override
            public void onResponse(Call<List<UnsplashAPIResponse>> call, Response<List<UnsplashAPIResponse>> response) {
                if (response.isSuccessful() && response.body() != null){
                    adapter.swapData(response.body());
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
