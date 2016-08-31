package com.example.genlan.lonely.server;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
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
    private static int mMusicCount;
    private static boolean isFirstCreate;
    private static onPlayingListener mListener;

    public void setPlayingListener(onPlayingListener mListener) {
        this.mListener = mListener;
    }

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
        LogUtil.d(getClass(), "---------------onStartCommand---------------");
        mMusicIndex = intent.getIntExtra(Config.MUSIC_INDEX, 0);
        boolean isPlay = intent.getBooleanExtra(Config.MUSIC_IS_PLAYING, false);
        LogUtil.d(getClass(), "传值：" + mMusicIndex);
        List<LocalMusicIndex> list = DataSupport.findAll(LocalMusicIndex.class);
        if (isPlay) {
            LogUtil.d(getClass(), "---------------onStartCommand_true---------------");
            myMediaPlayer.pause();
        } else {
            LogUtil.d(getClass(), "---------------onStartCommand_false---------------");
            if (isFirstCreate) playMusic(list);
            else myMediaPlayer.start();
        }
        return super.onStartCommand(intent, flags, startId);
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
            int rtn = 0;
            if (myMediaPlayer != null) {
                rtn = myMediaPlayer.getCurrentPosition();
            }
            return rtn;
        }

        public void seekTo(int position) {
            if (myMediaPlayer != null) {
                myMediaPlayer.seekTo(position);
            }
        }

    }

    public interface onPlayingListener {
        void onPlaying(LocalMusicIndex music);
    }
}
