package com.wx.movie.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wx.movie.DetailActivity;
import com.wx.movie.R;
import com.wx.movie.models.SliderData;
import com.wx.movie.tools.CircleCornerForm;

import java.util.List;

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder> {
    private final List<SliderData> mSliderItems;
    private Context context;

    public RecommendAdapter(Context context, List<SliderData> mSliderItems) {
        this.context = context;
        this.mSliderItems = mSliderItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommend_item_layout, null);
        return new RecommendAdapter.ViewHolder(inflate);
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
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", sliderItem.getId());
                intent.putExtra("type", sliderItem.getType());
                intent.putExtra("poster_path", sliderItem.getImgUrl());
                intent.putExtra("title", sliderItem.getTitle());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mSliderItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        private ImageView imgView;
        public ViewHolder(View itemView) {
            super(itemView);
            imgView = (ImageView) itemView.findViewById(R.id.img_recommend_item);
            this.itemView = itemView;
        }
    }
}
