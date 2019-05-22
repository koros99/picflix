package com.kilel.picflix;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kilel.picflix.model.UnsplashAPIResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ImageViewHolder> {

    private List<UnsplashAPIResponse> mPictures = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(List<UnsplashAPIResponse> pictures, Context context) {
        this.mPictures = pictures;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.bindPicture(mPictures.get(position));
    }

    @Override
    public int getItemCount() {
        return mPictures.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_id) ImageView mImageView;
        @BindView(R.id.profile_image) ImageView mProfileImage;
        @BindView(R.id.img_description_id) TextView mDescription;
        private Context context;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }
        public void bindPicture(UnsplashAPIResponse picture){
            Picasso.get().load(picture.getUrls().getSmall()).into(mImageView);
            Picasso.get().load(picture.getUser().getProfileImage().getLarge()).into(mProfileImage);
            mDescription.setText("Photo by " + picture.getUser().getFirstName() + " " + picture.getUser().getLastName());
        }
    }
}