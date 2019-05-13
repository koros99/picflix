package com.kilel.picflix;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Main Activity";

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        loadFragment(new HomeFragment());

        getImages();
    }

    private void getImages(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls.add("https://images.unsplash.com/photo-1556951348-de7d29704984?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjF9&auto=format&fit=crop&w=500&q=60");
        mNames.add("Wallpapers");

        mImageUrls.add("https://images.unsplash.com/photo-1557167045-84a590d5ca22?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60");
        mNames.add("Nature");

        mImageUrls.add("https://images.unsplash.com/photo-1556219252-367b717c3867?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60");
        mNames.add("Architecture");

        mImageUrls.add("https://images.unsplash.com/photo-1556761175-b413da4baf72?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60");
        mNames.add("Work");

        mImageUrls.add("https://images.unsplash.com/photo-1554735788-e5b9efc1f490?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60");
        mNames.add("People");

        mImageUrls.add("https://images.unsplash.com/photo-1555939765-9b78f509d500?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60");
        mNames.add("Animals");

        mImageUrls.add("https://images.unsplash.com/photo-1555540798-37191cf27b8c?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60");
        mNames.add("Places");

        mImageUrls.add("https://images.unsplash.com/photo-1553026896-9bdf9fb59609?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60");
        mNames.add("Fashion");

        mImageUrls.add("https://images.unsplash.com/photo-1556694679-bfd2743bfc4c?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60");
        mNames.add("Food");

        initRecyclerView();
    }

    public void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recycler view");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
    }

    private boolean loadFragment(Fragment fragment){
        if (fragment != null){

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();

        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;

            case R.id.navigation_search:
                fragment = new SearchFragment();
                break;

            case R.id.navigation_likes:
                fragment = new LikesFragment();
                break;

        }

        return loadFragment(fragment);
    }
}
