package com.example.dd2.Fragment;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.dd2.R;
import com.example.dd2.commons.Common;
import com.example.dd2.services.PlayService;

import java.text.SimpleDateFormat;

import dyanamitechetan.vusikview.VusikView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayMusicFragment extends Fragment {


    VusikView vusikView;
    private TextView tvNameSong;
    private TextView tvTimeStart;
    private TextView tvTimeTotal;
    SeekBar sbTime;
    SimpleDateFormat formatHour = new SimpleDateFormat("mm:ss");
    private Runnable runnable;
    Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play_music, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        tvNameSong = v.findViewById(R.id.tvNameSong);
        tvTimeStart = v.findViewById(R.id.tvTimeStart);
        tvTimeTotal = v.findViewById(R.id.tvTimeTotal);
        sbTime = v.findViewById(R.id.sbTime);
        vusikView = v.findViewById(R.id.vusik);
        vusikView.start();
        vusikView.startNotesFall();
        vusikView.setTag(true);

        sbTime.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);

        sbTime.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        sbTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                PlayService.mediaPlayer.seekTo(sbTime.getProgress());
            }
        });

        runnable = new Runnable() {
            @Override
            public void run() {
                setNameSong(Common.listMusic.get(Common.selectId).getTitle());
                if (PlayService.mediaPlayer != null){
                    tvTimeStart.setText(formatHour.format(PlayService.mediaPlayer.getCurrentPosition()));
                    tvTimeTotal.setText(formatHour.format(PlayService.mediaPlayer.getDuration()));
                    sbTime.setProgress(PlayService.mediaPlayer.getCurrentPosition());
                    sbTime.setMax(PlayService.mediaPlayer.getDuration());
                    if (!PlayService.mediaPlayer.isPlaying()){
                        listener.onPause();
                        if ((boolean)vusikView.getTag()){
                            vusikView.pauseNotesFall();
                            vusikView.setTag(false);
                        }
                        handler.removeCallbacks(this);

                    }else{
                        if (!(boolean)vusikView.getTag()){
                            vusikView.resumeNotesFall();
                            vusikView.setTag(true);
                        }
                        listener.onPlaying();
                    }
                }
                handler.postDelayed(runnable, 1000);
            }
        };
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    Listener listener = new Listener() {
        @Override
        public void onPause() {

        }

        @Override
        public void onPlaying() {

        }
    };


    public interface Listener{
        void onPause();
        void onPlaying();
    }

    public void setNameSong(String title) {
        if (tvNameSong != null)
            tvNameSong.setText(title);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TEST", "PlayMusicFragment onResume");
        handler.removeCallbacks(runnable);
        handler.post(runnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
}
