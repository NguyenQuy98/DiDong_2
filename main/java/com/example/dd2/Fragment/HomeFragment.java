package com.example.dd2.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.example.dd2.MyData_video;
import com.example.dd2.R;
import com.example.dd2.Video_recyclerViewAdapter;
import com.example.dd2.adapters.MusicAdapter;
import com.example.dd2.commons.Common;
import com.example.dd2.models.Music;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    SliderLayout sliderLayout;
    View v;
    private RecyclerView rvMusic,videoRecyclerView;
    private List<Music> Listvideo;

    public interface Listener{
        void onItemVideoClick(int position);
    }

    Listener listener = new Listener() {
        @Override
        public void onItemVideoClick(int position) {

        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, container, false);
        //slider show
        sliderLayout =(SliderLayout) v.findViewById(R.id.imageSlider);
        //thiết lập hoạt hình chỉ báo bằng cách sử dụng SliderLayout.IndicatorAnimations. : WORM hoặc THIN_WORM hoặc MÀU SẮC hoặc DROP hoặc FILL hoặc NONE hoặc SCALE hoặc SCALE_DOWN hoặc SLIDE và SWAP
        sliderLayout.setIndicatorAnimation(IndicatorAnimations.SWAP);
        sliderLayout.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
        // set giây
        sliderLayout.setScrollTimeInSec(3);
        setSliderViews();


        rvMusic = (RecyclerView) v.findViewById(R.id.recyclerview);
        videoRecyclerView = (RecyclerView) v.findViewById(R.id.videorecyclerview);
        MusicAdapter musicAdapter = new MusicAdapter(true, getContext(), Common.listMusic);
        rvMusic.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvMusic.setAdapter(musicAdapter);

        Video_recyclerViewAdapter video_recyclerViewAdapter = new Video_recyclerViewAdapter(getContext(),Listvideo);
        videoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        videoRecyclerView.setAdapter(video_recyclerViewAdapter);

        video_recyclerViewAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onItemVideoClick(position);
            }
        });

        return v;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Listvideo = new ArrayList<>();
        for (int i = 0; i < MyData_video.namevideoArray.length; i++) {
            Listvideo.add(new Music(
                    MyData_video.namevideoArray[i],
                    MyData_video.videoArray[i],
                    MyData_video.imgvideoArray[i]
            ));
        }
    }
    private void setSliderViews() {
        for (int i = 0; i <= 3; i++) {
            final DefaultSliderView sliderView = new DefaultSliderView(getContext());
            switch (i) {
                case 0:
                    sliderView.setImageDrawable(R.drawable.banner1);
                    break;
                case 1:
                    sliderView.setImageDrawable(R.drawable.banner2);
                    break;
                case 2:
                    sliderView.setImageDrawable(R.drawable.banner3);
                    break;
                case 3:
                    sliderView.setImageDrawable(R.drawable.banner4);
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            sliderView.setDescription("Nhạc hôm nay có gì hay.\n" +
                    "Top nhạc xếp hạng. " + (i + 1));
            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView);
        }
    }
}
