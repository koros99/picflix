package com.kilel.picflix.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kilel.picflix.R;
import com.kilel.picflix.model.UnsplashAPIResponse;
import com.kilel.picflix.ui.FragmentImageDetail;
import com.kilel.picflix.ui.ImageDetailActivity;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

public class FirebaseImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mView;
    Context mContext;

    public FirebaseImageViewHolder(View itemView){
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindPicture(UnsplashAPIResponse picture){
        ImageView mImageView = mView.findViewById(R.id.img_id);
        ImageView mProfileImage = mView.findViewById(R.id.profile_image);
        TextView mDescription = mView.findViewById(R.id.img_description_id);

        Picasso.get().load(picture.getUrls().getSmall()).into(mImageView);
        Picasso.get().load(picture.getUser().getProfileImage().getLarge()).into(mProfileImage);
        mDescription.setText("Photo by " + picture.getUser().getUserFullName());
    }

    @Override
    public void onClick(View v) {
        final ArrayList<UnsplashAPIResponse> photos = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uId = user.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(FragmentImageDetail.FIREBASE_CHILD_PHOTO).child(uId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    photos.add(snapshot.getValue(UnsplashAPIResponse.class));
                }

                int itemPosition = getLayoutPosition();

                Intent intent = new Intent(mContext, ImageDetailActivity.class);
                intent.putExtra("position", itemPosition + "");
                intent.putExtra("pictures", Parcels.wrap(photos));

                mContext.startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
