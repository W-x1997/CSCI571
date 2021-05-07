package com.wx.movie.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wx.movie.DetailActivity;
import com.wx.movie.R;
import com.wx.movie.models.SearchData;
import com.wx.movie.models.SliderData;
import com.wx.movie.tools.CircleCornerForm;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter  extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{

    private List<SearchData> data_list;
    private Context context;


    public SearchAdapter(Context context, ArrayList<SearchData> data) {
        this.context = context;
        this.data_list = data;
    }


    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_layout, null);
        return new SearchAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        final SearchData data_item = data_list.get(position);

        Picasso.get().load(data_item.getBackdrop_path()).transform(new CircleCornerForm()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.search_view.setBackground(new BitmapDrawable(bitmap));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }

        });

        String str_type_year = data_item.getMedia_type().toUpperCase() + " (" + data_item.getYear() + ")";
        holder.tv_type_year.setText(str_type_year);
        holder.tv_rating.setText(String.valueOf(data_item.getRating()));
        holder.tv_title.setText(data_item.getTitle().toUpperCase());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("movie_item_top",  mSliderItems.get(position).getType() + " " + mSliderItems.get(position).getId() + " is click");
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("id", data_item.getId());
                intent.putExtra("type", data_item.getMedia_type());
                intent.putExtra("poster_path", data_item.getBackdrop_path());
                intent.putExtra("title", data_item.getTitle());
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
        LinearLayout search_view;
        TextView tv_type_year;
        TextView tv_rating;
        TextView tv_title;


        public ViewHolder(View itemView) {
            super(itemView);
            search_view = itemView.findViewById(R.id.search_item_view);
            tv_type_year = itemView.findViewById(R.id.tv_type_year);
            tv_rating = itemView.findViewById(R.id.tv_rating);
            tv_title = itemView.findViewById(R.id.tv_title);
            this.itemView = itemView;
        }
    }
}
