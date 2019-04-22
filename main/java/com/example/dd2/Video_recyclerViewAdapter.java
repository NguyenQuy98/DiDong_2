package com.example.dd2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dd2.models.Music;

import java.util.List;

public class Video_recyclerViewAdapter extends RecyclerView.Adapter<Video_recyclerViewAdapter.MyViewHolder> {
    Context mContext;
    List<Music> mData;

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    };

    public Video_recyclerViewAdapter(Context mContext, List<Music> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.video_item,viewGroup,false);
        MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder,final int i) {
        myViewHolder.tv_nameVideo.setText("Tên MV:"+mData.get(i).getTitle());
        myViewHolder.tv_video.setText("Nhạc Sĩ:"+mData.get(i).getArtist());
        Glide.with(mContext).load(mData.get(i).getImage_id()).into(myViewHolder.img);

        myViewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(null,v,i,0);
            }
        });
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_nameVideo;
        private TextView tv_video;
        private ImageView img;
        View root;
//        private CardView cardView;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_nameVideo = (TextView) itemView.findViewById(R.id.nameVideo);
            tv_video = (TextView) itemView.findViewById(R.id.video);
            img = (ImageView) itemView.findViewById(R.id.image);
            root = itemView.findViewById(R.id.root);
        }
    }
}
