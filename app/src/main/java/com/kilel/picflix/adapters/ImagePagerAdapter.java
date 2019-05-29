package com.kilel.picflix;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kilel.picflix.model.UnsplashAPIResponse;

import java.util.ArrayList;
import java.util.List;

public class ImagePagerAdapter extends FragmentPagerAdapter {

    private List<UnsplashAPIResponse> mPictures;

    public ImagePagerAdapter(FragmentManager fm, List<UnsplashAPIResponse> mPictures) {
        super(fm);
        this.mPictures = mPictures;
    }

    @Override
    public Fragment getItem(int i) {
        return FragmentImageDetail.newInstance(mPictures.get(i));
    }

    @Override
    public int getCount() {
        return mPictures.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mPictures.get(position).getUser().getFirstName();
    }
}
