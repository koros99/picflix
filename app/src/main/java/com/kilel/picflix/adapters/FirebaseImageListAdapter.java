package com.kilel.picflix.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.kilel.picflix.R;
import com.kilel.picflix.model.UnsplashAPIResponse;
import com.kilel.picflix.util.ItemTouchHelperAdapter;
import com.kilel.picflix.util.OnStartDragListener;

public class FirebaseImageListAdapter extends FirebaseRecyclerAdapter<UnsplashAPIResponse, FirebaseImageViewHolder> implements ItemTouchHelperAdapter {

    private DatabaseReference mRef;
    private OnStartDragListener mOnStartDragListener;
    private Context mContext;

    public FirebaseImageListAdapter(@NonNull FirebaseRecyclerOptions<UnsplashAPIResponse> options, DatabaseReference ref, OnStartDragListener onStartDragListener, Context context) {
        super(options);
        this.mRef = ref;
        this.mOnStartDragListener = onStartDragListener;
        this.mContext = context;
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
    }

    @NonNull
    @Override
    public FirebaseImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_list_item_drag, viewGroup, false);
        return new FirebaseImageViewHolder(view);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        notifyItemMoved(fromPosition, toPosition);
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        getRef(position).removeValue();
    }
}
