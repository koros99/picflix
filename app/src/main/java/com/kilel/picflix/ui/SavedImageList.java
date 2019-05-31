package com.kilel.picflix.ui;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kilel.picflix.R;
import com.kilel.picflix.adapters.FirebaseImageViewHolder;
import com.kilel.picflix.model.UnsplashAPIResponse;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedImageList extends AppCompatActivity {

    private DatabaseReference mImageReference;
    private FirebaseRecyclerAdapter<UnsplashAPIResponse, FirebaseImageViewHolder> mFirebaseAdapter;

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        mImageReference = FirebaseDatabase.getInstance().getReference(FragmentImageDetail.FIREBASE_CHILD_PHOTO).child(uid);

        ButterKnife.bind(this);
        setUpFirebaseAdapter();
    }

    private void setUpFirebaseAdapter(){

        FirebaseRecyclerOptions<UnsplashAPIResponse> options =
                new FirebaseRecyclerOptions.Builder<UnsplashAPIResponse>()
                        .setQuery(mImageReference, UnsplashAPIResponse.class)
                        .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<UnsplashAPIResponse, FirebaseImageViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FirebaseImageViewHolder holder, int position, @NonNull UnsplashAPIResponse photo) {
                holder.bindPicture(photo);
            }

            @NonNull
            @Override
            public FirebaseImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_list, viewGroup, false);
                return new FirebaseImageViewHolder(view);
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mFirebaseAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mFirebaseAdapter != null){
            mFirebaseAdapter.stopListening();
        }
    }

}
