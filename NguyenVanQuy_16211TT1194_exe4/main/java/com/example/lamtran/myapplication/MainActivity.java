package com.example.lamtran.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {


    private VideoView videoView, video;
    private int position = 0;
    private MediaController mediaController;
    private MediaController mediaController1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        videoView = (VideoView) findViewById(R.id.videoView);

        video = (VideoView) findViewById(R.id.video);

        // Tạo bộ điều khiển
        if (mediaController == null) {
            mediaController = new MediaController(MainActivity.this);

            // Neo vị trí của MediaController với VideoView.
            mediaController.setAnchorView(videoView);


            // Sét đặt bộ điều khiển cho VideoView.
            videoView.setMediaController(mediaController);
/*
            mediaController.setAnchorView(video);

            // Sét đặt bộ điều khiển cho VideoView.
            video.setMediaController(mediaController);*/
        }

        if (mediaController1 == null) {
            mediaController1 = new MediaController(MainActivity.this);

            // Neo vị trí của MediaController với VideoView.


            mediaController1.setAnchorView(video);

            // Sét đặt bộ điều khiển cho VideoView.
            video.setMediaController(mediaController1);
        }


        try {
            // ID của file video.
            int id = this.getRawResIdByName("myvideo");
            int id2 = this.getRawResIdByName("videoplayback");
            videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + id));
           video.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + id2));

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoView.requestFocus();

       video.requestFocus();

        // Sự kiện khi file video sẵn sàng để chơi.
        videoView.setOnPreparedListener(new OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {


                videoView.seekTo(position);
             /*   video.seekTo(position);*/
                if (position == 0) {
                    videoView.start();

                }

                // Khi màn hình Video thay đổi kích thước
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

                        // Neo lại vị trí của bộ điều khiển của nó vào VideoView.
                        mediaController.setAnchorView(videoView);


                    }
                });
            }
        });

       video.setOnPreparedListener(new OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {



                video.seekTo(position);
                if (position == 0) {

                    video.start();
                }

                // Khi màn hình Video thay đổi kích thước
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

                        // Neo lại vị trí của bộ điều khiển của nó vào VideoView.


                        mediaController1.setAnchorView(video);
                    }
                });
            }
        });

    }




    // Tìm ID ứng với tên của file nguồn (Trong thư mục raw).
    public int getRawResIdByName(String resName) {
        String pkgName = this.getPackageName();

        // Trả về 0 nếu không tìm thấy.
        int resID = this.getResources().getIdentifier(resName, "raw", pkgName);
        Log.i("AndroidVideoView", "Res Name: " + resName + "==> Res ID = " + resID);
        return resID;
    }





    // Khi bạn xoay điện thoại, phương thức này sẽ được gọi
    // nó lưu trữ lại ví trí file video đang chơi.
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        // Lưu lại vị trí file video đang chơi.
        savedInstanceState.putInt("CurrentPosition", videoView.getCurrentPosition());
        videoView.pause();

        savedInstanceState.putInt("CurrentPosition", video.getCurrentPosition());
        video.pause();
    }


    // Sau khi điện thoại xoay chiều xong. Phương thức này được gọi,
    // bạn cần tái tạo lại ví trí file nhạc đang chơi.
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Lấy lại ví trí video đã chơi.
        position = savedInstanceState.getInt("CurrentPosition");
        videoView.seekTo(position);
        video.seekTo(position);
    }
}

