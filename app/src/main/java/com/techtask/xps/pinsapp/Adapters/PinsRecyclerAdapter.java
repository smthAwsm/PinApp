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

import com.techtask.xps.pinsapp.Activities.MainActivity;
import com.techtask.xps.pinsapp.Helper.ItemTouchHelperAdapter;
import com.techtask.xps.pinsapp.Helper.ItemTouchHelperViewHolder;
import com.techtask.xps.pinsapp.Helper.MapHelper;
import com.techtask.xps.pinsapp.Helper.OnStartDragListener;
import com.techtask.xps.pinsapp.Models.MarkerModel;
import com.techtask.xps.pinsapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by XPS on 6/11/2016.
 */
public class PinsRecyclerAdapter extends RecyclerView.Adapter<PinsRecyclerAdapter.ItemViewHolder> implements ItemTouchHelperAdapter
{
    private final OnStartDragListener dragStartListener;
    private List<MarkerModel> markersData = new ArrayList<>();

        public PinsRecyclerAdapter(Context context, List<MarkerModel> markersData,OnStartDragListener dragStartListener) {
            this.dragStartListener = dragStartListener;
            this.markersData = markersData;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pin_item_layout, parent, false);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            return itemViewHolder;
        }

        @Override
        public void onBindViewHolder(final ItemViewHolder holder, final int position) {

            holder.addressView.setText(markersData.get(position).getTitle());
            holder.dateView.setText(markersData.get(position).getDate());

            holder.handleView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        dragStartListener.onStartDrag(holder);
                    }
                    return false;
                }
            });

            holder.viewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MapHelper.showMarker(markersData.get(position));
                }
            });
        }

        @Override
        public void onItemDismiss(int position) {
            MapHelper.deleteMarker(markersData.get(position));
            //markersData.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            Collections.swap(markersData, fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public int getItemCount() {
            return markersData.size();
        }

        public static class ItemViewHolder extends RecyclerView.ViewHolder implements
                ItemTouchHelperViewHolder {

            public final View viewItem;
            public final TextView addressView;
            public final TextView dateView;
            public final ImageView handleView;

            public ItemViewHolder(View itemView) {
                super(itemView);

                viewItem = itemView;
                addressView = (TextView) itemView.findViewById(R.id.addressTextView);
                dateView = (TextView) itemView.findViewById(R.id.dateTextView);
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
