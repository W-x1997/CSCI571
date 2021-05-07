package com.wx.movie.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wx.movie.DetailActivity;
import com.wx.movie.R;
import com.wx.movie.models.SliderData;
import com.wx.movie.tools.CircleCornerForm;
import com.wx.movie.tools.SharedHelper;

import java.util.List;

public class WatchListAdapter extends RecyclerView.Adapter< WatchListAdapter.ViewHolder> {

    private final List<SliderData> mSliderItems;
    private Context context;

    public WatchListAdapter(Context context, List<SliderData> mSliderItems) {
        this.context = context;
        this.mSliderItems = mSliderItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.watchlist_item_layout, null);
        return new WatchListAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SliderData sliderItem = mSliderItems.get(position);
        Picasso.get().load(sliderItem.getImgUrl()).transform(new CircleCornerForm()).into(holder.imgView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("movie_item_top",  mSliderItems.get(position).getType() + " " + mSliderItems.get(position).getId() + " is click");
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("id", sliderItem.getId());
                intent.putExtra("type", sliderItem.getType());
                intent.putExtra("poster_path", sliderItem.getImgUrl());
                intent.putExtra("title", sliderItem.getTitle());
                context.startActivity(intent);
            }
        });

        if (sliderItem.getType().equals("movie")) {
            holder.textView.setText("Movie");
        }

        if (sliderItem.getType().equals("tv")) {
            holder.textView.setText("TV");
        }

        holder.remove_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete from our watchlist
                String key = sliderItem.getType() + "_" + sliderItem.getId() + "_" + sliderItem.getTitle();
                SharedHelper.remove(key, context);
                mSliderItems.remove(sliderItem);
                notifyDataSetChanged();
                Toast.makeText(context, sliderItem.getTitle() + "was removed from Watchlist", Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return mSliderItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
    View itemView;
    private View remove_view;
    private TextView textView;
    private ImageView imgView;
    public ViewHolder(View itemView) {
        super(itemView);
        imgView = (ImageView) itemView.findViewById(R.id.img_watchlist_item);
        textView = (TextView) itemView.findViewById(R.id.tv_watchlist_type);
        remove_view = (View) itemView.findViewById(R.id.remove_view);
        this.itemView = itemView;
    }
}
}
