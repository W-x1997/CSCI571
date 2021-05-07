package com.wx.movie.adapters;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.wx.movie.DetailActivity;
import com.wx.movie.MainActivity;
import com.wx.movie.R;
import com.wx.movie.models.SliderData;
import com.wx.movie.tools.CircleCornerForm;
import com.wx.movie.tools.SharedHelper;

import java.util.ArrayList;
import java.util.List;

public class ScrollAdapter extends RecyclerView.Adapter<ScrollAdapter.ViewHolder>{

    private final List<SliderData> mSliderItems;
    private Context context;

    public ScrollAdapter(Context context, ArrayList<SliderData> sliderDataArrayList) {
        this.mSliderItems = sliderDataArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ScrollAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.scroll_item_layout, null);
        return new ScrollAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ScrollAdapter.ViewHolder holder, int position) {
        final SliderData sliderItem = mSliderItems.get(position);

//        Glide.with(holder.itemView)
//                .load(sliderItem.getImgUrl())
//                .fitCenter()
//                .into(holder.imgView);

        //Picasso.get().load(sliderItem.getImgUrl()).into(holder.imgView);
        Picasso.get().load(sliderItem.getImgUrl()).transform(new CircleCornerForm()).into(holder.imgView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("movie_item_top",  mSliderItems.get(position).getType() + " " + mSliderItems.get(position).getId() + " is click");
                Intent intent = new Intent(context, DetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", sliderItem.getId());
                intent.putExtra("type", sliderItem.getType());
                intent.putExtra("poster_path", sliderItem.getImgUrl());
                intent.putExtra("title", sliderItem.getTitle());
                context.startActivity(intent);
            }
        });



        //pop  click set
        holder.pop_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.pop_view);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                String flag = sliderItem.getType() + "_" + sliderItem.getId() + "_" + sliderItem.getTitle();
                if (SharedHelper.contains(flag, context)) {
                    popupMenu.getMenu().findItem(R.id.add_to_watchlist).setTitle("Remove from Watchlist");
                } else {
                    popupMenu.getMenu().findItem(R.id.add_to_watchlist).setTitle("Add to Watchlist");
                }


                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // Toast message on menu item clicked
                        String imdb_link = "https://www.themoviedb.org/" + sliderItem.getType() + "/" + sliderItem.getId();

                        switch(menuItem.getItemId()) {
                            case R.id.link_tmdb:
                                Uri uri = Uri.parse(imdb_link);
                                context.startActivity(new Intent(Intent.ACTION_VIEW, uri));

                                break;
                            case R.id.share_facebook:
                                String fb_link = "https://www.facebook.com/sharer/sharer.php?u=" + imdb_link;
                                Uri uri_fb = Uri.parse(fb_link);
                                context.startActivity(new Intent(Intent.ACTION_VIEW, uri_fb));
                                break;

                            case R.id.share_twitter:
                                String twitter_link = "https://twitter.com/intent/tweet?text=Check this out! \n" + imdb_link;
                                Uri uri_twitter = Uri.parse(twitter_link);
                                context.startActivity(new Intent(Intent.ACTION_VIEW, uri_twitter));

                                break;

                            case R.id.add_to_watchlist:
                                String key = sliderItem.getType() + "_" + sliderItem.getId() + "_" + sliderItem.getTitle();
                                String value = sliderItem.getImgUrl();
                                String title = sliderItem.getTitle();

                                if (!SharedHelper.contains(key, context)) {
                                    SharedHelper.putString(key, value, context);



                                    Toast.makeText(context, title + "was added to Watchlist", Toast.LENGTH_LONG).show();


//                                    Log.i("Toast", "Added! ");
//                                    if (context == null)
//                                        Log.i("Toast_context", "Context is NULL");

                                } else {
                                    SharedHelper.remove(key, context);
                                    Toast.makeText(context, title + "was removed from Watchlist", Toast.LENGTH_SHORT).show();

//                                    Log.i("Toast", "Removed! ");
//                                    if (context == null)
//                                        Log.i("Toast_context", "Context is NULL");

                                }

                                break;


                        }
//                        Toast.makeText(context, "You Clicked " + menuItem.getTitle(), Toast.LENGTH_LONG).show();
//
                        Log.i("pop_item", "You clicked pop_item:" + menuItem.getTitle());
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSliderItems.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        View pop_view;
        private ImageView imgView;
        public ViewHolder(View itemView) {
            super(itemView);
            imgView = (ImageView) itemView.findViewById(R.id.scroll_item);
            pop_view = (View) itemView.findViewById(R.id.pop_view);
            this.itemView = itemView;
        }
    }

}
