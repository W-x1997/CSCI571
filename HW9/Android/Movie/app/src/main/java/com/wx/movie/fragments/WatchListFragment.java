package com.wx.movie.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.wx.movie.R;
import com.wx.movie.adapters.ScrollAdapter;
import com.wx.movie.adapters.WatchListAdapter;
import com.wx.movie.models.SliderData;
import com.wx.movie.tools.SharedHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class WatchListFragment extends Fragment {

    private RecyclerView recycle_watchlist;
    private ArrayList<SliderData> data_list;
    private WatchListAdapter adapter_watchlist;
    GridLayoutManager gridLayoutManager;

    private TextView textView;


    ItemTouchHelper itemTouchHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watchlist, container, false);
        recycle_watchlist = view.findViewById(R.id.recycle_watchlist);
        textView = view.findViewById(R.id.tv_watchlist_nodata);

        initData();
        initItemTouchHelper();

        if (data_list == null || data_list.size() == 0) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }

        return view;
    }

    private void initItemTouchHelper() {
        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                final int dragFlags;
                final int swipeFlags;
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                    swipeFlags = 0;
                } else {
                    dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                    swipeFlags = 0;
                }
                return makeMovementFlags(dragFlags, swipeFlags);

            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
                int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(data_list, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(data_list, i, i - 1);
                    }
                }
                adapter_watchlist.notifyItemMoved(fromPosition, toPosition);
                return true;

            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });


        itemTouchHelper.attachToRecyclerView(recycle_watchlist);

    }

    private void initData() {

        data_list = new ArrayList<>();
        Map<String, String> map = (Map<String, String>) SharedHelper.getAll(getContext());
        for (String key : map.keySet()) {
            String[] arr = key.split("_");
            String type = arr[0];
            String id = arr[1];
            String title = arr[2];
            String poster_path = map.get(key);
            data_list.add(new SliderData(id, poster_path, type, title));
            Log.i("watchlist", type + " " + id + " " +  title +" " + poster_path);

        }

        gridLayoutManager = new GridLayoutManager(getContext(),3);
        recycle_watchlist.setLayoutManager(gridLayoutManager);

        adapter_watchlist = new WatchListAdapter(getContext(), data_list);
        recycle_watchlist.setAdapter(adapter_watchlist);

    }
}
