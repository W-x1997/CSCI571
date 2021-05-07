package com.wx.movie.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wx.movie.DetailActivity;
import com.wx.movie.R;
import com.wx.movie.ReviewActivity;
import com.wx.movie.models.CastData;
import com.wx.movie.models.ReviewData;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{

    private List<ReviewData> data_list;
    private Context context;

    public ReviewAdapter(Context context, List<ReviewData> data_list) {
        this.context = context;
        this.data_list = data_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item_layout, null);
        return new ReviewAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ReviewData data_Item = data_list.get(position);
        holder.tv_title.setText(data_Item.getTitle());
        holder.tv_rating.setText(data_Item.getRating());
        holder.tv_content.setText(data_Item.getContent());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReviewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("title", data_Item.getTitle());
                intent.putExtra("rating", data_Item.getRating());
                intent.putExtra("content", data_Item.getContent());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView tv_title;
        TextView tv_rating;
        TextView tv_content;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_content = itemView.findViewById(R.id.tv_detail_review_content);
            tv_rating = itemView.findViewById(R.id.tv_detail_review_rating);
            tv_title = itemView.findViewById(R.id.tv_detail_review_title);
            this.itemView = itemView;
        }
    }
}
