package com.example.dd2.models;

import android.content.Context;
import android.content.SharedPreferences;

public class Music implements Comparable<Music>{
    String title, path, artist, album;
    byte[] pic;
    int image_id;
    boolean isLike = false;
    int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    public Music(String title, String artist, int image_id) {
        this.title = title;
        this.artist = artist;
        this.image_id = image_id;
    }

    public Music() {
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public byte[] getPic() {
        return pic;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    public int getImage_id() {
        return image_id;
    }

    @Override
    public int compareTo(Music o) {
        //return title.compareToIgnoreCase(o.title) ;
        return title.compareToIgnoreCase(o.title);
    }


    public static void likeOrDislike(Context context, Music music){
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName()+"_like", Context.MODE_PRIVATE);
        preferences.edit().putBoolean(music.path, music.isLike).apply();
    }

    public static void removeKey(Context context, Music music){
        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName()+"_like", Context.MODE_PRIVATE);
        preferences.edit().remove(music.path).apply();
    }
}
