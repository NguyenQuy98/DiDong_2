package com.example.dd2.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.dd2.HomeMusic;
import com.example.dd2.R;
import com.example.dd2.commons.Common;
import com.example.dd2.models.Music;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class PlayService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener  {

    public static long time = 0;
    //media player
    public static MediaPlayer mediaPlayer;
    public static boolean isRepeat = false;
    //LocalBroadcastManager broadcastManager;
    public static int currentId = -1;
    public static boolean isStart = false;
    Notification notification;
    private String PREV_ACTION = "PREV_ACTION";
    RemoteViews contentView;
    private String PAUSE_ACTION = "PAUSE_ACTION";

    Handler handler = new Handler();
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TEST service", "onCreate");
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            Log.d("TEST service", "mediaPlayer nul");
        }
        initMusicPlayer();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            //notificationManager.createNotificationChannelGroup(new NotificationChannelGroup("beetrack_group", "Beetrack notification"));
            NotificationChannel channel = new NotificationChannel("Music",
                    "Thông báo",
                    NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
        }
        Intent intent = new Intent(this, HomeMusic.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder build = new NotificationCompat.Builder(this, "Music")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setVibrate(null)
                .setSound(null)
                .setContentIntent(pendingIntent);
        notification = build.build();
        contentView = new RemoteViews(getPackageName(), R.layout.layout_control_notification);
        //prev action
        Intent prevReceive = new Intent();
        //set action
        prevReceive.setAction(PREV_ACTION);
        PendingIntent pendingIntentPrev = PendingIntent.getBroadcast(this, 12345, prevReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        //day là set su kien click nut prev
        //no càn params pendingintent
        //mà trong service ko tuong tac như trong màn hìh bt
        //cho nên phải dùng broastcast receiver để xử lý
        //mà broastcast receiver nó bắt theo action cho nên phải gắn action
        contentView.setOnClickPendingIntent(R.id.btnPrev, pendingIntentPrev);

        //pause action
        Intent pauseReceive = new Intent();
        pauseReceive.setAction(PAUSE_ACTION);
        PendingIntent pendingIntentPause = PendingIntent.getBroadcast(this, 12345, pauseReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.btnPlay, pendingIntentPause);

        //next action
        Intent nextReceive = new Intent();
        String NEXT_ACTION = "NEXT_ACTION";
        nextReceive.setAction(NEXT_ACTION);
        PendingIntent pendingIntentNext = PendingIntent.getBroadcast(this, 12345, nextReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.btnNext, pendingIntentNext);

        IntentFilter filter = new IntentFilter();
        filter.addAction(PREV_ACTION);
        filter.addAction(PAUSE_ACTION);
        filter.addAction(NEXT_ACTION);

        //mình phải đăng ký receiver thì mói nhan5 dc9 tin hieu
        registerReceiver(receiver, filter);
        notification.contentView = contentView;
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (time != 0 && System.currentTimeMillis() >= time){
                    stopService(new Intent(PlayService.this, PlayService.class));
                    handler.removeCallbacks(this);
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //day là noi receiver nhận tín hiệu từ broastcast
            //tín hiệu là action

            String action = intent.getAction();
            //này là xử lý theo action tương úng
            if (action != null){
                if (action.equals(PREV_ACTION)){
                    Common.selectId--;
                    if (Common.selectId < 0){
                        Common.selectId = Common.listMusic.size() - 1;
                    }
                    playSong(getApplicationContext(), Common.selectId);
                    startForeground();
                }else if (action.equals(PAUSE_ACTION)){
                    Log.d("TEST", "pause");
                    context.stopService(new Intent(context, PlayService.class));
                }else {
                    Common.selectId++;
                    if (Common.selectId == Common.listMusic.size()){
                        Common.selectId = 0;
                    }
                    playSong(getApplicationContext(), Common.selectId);
                    startForeground();
                }
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("TEST service", "onStartCommand");
        playSong(getApplicationContext(), Common.selectId);
        startForeground();
        return START_STICKY;
    }

    void startForeground(){
        Music music = Common.listMusic.get(Common.selectId);
        Bitmap bitmap = null;
        if (music.getPic() != null){
            ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(music.getPic());
            bitmap = BitmapFactory.decodeStream(arrayInputStream);
        }
        if (bitmap == null){
            contentView.setImageViewResource(R.id.image, R.drawable.d1);
        }else
            contentView.setImageViewBitmap(R.id.image, bitmap);
        contentView.setTextViewText(R.id.title, music.getTitle());
        contentView.setTextViewText(R.id.artist, music.getArtist());
        startForeground(9, notification);
    }

    public void initMusicPlayer(){
        mediaPlayer.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
    }

    public static void playSong(Context context, int position){
        if (currentId == position){
            mediaPlayer.start();
            return;
        }
        currentId = position;
        mediaPlayer.stop();
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(context, Uri.parse(Common.listMusic.get(position).getPath()));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (!isRepeat){
            Common.selectId++;
            if (Common.selectId > Common.listMusic.size() - 1){
                Common.selectId = 0;
            }
        }
        playSong(getApplicationContext(), Common.selectId);
        startForeground();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d("TEST service", "onTaskRemoved");
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        mediaPlayer.pause();
        stopForeground(true);
        Log.d("TEST service", "onDestroy");
        super.onDestroy();
    }
}
