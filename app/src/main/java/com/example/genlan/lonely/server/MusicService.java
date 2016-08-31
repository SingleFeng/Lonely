package com.example.genlan.lonely.server;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.genlan.lonely.config.Config;
import com.example.genlan.lonely.data.LocalMusicIndex;
import com.example.genlan.lonely.util.LogUtil;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by GenLan on 2016/8/30.
 */
public class MusicService extends Service {


    private MediaPlayer myMediaPlayer;
    private static int mMusicIndex;
    private static boolean isFirstCreate;

    @Override
    public void onCreate() {
        super.onCreate();
        myMediaPlayer = new MediaPlayer();
        isFirstCreate = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isFirstCreate = false;
        myMediaPlayer.release();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d(getClass(),"---------------onStartCommand---------------");
        mMusicIndex = intent.getIntExtra(Config.MUSIC_INDEX, 0);
        boolean isPlay = intent.getBooleanExtra(Config.MUSIC_IS_PLAYING, false);
        LogUtil.d(getClass(), "传值：" + mMusicIndex);
        List<LocalMusicIndex> list = DataSupport.findAll(LocalMusicIndex.class);
        if (isPlay) {
            LogUtil.d(getClass(),"---------------onStartCommand_true---------------");
            myMediaPlayer.pause();
        } else {
            LogUtil.d(getClass(),"---------------onStartCommand_false---------------");
            if (isFirstCreate) playMusic(list);
            else myMediaPlayer.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void playMusic(final List<LocalMusicIndex> list) {
        isFirstCreate = false;
        String path = list.get(mMusicIndex).getmMusicUrl();
        try {
            myMediaPlayer.reset();
            myMediaPlayer.setDataSource(path);
            myMediaPlayer.prepare();
            myMediaPlayer.start();
            myMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    playMusic(list);
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
