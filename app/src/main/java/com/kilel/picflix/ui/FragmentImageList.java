package com.kilel.picflix.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kilel.picflix.Constants;
import com.kilel.picflix.R;
import com.kilel.picflix.adapters.ImageListAdapter;
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

import static com.kilel.picflix.ui.MainActivity.API_KEY;
import static com.kilel.picflix.ui.MainActivity.PER_PAGE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentImageList extends Fragment {

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    private ImageListAdapter mAdapter;
    public ArrayList<UnsplashAPIResponse> mPhotos = new ArrayList<>();


    public FragmentImageList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_fragment_image_list, container, false);
        ButterKnife.bind(this, view);

        loadJson("");
        mAdapter = new ImageListAdapter(mPhotos, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setLayoutManager(layoutManager);

        return view;
    }

    public void loadJson(final String keyword){
        UnsplashInterface apiInterface = ApiClient.getApiClient().create(UnsplashInterface.class);
        Call<List<UnsplashAPIResponse>> call;

        if (keyword.length() != 0) {
            call = apiInterface.getSearchPhotos(Constants.API_KEY, Constants.PER_PAGE, keyword);
        }
        else {
            call = apiInterface.getPhotos(Constants.API_KEY, Constants.PER_PAGE);
        }

        call.enqueue(new Callback<List<UnsplashAPIResponse>>() {
            @Override
            public void onResponse(Call<List<UnsplashAPIResponse>> call, Response<List<UnsplashAPIResponse>> response) {
                if (response.isSuccessful() && response.body() != null){
                    mAdapter.swapData(response.body());
                } else {
                    Toast.makeText(getActivity(), "No Result!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UnsplashAPIResponse>> call, Throwable t) {
            }
        });

    }

}
