package com.kilel.picflix.ui;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.kilel.picflix.Constants;
import com.kilel.picflix.R;
import com.kilel.picflix.adapters.FirebaseImageListAdapter;
import com.kilel.picflix.adapters.FirebaseImageViewHolder;
import com.kilel.picflix.model.UnsplashAPIResponse;
import com.kilel.picflix.util.OnStartDragListener;
import com.kilel.picflix.util.SimpleItemTouchHelperCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedImageListActivity extends AppCompatActivity implements OnStartDragListener{

    private DatabaseReference mImageReference;
    private FirebaseImageListAdapter mFirebaseAdapter;
    private ItemTouchHelper mItemTouchHelper;

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setUpFirebaseAdapter();
    }

    private void setUpFirebaseAdapter(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        Query query = FirebaseDatabase.getInstance()
                .getReference(FragmentImageDetail.FIREBASE_CHILD_PHOTO)
                .child(uid)
                .orderByChild(Constants.FIREBASE_QUERY_INDEX);

        FirebaseRecyclerOptions<UnsplashAPIResponse> options =
                new FirebaseRecyclerOptions.Builder<UnsplashAPIResponse>()
                        .setQuery(query, UnsplashAPIResponse.class)
                        .build();

        mFirebaseAdapter = new FirebaseImageListAdapter(options, query, this, this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mFirebaseAdapter);
        mRecyclerView.setHasFixedSize(true);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mFirebaseAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirebaseAdapter.stopListening();
    }

    public void onStartDrag(RecyclerView.ViewHolder viewHolder){
        mItemTouchHelper.startDrag(viewHolder);
    }
}
