package com.kilel.picflix.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.kilel.picflix.R;
import com.kilel.picflix.model.UnsplashAPIResponse;
import com.kilel.picflix.ui.ImageDetailActivity;
import com.kilel.picflix.util.ItemTouchHelperAdapter;
import com.kilel.picflix.util.OnStartDragListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;

public class FirebaseImageListAdapter extends FirebaseRecyclerAdapter<UnsplashAPIResponse, FirebaseImageViewHolder> implements ItemTouchHelperAdapter {

    private Query mRef;
    private OnStartDragListener mOnStartDragListener;
    private Context mContext;
    private ChildEventListener mChildEventListener;
    private ArrayList<UnsplashAPIResponse> photos = new ArrayList<>();

    public FirebaseImageListAdapter(@NonNull FirebaseRecyclerOptions<UnsplashAPIResponse> options, Query ref, OnStartDragListener onStartDragListener, Context context) {
        super(options);
        this.mRef = ref;
        this.mOnStartDragListener = onStartDragListener;
        this.mContext = context;
        mChildEventListener = mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                photos.add(dataSnapshot.getValue(UnsplashAPIResponse.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onBindViewHolder(@NonNull final FirebaseImageViewHolder holder, int position, @NonNull UnsplashAPIResponse model) {
        holder.bindPicture(model);
        holder.mImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    mOnStartDragListener.onStartDrag(holder);
                }
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ImageDetailActivity.class);
                intent.putExtra("position", holder.getAdapterPosition() + "");
                intent.putExtra("pictures", Parcels.wrap(photos));

                mContext.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public FirebaseImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_list_item_drag, viewGroup, false);
        return new FirebaseImageViewHolder(view);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(photos, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        setIndexInFirebase();
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        photos.remove(position);
        getRef(position).removeValue();
        Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
    }

    private void setIndexInFirebase(){
        for (UnsplashAPIResponse photo: photos){
            int index = photos.indexOf(photo);
            DatabaseReference ref = getRef(index);
            photo.setIndex(Integer.toString(index));
            ref.setValue(photo);
        }
    }

    @Override
    public void stopListening() {
        super.stopListening();
        mRef.removeEventListener(mChildEventListener);
    }
}
