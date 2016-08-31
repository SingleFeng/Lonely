package com.example.genlan.lonely.server;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.genlan.lonely.data.LocalMusicIndex;

import org.litepal.crud.DataSupport;

/**
 * Created by GenLan on 2016/8/30.
 */
public class MusicService extends Service {


    private MediaPlayer myMediaPlayer;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        playMusic(DataSupport.find(LocalMusicIndex.class,2).getmMusicUrl());
        return super.onStartCommand(intent, flags, startId);
    }

    void playMusic(String path){
        try {
            myMediaPlayer.reset();
            myMediaPlayer.setDataSource(path);
            myMediaPlayer.prepare();
            myMediaPlayer.start();
            myMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    nextMusic();
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private static final String MUSIC_PATH2 = DataSupport.find(LocalMusicIndex.class,3).getmMusicUrl();


    //下一首
    void nextMusic(){
            playMusic(MUSIC_PATH2);
    }
}
