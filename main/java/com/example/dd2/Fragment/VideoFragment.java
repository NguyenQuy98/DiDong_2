package com.example.dd2.Fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.dd2.MyData_video;
import com.example.dd2.R;
import com.example.dd2.Video_recyclerViewAdapter;
import com.example.dd2.models.Music;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {

    View v;
    VideoView videoView;
    ArrayList<String> videoList;

    private RecyclerView videoRecyclerView;
    private List<Music> Listvideo;
    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_video, container, false);
        videoView = v.findViewById(R.id.videoView);
        //String url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";

        MediaController mediaController = new MediaController(getContext());
        videoView.setMediaController(mediaController);
        //mediaController.setAnchorView(videoView);

        videoRecyclerView = (RecyclerView) v.findViewById(R.id.videorecyclerview);
        Video_recyclerViewAdapter video_recyclerViewAdapter = new Video_recyclerViewAdapter(getContext(),Listvideo);
        videoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        videoRecyclerView.setAdapter(video_recyclerViewAdapter);
        video_recyclerViewAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playVideo(position);
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    public void playVideo(int position){
        int idVideo;
        switch (position){
            case 0:
                idVideo = R.raw.mv_hatchoanhnghe;
                break;
            case 1:
                idVideo = R.raw.video1;
                break;
            case 2:
                idVideo = R.raw.mv_mytam;
                break;
            default:
                idVideo = R.raw.mv_thuytien;
                break;
        }
        String url = "android.resource://" + "com.example.dd2" + "/" + idVideo;
        Uri uri = Uri.parse(url);
        videoView.setVideoURI(uri);
        videoView.start();
    }

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

}
