package com.example.dd2;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dd2.commons.Common;
import com.example.dd2.models.Music;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class LoadingActivity extends AppCompatActivity {
    ImageView bgapp, clover;
    LinearLayout textsplash, menus;
    Animation frombottom;
    private int Per_Req = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        frombottom = AnimationUtils.loadAnimation(this, R.anim.logo);

        bgapp = (ImageView) findViewById(R.id.bgapp);
        clover = (ImageView) findViewById(R.id.clover);
        textsplash = (LinearLayout) findViewById(R.id.textsplash);
        menus = (LinearLayout) findViewById(R.id.menus);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();
        menus.startAnimation(frombottom);
        bgapp.animate().translationY(-2800).setDuration(800).setStartDelay(800);
        clover.animate().alpha(0).setDuration(800).setStartDelay(400);
        textsplash.animate().translationY(140).alpha(0).setDuration(800).setStartDelay(600);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Per_Req);
        } else {
            //TODO process
            readMp3();
        }
    }

    private void readMp3() {
        new AsyncTask(){
            @Override
            protected Object doInBackground(Object[] objects) {
                SharedPreferences preferences = getSharedPreferences(getPackageName()+"_like", Context.MODE_PRIVATE);
                if (Common.listMusic.size() == 0){
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    ArrayList<File> files = findSong(new File("/storage/"));
                    ArrayList<File> files2 = findSong(Environment.getExternalStorageDirectory());
                    if (files2 != null){
                        files.addAll(files2);
                    }
                    for (File f : files){
                        Log.d("TEST path", f.getPath());
                        retriever.setDataSource(f.getPath());
                        String album = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                        String artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                        String title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                        if (title == null || title.equals("")) {
                            String name = f.getName();
                            title = name.substring(0, name.lastIndexOf("."));
                        }
                        album = (album == null || album.equals(""))?"Unknow":album;
                        artist = (artist == null || artist.equals(""))?"Unknow":artist;
                        Music music = new Music(title, artist, Common.defaultImageId);
                        music.setAlbum(album);
                        music.setPath(f.getPath());
                        music.setLike(preferences.getBoolean(f.getPath(), false));
                        byte[] pic = retriever.getEmbeddedPicture();
                        if (pic != null)
                            music.setPic(pic);
                        Common.listMusic.add(music);
                    }
                    Collections.sort(Common.listMusic);
                    int size = Common.listMusic.size();
                    for (int i = 0; i < size; i++){
                        Common.listMusic.get(i).setPosition(i);
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Intent intent = new Intent(LoadingActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }.execute();
    }

    public ArrayList<File> findSong(File file){
        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = file.listFiles();
        if (files == null){
            return new ArrayList<>();
        }
        for(File singFile: files){
            if (singFile.isDirectory() && !singFile.isHidden()){
                arrayList.addAll(findSong(singFile));
            }
            else{
                if (singFile.isFile() && singFile.getName().contains(".") && singFile.length() != 0) {
                    if (singFile.getName().endsWith(".mp3") ||
                            singFile.getName().endsWith(".wav")) {
                        arrayList.add(singFile);
                    }
                } else
                    arrayList.addAll(findSong(singFile));
            }
        }
        return  arrayList;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Per_Req) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //TODO process
                readMp3();
                Toast.makeText(this, "Đã cấp quyền truy cập bộ nhớ!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Chưa cấp quyền truy cập bộ nhớ!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
