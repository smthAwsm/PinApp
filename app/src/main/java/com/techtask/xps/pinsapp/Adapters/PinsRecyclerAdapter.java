package com.techtask.xps.pinsapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.techtask.xps.pinsapp.Helper.ItemTouchHelperAdapter;
import com.techtask.xps.pinsapp.Helper.ItemTouchHelperViewHolder;
import com.techtask.xps.pinsapp.Helper.OnStartDragListener;
import com.techtask.xps.pinsapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by XPS on 6/11/2016.
 */
public class PinsRecyclerAdapter extends RecyclerView.Adapter<PinsRecyclerAdapter.ItemViewHolder> implements ItemTouchHelperAdapter
{

        private final List<String> mItems = new ArrayList<>();

        private final OnStartDragListener mDragStartListener;

        public PinsRecyclerAdapter(Context context, OnStartDragListener dragStartListener) {
            mDragStartListener = dragStartListener;
            mItems.addAll(Arrays.asList(context.getResources().getStringArray(R.array.dummy_items)));
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pin_item_layout, parent, false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            return itemViewHolder;
        }

        @Override
        public void onBindViewHolder(final ItemViewHolder holder, int position) {
            holder.textView.setText(mItems.get(position));

            // Start a drag whenever the handle view it touched
            holder.handleView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        mDragStartListener.onStartDrag(holder);
                    }
                    return false;
                }
            });
        }

        @Override
        public void onItemDismiss(int position) {
            mItems.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            Collections.swap(mItems, fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        /**
         * Simple example of a view holder that implements {@link ItemTouchHelperViewHolder} and has a
         * "handle" view that initiates a drag event when touched.
         */
        public static class ItemViewHolder extends RecyclerView.ViewHolder implements
                ItemTouchHelperViewHolder {

            public final TextView textView;
            public final ImageView handleView;

            public ItemViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.itemText);
                handleView = (ImageView) itemView.findViewById(R.id.handle);
            }

            @Override
            public void onItemSelected() {
                itemView.setBackgroundColor(Color.LTGRAY);
            }

            @Override
            public void onItemClear() {
                itemView.setBackgroundColor(0);
            }
        }


}
