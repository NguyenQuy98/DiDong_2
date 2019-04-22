package com.example.dd2.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dd2.HomeMusic;
import com.example.dd2.R;
import com.example.dd2.commons.Common;
import com.example.dd2.models.Music;
import com.example.dd2.services.PlayService;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> implements Filterable {
    Context mContext;
    List<Music> mData;
    int layout;
    private boolean isSortASC;
    private HomeMusic.OptionLike optionLike;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<Music> arrayList = new ArrayList<>();
                for (Music music : Common.listMusic) {
                    if (constraint.toString().equals("") || music.getTitle().toLowerCase().toLowerCase().contains(constraint.toString())) {
                        switch (optionLike) {
                            case LIKE:
                                if (music.isLike()) {
                                    arrayList.add(music);
                                }
                                break;
                            case DISLIKE:
                                if (!music.isLike()) {
                                    Log.d("TEST", "Đã vào");
                                    arrayList.add(music);
                                }
                                break;
                            default:
                                arrayList.add(music);
                                break;
                        }
                    }
                }
                if (isSortASC) {
                    Collections.sort(arrayList);
                } else {
                    Collections.reverse(arrayList);
                }
                results.values = arrayList;
                //results.count = arrayList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mData.clear();
                mData.addAll((ArrayList<Music>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public void setConditionFilter(boolean isSortASC, HomeMusic.OptionLike optionLike) {
        this.isSortASC = isSortASC;
        this.optionLike = optionLike;
    }

    public interface Listener {
        void onItemLike();

        void onItemClick();
    }

    Listener listener = new Listener() {
        @Override
        public void onItemLike() {

        }

        @Override
        public void onItemClick() {

        }
    };

    public MusicAdapter(boolean isHome, Context mContext, List<Music> mData) {
        this.mContext = mContext;
        this.mData = new ArrayList<>(mData);
        if (isHome) {
            layout = R.layout.home_item;
        } else
            layout = R.layout.music_item;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(layout, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.tv_name.setText(mData.get(i).getTitle());
        myViewHolder.tv_music.setText(mData.get(i).getArtist());
        if (mData.get(i).getPic() == null)
            myViewHolder.img.setImageResource(mData.get(i).getImage_id());
        else
            Glide.with(mContext).load(mData.get(i).getPic()).into(myViewHolder.img);

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.selectId = mData.get(i).getPosition();
                PlayService.isStart = true;
                Intent intent = new Intent(mContext, HomeMusic.class);
                mContext.startActivity(intent);
                listener.onItemClick();
            }
        });
        if (myViewHolder.btnDelete != null) {
            myViewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Cảnh báo");
                    builder.setMessage("Bạn muốn xoá bài hát này?");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            File file = new File(mData.get(i).getPath());
                            if (file.exists()) {
                                file.delete();
                            }
                            Music.removeKey(mContext, mData.get(i));
                            Toast.makeText(mContext, "Đã xoá " + mData.get(i).getTitle(), Toast.LENGTH_LONG).show();
                            notifyItemRemoved(i);
                            mData.remove(i);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    notifyDataSetChanged();
                                }
                            }, 500);

                        }
                    });
                    builder.setPositiveButton("Huỷ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            });
        }
        if (myViewHolder.btnLike != null) {
            if (mData.get(i).isLike()) {
                myViewHolder.btnLike.setBackground(mContext.getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
            } else {
                myViewHolder.btnLike.setBackground(mContext.getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
            }
            myViewHolder.btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isLike = mData.get(i).isLike();
                    mData.get(i).setLike(!isLike);
                    String s = "";
                    if (mData.get(i).isLike()) {
                        s = "Đã thêm yêu thích ";
                        myViewHolder.btnLike.setBackground(mContext.getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                    } else {
                        s = "Đã bỏ yêu thích ";
                        myViewHolder.btnLike.setBackground(mContext.getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                    }
                    Music.likeOrDislike(mContext, mData.get(i));
                    listener.onItemLike();
                    Toast.makeText(mContext, s + mData.get(i).getTitle(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private TextView tv_music;
        private ImageView img;
        private CardView cardView;
        private ImageButton btnLike, btnDelete;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.title);
            tv_music = (TextView) itemView.findViewById(R.id.artist);
            img = (ImageView) itemView.findViewById(R.id.image);
            cardView = (CardView) itemView.findViewById(R.id.itemMusic);
            if (layout == R.layout.music_item) {
                btnLike = itemView.findViewById(R.id.btnLike);
                btnDelete = itemView.findViewById(R.id.btnDelete);
            }
        }
    }
}
