package com.kilel.picflix;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kilel.picflix.model.UnsplashAPIResponse;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentImageDetail extends Fragment {

    @BindView(R.id.fullImageView) ImageView mFullImageView;
    @BindView(R.id.userName) TextView mUserName;
    @BindView(R.id.linkToUnsplash) TextView mLinkToUnsplash;
    @BindView(R.id.saveImageButton) Button mSaveImageButton;

    private UnsplashAPIResponse mPicture;


    public FragmentImageDetail() {
        // Required empty public constructor
    }

    public static FragmentImageDetail newInstance(UnsplashAPIResponse image){
        FragmentImageDetail fragmentImageDetail = new FragmentImageDetail();
        Bundle args = new Bundle();
        args.putParcelable("picture", Parcels.wrap(image));
        fragmentImageDetail.setArguments(args);
        return fragmentImageDetail;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPicture = Parcels.unwrap(getArguments().getParcelable("picture"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_detail, container, false);
        ButterKnife.bind(this, view);

        Picasso.get().load(mPicture.getUrls().getFull()).into(mFullImageView);

        return view;
    }

}
