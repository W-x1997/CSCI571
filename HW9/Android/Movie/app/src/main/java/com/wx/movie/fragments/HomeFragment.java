package com.wx.movie.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.ClientError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.smarteist.autoimageslider.SliderView;
import com.wx.movie.Constant;
import com.wx.movie.R;
import com.wx.movie.adapters.ScrollAdapter;
import com.wx.movie.adapters.SliderAdapter;
import com.wx.movie.models.SliderData;
import com.wx.movie.tools.NoUnderlineSpan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ScrollView movie_part;
    private ScrollView tv_part;


    private RequestQueue mRequestQueue;


    // movie part
    private SliderAdapter adapter_movie; // now_play movies
    private ArrayList<SliderData> sliderData_movie; // now_play movies
    private SliderView sliderView_movie; // now_play movies
    private boolean isload = false;

    private LinearLayoutManager manager;
    private LinearLayoutManager manager2;

    private RecyclerView recyclerView_movie_top;
    private ArrayList<SliderData> data_movie_top; // top movies
    private  ScrollAdapter adapter_movie_top;

    private RecyclerView recyclerView_movie_popular;
    private ArrayList<SliderData> data_movie_popular; // top movies
    private ScrollAdapter adapter_movie_popular;


    // tv part
    private SliderAdapter adapter_tv; // now_play tv
    private ArrayList<SliderData> sliderData_tv; // now_play tv
    private SliderView sliderView_tv;

    private LinearLayoutManager manager3;
    private LinearLayoutManager manager4;

    private RecyclerView recyclerView_tv_top;
    private ArrayList<SliderData> data_tv_top; // top movies
    public ScrollAdapter adapter_tv_top;

    private RecyclerView recyclerView_tv_popular;
    private ArrayList<SliderData> data_tv_popular; // top movies
    private ScrollAdapter adapter_tv_popular;

    private ProgressBar progressBar;
    private LinearLayout prog_lcontainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        progressBar = view.findViewById(R.id.progress_bar);
        prog_lcontainer = view.findViewById(R.id.prog_layout);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                //progressBar.setVisibility(View.GONE);
                prog_lcontainer.setVisibility(View.GONE);
            }

        }, 2500);

        //first we see the movie part
        movie_part = view.findViewById(R.id.movie_part);
        tv_part = view.findViewById(R.id.tv_part);
        tv_part.setVisibility(View.GONE);


        //add footer link
        TextView footer = (TextView) view.findViewById(R.id.footer1);
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.themoviedb.org/");
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
            }
        });

        TextView footer2 = (TextView) view.findViewById(R.id.footer3);
        footer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.themoviedb.org/");
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
            }
        });




        //movie slide part
        sliderView_movie = view.findViewById(R.id.slider);
        mRequestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        sliderData_movie = new ArrayList<>();
        Fetch_Nowplay_Movies();

        //movie top part
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_movie_top = view.findViewById(R.id.recycle_movie_top);
        recyclerView_movie_top.setLayoutManager(manager);
        data_movie_top = new ArrayList<>();
        Fetch_Top_Movies();


        //movie popular part
        manager2 = new LinearLayoutManager(getContext());
        manager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_movie_popular = view.findViewById(R.id.recycle_movie_popular);
        recyclerView_movie_popular.setLayoutManager(manager2);
        data_movie_popular = new ArrayList<>();
        Fetch_Popular_Movies();

        //
        //tv slide part
        sliderView_tv = view.findViewById(R.id.slider_tv);
        sliderData_tv = new ArrayList<>();
        Fetch_Nowplay_Tv();

        //tv top part
        manager3 = new LinearLayoutManager(getContext());
        manager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_tv_top = view.findViewById(R.id.recycle_tv_top);
        recyclerView_tv_top.setLayoutManager(manager3);
        data_tv_top = new ArrayList<>();
        Fetch_Top_Tv();


        //tv popular part
        manager4 = new LinearLayoutManager(getContext());
        manager4.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_tv_popular = view.findViewById(R.id.recycle_tv_popular);
        recyclerView_tv_popular.setLayoutManager(manager4);
        data_tv_popular = new ArrayList<>();
        Fetch_Popular_Tv();



//        if (!isload) {
//            Fetch_Nowplay_Movies();
//            isload = true;
//        }




        //return inflater.inflate(R.layout.fragment_home, container, false);


        // two tabs used to swtich the data
        TextView movie_tab = (TextView) view.findViewById(R.id.movie_tab);
        TextView tv_tab = (TextView) view.findViewById(R.id.tv_tab);

        //default is movie
        movie_tab.setTextColor(view.getResources().getColor(R.color.white));
        tv_tab.setTextColor(view.getResources().getColor(R.color.colorPrimary));

        movie_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movie_tab.setTextColor(view.getResources().getColor(R.color.white));
                tv_tab.setTextColor(view.getResources().getColor(R.color.colorPrimary));
                
                movie_part.setVisibility(View.VISIBLE);
                tv_part.setVisibility(View.GONE);
            }
        });

        tv_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movie_tab.setTextColor(view.getResources().getColor(R.color.colorPrimary));
                tv_tab.setTextColor(view.getResources().getColor(R.color.white));

                movie_part.setVisibility(View.GONE);
                tv_part.setVisibility(View.VISIBLE);
            }
        });


        return view;
    }

    private void Fetch_Popular_Tv() {
        String url = Constant.host + "/populartv";
        JsonArrayRequest jsonArrayReq_tv_popular = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("tv_popular", response.toString());
                        try {

                            for (int i = 0; i < Math.min(10, response.length()); i++) {
                                JSONObject tv = (JSONObject) response.get(i);
                                String poster_path = tv.getString("poster_path");
                                String id = String.valueOf(tv.getInt("id"));
                                String title = tv.getString("name");
                                data_tv_popular.add(new SliderData(id, poster_path, "tv", title));

                            }
                            adapter_tv_popular = new ScrollAdapter(getContext(), data_tv_popular);
                            recyclerView_tv_popular.setAdapter(adapter_tv_popular);


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
                VolleyLog.d("tv_popular", "Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        mRequestQueue.add(jsonArrayReq_tv_popular);
    }

    private void Fetch_Top_Tv() {
        String url = Constant.host + "/topratedtv";
        JsonArrayRequest jsonArrayReq_tv_top = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("tv_top", response.toString());
                        try {

                            for (int i = 0; i < Math.min(10, response.length()); i++) {
                                JSONObject tv = (JSONObject) response.get(i);
                                String poster_path = tv.getString("poster_path");
                                String id = String.valueOf(tv.getInt("id"));
                                String title = tv.getString("name");
                                data_tv_top.add(new SliderData(id, poster_path, "tv", title));

                            }
                            adapter_tv_top = new ScrollAdapter(getContext(), data_tv_top);
                            recyclerView_tv_top.setAdapter(adapter_tv_top);


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
                VolleyLog.d("tv_top", "Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        mRequestQueue.add(jsonArrayReq_tv_top);
    }

    private void Fetch_Nowplay_Tv() {
        String url = Constant.host + "/trendingtv";
        JsonArrayRequest jsonArrayReq_tv_now = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("tv_now", response.toString());
                        try {

                            for (int i = 0; i < Math.min(6, response.length()); i++) {
                                JSONObject tv = (JSONObject) response.get(i);
                                String poster_path = tv.getString("poster_path");
                                String id = String.valueOf(tv.getInt("id"));
                                String title = tv.getString("name");
                                sliderData_tv.add(new SliderData(id, poster_path, "tv", title));

                                Log.i("ITEM", poster_path + " " + id);
                            }

                            adapter_tv = new SliderAdapter(getContext(), sliderData_tv);
                            sliderView_tv.setSliderAdapter(adapter_tv);

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
                VolleyLog.d("tv_now", "Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        mRequestQueue.add(jsonArrayReq_tv_now);
        //SliderAdapter
        sliderView_movie.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView_movie.setScrollTimeInMillis(6000);
        sliderView_movie.setAutoCycle(true);
        sliderView_movie.startAutoCycle();
    }

    private void Fetch_Popular_Movies() {
        String url = Constant.host + "/popularmovies";
        JsonArrayRequest jsonArrayReq_movie_popular = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("movies_popular", response.toString());
                        try {

                            for (int i = 0; i < Math.min(10, response.length()); i++) {
                                JSONObject movie = (JSONObject) response.get(i);
                                String poster_path = movie.getString("poster_path");
                                String id = String.valueOf(movie.getInt("id"));
                                String title = movie.getString("title");
                                data_movie_popular.add(new SliderData(id, poster_path, "movie", title));

                            }
                            adapter_movie_popular = new ScrollAdapter(getContext(), data_movie_popular);
                            recyclerView_movie_popular.setAdapter(adapter_movie_popular);


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
                VolleyLog.d("movies_popular", "Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        mRequestQueue.add(jsonArrayReq_movie_popular);
    }

    private void Fetch_Top_Movies() {
        String url = Constant.host + "/topratedmovies";
        JsonArrayRequest jsonArrayReq_movie_top = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("movies_top", response.toString());
                        try {

                            for (int i = 0; i < Math.min(10, response.length()); i++) {
                                JSONObject movie = (JSONObject) response.get(i);
                                String poster_path = movie.getString("poster_path");
                                String id = String.valueOf(movie.getInt("id"));
                                String title = movie.getString("title");
                                data_movie_top.add(new SliderData(id, poster_path, "movie", title));

                            }
                            adapter_movie_top = new ScrollAdapter(getContext(), data_movie_top);
                            recyclerView_movie_top.setAdapter(adapter_movie_top);


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
                VolleyLog.d("movies_top", "Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        mRequestQueue.add(jsonArrayReq_movie_top);

    }

    private void Fetch_Nowplay_Movies() {
        String url = Constant.host + "/nowmovies";
        JsonArrayRequest jsonArrayReq_movie_now = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("movies_now", response.toString());
                        try {

                            for (int i = 0; i < Math.min(6, response.length()); i++) {
                                JSONObject movie = (JSONObject) response.get(i);
                                String poster_path = movie.getString("poster_path");
                                String id = String.valueOf(movie.getInt("id"));
                                String title = movie.getString("title");
                                sliderData_movie.add(new SliderData(id, poster_path, "movie", title));

                                Log.i("ITEM", poster_path + " " + id);
                            }

                            adapter_movie = new SliderAdapter(getContext(), sliderData_movie);
                            sliderView_movie.setSliderAdapter(adapter_movie);

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
//                VolleyLog.d("movies_now", "Error: " + error.getMessage());
//                Toast.makeText(getActivity().getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        mRequestQueue.add(jsonArrayReq_movie_now);
        //SliderAdapter
        sliderView_movie.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView_movie.setScrollTimeInMillis(6000);
        sliderView_movie.setAutoCycle(true);
        sliderView_movie.startAutoCycle();



    }
}
