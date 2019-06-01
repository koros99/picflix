package com.kilel.picflix.ui;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kilel.picflix.R;
import com.kilel.picflix.adapters.ImagePagerAdapter;
import com.kilel.picflix.model.UnsplashAPIResponse;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageDetailActivity extends AppCompatActivity {

    @BindView(R.id.viewPager) ViewPager mViewPager;
    private ImagePagerAdapter pagerAdapter;
    List<UnsplashAPIResponse> mPictures = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        ButterKnife.bind(this);

        mPictures = Parcels.unwrap(getIntent().getParcelableExtra("pictures"));
        int startingPosition = getIntent().getIntExtra("position", 0);

        pagerAdapter = new ImagePagerAdapter(getSupportFragmentManager(), mPictures);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(startingPosition);

    }
}
