package com.kilel.picflix.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kilel.picflix.R;
import com.kilel.picflix.model.UnsplashAPIResponse;
import com.kilel.picflix.ui.FragmentImageDetail;
import com.kilel.picflix.ui.ImageDetail;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;

public class FirebaseImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mView;
    Context mContext;

    @BindView(R.id.img_id) ImageView mImageView;
    @BindView(R.id.profile_image) ImageView mProfileImage;
    @BindView(R.id.img_description_id) TextView mDescription;

    public FirebaseImageViewHolder(View itemView){
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindPicture(UnsplashAPIResponse picture){
        Picasso.get().load(picture.getUrls().getSmall()).into(mImageView);
        Picasso.get().load(picture.getUser().getProfileImage().getLarge()).into(mProfileImage);
        mDescription.setText("Photo by " + picture.getUser().getUserFullName());
    }

    @Override
    public void onClick(View v) {
        final ArrayList<UnsplashAPIResponse> photos = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(FragmentImageDetail.FIREBASE_CHILD_PHOTO);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    photos.add(snapshot.getValue(UnsplashAPIResponse.class));
                }

                int itemPosition = getLayoutPosition();

                Intent intent = new Intent(mContext, ImageDetail.class);
                intent.putExtra("position", itemPosition + "");
                intent.putExtra("photos", Parcels.wrap(photos));

                mContext.startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
