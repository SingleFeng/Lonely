package com.example.genlan.lonely.data;


import org.litepal.crud.DataSupport;

/**
 * Created by GenLan on 2016/8/30.
 * 本地音乐Litepal支持
 */
public class LocalMusicIndex extends DataSupport{

    /**
     * Music id
     * */
    private int mMusicId;

    /**
     * Music title
     * */
    private String mMusicTitle;

    /**
     * Music artist
     * */
    private String mMusicArtist;

    /**
     * Music duration
     * */
    private long mMusicDuration;

    /**
     * Music size
     * */
    private long mMusicSize;

    /**
     * Music path
     * */
    private String mMusicUrl;

    public int getmMusicId() {
        return mMusicId;
    }

    public void setmMusicId(int mMusicId) {
        this.mMusicId = mMusicId;
    }

    public String getmMusicTitle() {
        return mMusicTitle;
    }

    public void setmMusicTitle(String mMusicTitle) {
        this.mMusicTitle = mMusicTitle;
    }

    public String getmMusicArtist() {
        return mMusicArtist;
    }

    public void setmMusicArtist(String mMusicArtist) {
        this.mMusicArtist = mMusicArtist;
    }

    public long getmMusicDuration() {
        return mMusicDuration;
    }

    public void setmMusicDuration(long mMusicDuration) {
        this.mMusicDuration = mMusicDuration;
    }

    public long getmMusicSize() {
        return mMusicSize;
    }

    public void setmMusicSize(long mMusicSize) {
        this.mMusicSize = mMusicSize;
    }

    public String getmMusicUrl() {
        return mMusicUrl;
    }

    public void setmMusicUrl(String mMusicUrl) {
        this.mMusicUrl = mMusicUrl;
    }
}
