package com.appsereno.viewmodel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.appsereno.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class BucketsAdapter extends RecyclerView.Adapter<BucketsAdapter.MyViewHolder>{
    private List<String> bucketNames,bitmapList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail=(ImageView) view.findViewById(R.id.image);
        }
    }

    public BucketsAdapter(List<String> bucketNames,List<String> bitmapList,Context context) {
        this.bucketNames=bucketNames;
        this.bitmapList = bitmapList;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(position<=bucketNames.size()-1) {
            bucketNames.get(position);
            holder.title.setText(bucketNames.get(position));
            Glide.with(context).load("file://" + bitmapList.get(position)).apply(new RequestOptions().override(300, 300).centerCrop()).into(holder.thumbnail);
        }else{
            holder.title.setText("vacio");
            holder.thumbnail.setImageResource(R.drawable.ic_round_insert_photo_24);
            holder.thumbnail.setPadding(1,0,1,0);
        }
    }

    @Override
    public int getItemCount() {
        return bucketNames.size()>9?bucketNames.size():10;
    }
}