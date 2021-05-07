package com.wx.movie.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.wx.movie.Constant;
import com.wx.movie.R;
import com.wx.movie.adapters.ScrollAdapter;
import com.wx.movie.adapters.SearchAdapter;
import com.wx.movie.models.SearchData;
import com.wx.movie.models.SliderData;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchFragment extends Fragment {


    private RequestQueue mRequestQueue;


    LinearLayoutManager manager;

    private RecyclerView recyclerView;
    private SearchView searchView;
    private ArrayList<SearchData> data_list;
    private SearchAdapter adapter_search;


    private TextView tv_nofound;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_search, container, false);
        initData(view);
        initSearchView(view);


        return view;
    }




    private void initData(View view) {
        tv_nofound = view.findViewById(R.id.tv_nofound);
        tv_nofound.setVisibility(View.GONE);
        recyclerView = view.findViewById(R.id.recycle_search);
        manager = new LinearLayoutManager(getContext());
       // manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        data_list = new ArrayList<SearchData>();
        mRequestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

    }

    private void initSearchView(View view) {
       searchView = view.findViewById(R.id.search_view);
        //search input frame
        androidx.appcompat.widget.SearchView.SearchAutoComplete tv = (androidx.appcompat.widget.SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        tv.setTextColor(Color.WHITE);
        tv.setHintTextColor(Color.GRAY);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //my code here for search

                FetchSearchData(s);
                //return false;
                if (data_list.size() == 0) {
                    tv_nofound.setVisibility(View.VISIBLE);
                    recyclerView.getRecycledViewPool().clear();
                } else {
                    tv_nofound.setVisibility(View.GONE);
                }

                return true;
            }
        });
    }


    private void FetchSearchData(String s) {
        String url = Constant.host + "/search?query_input=" + s;
        JsonArrayRequest jsonArrayReq_search = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("search_data", response.toString());
                        try {

                            data_list.clear();
                            for (int i = 0; i < Math.min(20, response.length()); i++) {
                                JSONObject obj = (JSONObject) response.get(i);
                                String backdrop_path = obj.getString("backdrop_path");
                                String id = String.valueOf(obj.getInt("id"));
                                String title = obj.getString("title");
                                String year = obj.getString("year");
                                String type = obj.getString("media_type");
                                double rating = obj.getDouble("rating");

                                data_list.add(new SearchData(type, id, title, rating, backdrop_path, year));

                            }
                            adapter_search = new SearchAdapter(getContext(), data_list);
                            adapter_search.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter_search);



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("search_data", "Error: " + error.getMessage());


            }
        });

        mRequestQueue.add(jsonArrayReq_search);
    }
}
