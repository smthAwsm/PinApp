package com.techtask.xps.pinsapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techtask.xps.pinsapp.Activities.MainActivity;
import com.techtask.xps.pinsapp.Adapters.PinsRecyclerAdapter;
import com.techtask.xps.pinsapp.Helper.OnStartDragListener;
import com.techtask.xps.pinsapp.Helper.SimpleItemTouchHelperCallback;
import com.techtask.xps.pinsapp.R;

/**
 * Created by XPS on 6/11/2016.
 */
public class PinsFragment extends Fragment implements OnStartDragListener{

    private ItemTouchHelper mItemTouchHelper;
    private static PinsRecyclerAdapter recyclerAdapter;

    public PinsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_page, container, false);
        recyclerAdapter = new PinsRecyclerAdapter(getContext(), MainActivity.markers,this);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.pinsRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(recyclerAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    public static PinsRecyclerAdapter getRecyclerAdapter() {
        return recyclerAdapter;
    }
}
