package com.wx.movie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;
import com.wx.movie.adapters.CastAdapter;
import com.wx.movie.adapters.RecommendAdapter;
import com.wx.movie.adapters.ReviewAdapter;
import com.wx.movie.adapters.ScrollAdapter;
import com.wx.movie.adapters.SearchAdapter;
import com.wx.movie.models.CastData;
import com.wx.movie.models.ReviewData;
import com.wx.movie.models.SearchData;
import com.wx.movie.models.SliderData;
import com.wx.movie.tools.SharedHelper;
import com.wx.movie.tools.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    private RequestQueue mRequestQueue;

    private YouTubePlayerView youTubePlayerView;
    private TextView tv_title;
    private ReadMoreTextView tv_overview;
    private TextView tv_genre;
    private TextView tv_year;
    private ImageView imageView_bg;
    private ImageButton btn_add;
    private ImageButton btn_share_fb;
    private ImageButton btn_add_twitter;
    private TextView review_tag;
    private TextView cast_tag;



    private String type;
    private String id;

    private String title;
    private String title2;
    private String overview;
    private String genres;
    private String year;
    private String video_key;
    private String backdrop_path; // used to replace the video part if the video is NULL
    private String video_link;

    String poster_path;

    GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView_cast;
    private ArrayList<CastData> cast_list;
    private CastAdapter adapter_cast;


    LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView_review;
    private ArrayList<ReviewData> review_list;
    private ReviewAdapter adapter_review;


    LinearLayoutManager linearLayoutManager2;
    private RecyclerView recyclerView_recommend;
    private ArrayList<SliderData> recommend_list;
    private RecommendAdapter adapter_recommend;

    private ProgressBar progressBar;
    private LinearLayout prog_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        type =  intent.getStringExtra("type");
        id = intent.getStringExtra("id");
        poster_path = intent.getStringExtra("poster_path");
        title2 = intent.getStringExtra("title");

        Log.i("info_detail",type + " " + id);


        initProgressBar();
        initView();
        FetchDetails();

        UpdateUI();

        FetchVideo();
        FetchCast();
        FetchReview();
        FetchRecommend();

    }

    private void initProgressBar() {
        progressBar = findViewById(R.id.progress_bar_detail);
        prog_container = findViewById(R.id.prog_layout_detail);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //progressBar.setVisibility(View.GONE);
                prog_container.setVisibility(View.GONE);
            }

        }, 2500);
    }

    private void UpdateUI() {
        String key = type + "_" + id + "_" + title2;
        Log.i("update_ui",key);
        if (!SharedHelper.contains(key, getApplicationContext())) {
            btn_add.setBackground(getResources().getDrawable(R.drawable.ic_baseline_add_circle_outline_24));
        } else {
            btn_add.setBackground(getResources().getDrawable(R.drawable.ic_baseline_remove_circle_outline_24));
        }
    }

    private void FetchRecommend() {
        String url = "";
        if (type.equals("movie"))
            url = Constant.host + "/recommendmovies?movie_id=" + id;
        else if (type.equals("tv"))
            url = Constant.host + "/recommendtv?tv_id=" + id;

        JsonArrayRequest jsonArrayReq_recommend = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("recommend_data", response.toString());
                        try {

                            for (int i = 0; i < Math.min(10, response.length()); i++) {
                                JSONObject obj = (JSONObject) response.get(i);
                                String id = obj.getString("id");
                                String poster_path = obj.getString("poster_path");

                                String title = "";
                                if (type.equals("movie")) {
                                    title = obj.getString("title");
                                }
                                if (type.equals("tv")) {
                                    title = obj.getString("name");
                                }
                                //!!!type
                                recommend_list.add(new SliderData(id, poster_path, type, title));
                            }
                            adapter_recommend = new RecommendAdapter(getApplicationContext(), recommend_list);
                            recyclerView_recommend.setAdapter(adapter_recommend);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("recommend_data", "Error: " + error.getMessage());


            }
        });

        mRequestQueue.add(jsonArrayReq_recommend);

    }

    private void FetchReview() {
        String url = "";
        if (type.equals("movie"))
            url = Constant.host + "/moviereviews?movie_id=" + id;
        else if (type.equals("tv"))
            url = Constant.host + "/tvreviews?tv_id=" + id;

        JsonArrayRequest jsonArrayReq_review = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("review_data", response.toString());
                        try {
                            if (response == null || response.length() == 0) {
                                review_tag.setVisibility(View.GONE);
                            }

                            for (int i = 0; i < Math.min(3, response.length()); i++) {
                                JSONObject obj = (JSONObject) response.get(i);
                                String created_at = obj.getString("created_at");
                                String author = obj.getString("author");
                                String content = obj.getString("content");
                                Integer rating = obj.getInt("rating");
                                if (rating > 5)
                                    rating = rating / 2;

                                //String date = "" + ", " + Tools.getMonth(created_at.substring(5, 7)) + " " + Tools.getDay(created_at.substring(8, 10)) + created_at.substring(0, 4);
                                //created_at = created_at.substring(0, 10);
                                created_at = created_at.replaceAll("T", " ");
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

                                Date date = formatter.parse(created_at);

                                //System.out.println("weixin_date: " + formatter.format(date));


                                DateFormat df5 = new SimpleDateFormat("E, MMM dd yyyy");

                                String str_date = df5.format(date);

                                String title = "by " + author + " on " + str_date;

                                String rating_str = rating + "/5";

                                review_list.add(new ReviewData(title, rating_str, content));
                            }
                            adapter_review = new ReviewAdapter(getApplicationContext(), review_list);
                            recyclerView_review.setAdapter(adapter_review);


                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("review_data", "Error: " + error.getMessage());


            }
        });

        mRequestQueue.add(jsonArrayReq_review);



    }

    private void FetchCast() {
        String url = "";
        if (type.equals("movie"))
            url = Constant.host + "/moviecast?movie_id=" + id;
        else if (type.equals("tv"))
            url = Constant.host + "/tvcast?tv_id=" + id;

        JsonArrayRequest jsonArrayReq_cast = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("cast_data", response.toString());
                        try {

                            if (response == null || response.length() == 0) {
                                cast_tag.setVisibility(View.GONE);
                            }

                            for (int i = 0; i < Math.min(6, response.length()); i++) {
                                JSONObject obj = (JSONObject) response.get(i);
                                String profile_path = obj.getString("profile_path");
                                String name = obj.getString("name");
                                cast_list.add(new CastData(name, profile_path));
                            }
                            adapter_cast = new CastAdapter(getApplicationContext(), cast_list);
                            recyclerView_cast.setAdapter(adapter_cast);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("cast_data", "Error: " + error.getMessage());


            }
        });

        mRequestQueue.add(jsonArrayReq_cast);

    }

    private void FetchDetails() {
        String url = "";
        if (type.equals("movie"))
            url = Constant.host + "/moviedetails?movie_id=" + id;
        else if (type.equals("tv"))
            url = Constant.host + "/tvdetails?tv_id=" + id;

        Log.i("url", url);
        JsonObjectRequest jsonObjReq_detail = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("JsonObject-成功-Detail", response.toString());
                try {
                    String res_title = response.getString("title");
                    String res_overview = response.getString("overview");
                    String res_year = "";
                    if (type.equals("movie"))
                        res_year = response.getString("release_date").substring(0, 4);
                    if (type.equals("tv"))
                        res_year = response.getString("first_air_date").substring(0, 4);
                    String img_path = response.getString("backdrop_path");

                    String str_genre = "";
                    JSONArray json_genre_arr = response.getJSONArray("genres");
                    for (int i = 0; i < json_genre_arr.length(); i++) {
                        JSONObject item = (JSONObject) json_genre_arr.get(i);
                        str_genre = str_genre + item.getString("name") + ", ";
                    }

                    title = res_title;
                    overview = res_overview;
                    year = res_year;
                    backdrop_path = img_path;
                    if (str_genre.length() > 2 )
                        genres = str_genre.substring(0, str_genre.length() - 2);

                    tv_year.setText(year);
                    tv_title.setText(title);


                    //!!!
                    tv_overview.setText(overview);


                    tv_genre.setText(genres);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Detail", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(jsonObjReq_detail);

    }


    private void FetchVideo() {
        String url = "";
        if (type.equals("movie"))
            url = Constant.host + "/movievideo?movie_id=" + id;
        else if (type.equals("tv"))
            url = Constant.host + "/tvvideo?tv_id=" + id;

        JsonArrayRequest jsonArrayReq_video = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("video_req", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = (JSONObject) response.get(i);
                                if (!obj.getString("type").equals("Trailer")) {
                                    continue;
                                }
                                video_link = obj.getString("link");
                                video_key = obj.getString("key");
                                break;

                            }

                            if (response == null || response.length() == 0 || video_key == null) {
                                imageView_bg.setVisibility(View.VISIBLE);
                                Picasso.get().load(backdrop_path).into(imageView_bg);
                                youTubePlayerView.setVisibility(View.GONE);

                            } else {
                                youTubePlayerView.setVisibility(View.VISIBLE);
                                imageView_bg.setVisibility(View.GONE);
                                youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                                    @Override
                                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                                        String videoId = video_key;
                                        youTubePlayer.cueVideo(videoId, 0);
                                    }
                                });

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("video_req", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        mRequestQueue.add(jsonArrayReq_video);

    }


    private void initView() {
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);


        review_tag = findViewById(R.id.tv_review_tag);
        cast_tag = findViewById(R.id.tv_cast_tag);
        tv_title = findViewById(R.id.tv_detail_title);
        tv_overview = findViewById(R.id.tv_detail_overview);
        tv_genre = findViewById(R.id.tv_detail_genre);
        tv_year = findViewById(R.id.tv_detail_year);
        imageView_bg = findViewById(R.id.iv_backdrop);
        btn_add = findViewById(R.id.btn_detail_add);
        btn_share_fb = findViewById(R.id.btn_detail_fb);
        btn_add_twitter = findViewById(R.id.btn_detail_twitter);
        recyclerView_cast = findViewById(R.id.recycle_cast);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
        recyclerView_cast.setLayoutManager(gridLayoutManager);
        cast_list = new ArrayList<CastData>();

        recyclerView_review = findViewById(R.id.recycle_detail_review);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView_review.setLayoutManager(linearLayoutManager);
        review_list = new ArrayList<ReviewData>();


        linearLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_recommend = findViewById(R.id.recycle_recommend);
        recyclerView_recommend.setLayoutManager(linearLayoutManager2);
        recommend_list = new ArrayList<SliderData>();




        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = type + "_" + id + "_" + title;
                String value = poster_path;

                if (!SharedHelper.contains(key, getApplicationContext())) {
                    SharedHelper.putString(key, value, getApplicationContext());
                    Toast.makeText(getApplicationContext(), title + "was added to Watchlist", Toast.LENGTH_LONG).show();
                    btn_add.setBackground(getResources().getDrawable(R.drawable.ic_baseline_remove_circle_outline_24));

                } else {
                    SharedHelper.remove(key, getApplicationContext());
                    Toast.makeText(getApplicationContext(), title + "was removed from Watchlist", Toast.LENGTH_SHORT).show();
                    btn_add.setBackground(getResources().getDrawable(R.drawable.ic_baseline_add_circle_outline_24));

                }

            }
        });

        btn_share_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fb_link = "https://www.facebook.com/sharer/sharer.php?u=https://www.youtube.com/watch?v=" + video_key;
                Uri uri = Uri.parse(fb_link);
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
            }
        });

        btn_add_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imdblink = "https://www.themoviedb.org/" + type + "/" + id;
                String twitter_link = "https://twitter.com/intent/tweet?text=Check this out! \n" + imdblink;
                Uri uri = Uri.parse(twitter_link);
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}