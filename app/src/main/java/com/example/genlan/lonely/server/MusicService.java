package com.example.genlan.lonely.server;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.example.genlan.lonely.R;
import com.example.genlan.lonely.activity.MainActivity;
import com.example.genlan.lonely.config.Config;
import com.example.genlan.lonely.data.LocalMusicIndex;
import com.example.genlan.lonely.util.LogUtil;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by GenLan on 2016/8/30.
 * 音乐的后台播放服务
 */
public class MusicService extends Service {


    private MediaPlayer myMediaPlayer;
    private static int mMusicIndex = 0;
    private static int mMusicCount = 0;
    private static boolean isFirstCreate = false;
    private static onPlayingListener mListener;
    private MyBind myBind = new MyBind();

    public void setPlayingListener(onPlayingListener mListener) {
        MusicService.mListener = mListener;
    }

    @Override
    public void onCreate() {
        LogUtil.d(getClass(), "---------------onCreate---------------");
        super.onCreate();
        myMediaPlayer = new MediaPlayer();
        isFirstCreate = true;
    }

    @Override
    public void onDestroy() {
        LogUtil.d(getClass(), "---------------onDestroy---------------");
        super.onDestroy();
        isFirstCreate = false;
        myMediaPlayer.release();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBind;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d(getClass(), "---------------onStartCommand---------------");
        mMusicIndex = intent.getIntExtra(Config.MUSIC_INDEX, 0);
        boolean isPlay = intent.getBooleanExtra(Config.MUSIC_IS_PLAYING, false);
        List<LocalMusicIndex> list = DataSupport.findAll(LocalMusicIndex.class);
        initPlay(isPlay,list);
//        initNotification(list);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtil.d(getClass(), "---------------onUnbind---------------");
        return super.onUnbind(intent);
    }

    /*    private void initNotification(){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        Intent intent = new Intent(this, MainActivity.class);
        RemoteViews views = new RemoteViews(getPackageName(),R.layout.item_notification_music);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);

        builder.setContent(views);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
//        notificationManager.notify(100,notification);
        startForeground(0x111, notification);
    }*/

    private void initNotification(List<LocalMusicIndex> list){
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,new Intent(this, MainActivity.class),0);
        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentTitle(list.get(mMusicIndex).getmMusicTitle())
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pendingIntent)
                .setProgress(myMediaPlayer.getDuration(),myMediaPlayer.getCurrentPosition(),true)
                .setDeleteIntent(null)
                .build();
        startForeground(0x111, notification);
    }

    private void initPlay(boolean isPlay,List<LocalMusicIndex> list){
        if (isPlay) {
            myMediaPlayer.pause();
        } else {
            if (isFirstCreate) playMusic(list);
            else myMediaPlayer.start();
        }
    }

    private void playMusic(final List<LocalMusicIndex> list) {
        isFirstCreate = false;
        mMusicCount = list.size();
        String path = list.get(mMusicIndex).getmMusicUrl();
        mListener.onPlaying(list.get(mMusicIndex));
        try {
            myMediaPlayer.reset();
            myMediaPlayer.setDataSource(path);
            myMediaPlayer.prepare();
            myMediaPlayer.start();
            myMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    if (mMusicIndex <= mMusicCount - 1) {
                        mMusicIndex = mMusicIndex + 1;
                    } else mMusicIndex = 0;
                    playMusic(list);
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public class MyBind extends Binder {
        //获取歌曲长度
        public int getMusicDuration() {
            int rtn = 0;
            if (myMediaPlayer != null) {
                rtn = myMediaPlayer.getDuration();
            }
            return rtn;
        }

        //获取当前播放进度
        public int getMusicCurrentPosition() {
            if (myMediaPlayer != null) {
                return myMediaPlayer.getCurrentPosition();
            }
            return 0;
        }

        public void seekTo(int position) {
            if (myMediaPlayer != null) {
                myMediaPlayer.seekTo(position);
            }
        }

        public void musicPause(){
            if (myMediaPlayer != null){
                myMediaPlayer.pause();
            }
        }

        public void musicRestart(){
            if (myMediaPlayer != null){
                myMediaPlayer.start();
            }
        }

    }

    public interface onPlayingListener {
        void onPlaying(LocalMusicIndex music);
    }
}
