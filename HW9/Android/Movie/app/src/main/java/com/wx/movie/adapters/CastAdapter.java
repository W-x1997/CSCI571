package com.wx.movie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wx.movie.R;
import com.wx.movie.models.CastData;
import com.wx.movie.models.SliderData;
import com.wx.movie.tools.CircleCornerForm;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder>{
    private final List<CastData> data_list;
    private Context context;

    public CastAdapter(Context context, ArrayList<CastData> data) {
        this.context = context;
        this.data_list = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast_item_layout, null);
        return new CastAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CastData data_Item = data_list.get(position);

        Picasso.get().load(data_Item.getProfile_path()).into(holder.circleView);
        holder.textView.setText(data_Item.getName());
    }

    @Override
    public int getItemCount() {
        return data_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        private TextView textView;
        private CircleImageView circleView;
        public ViewHolder(View itemView) {
            super(itemView);
            circleView = (CircleImageView) itemView.findViewById(R.id.circle_view);
            textView = (TextView) itemView.findViewById(R.id.tv_castname);
            this.itemView = itemView;
        }
    }
}
