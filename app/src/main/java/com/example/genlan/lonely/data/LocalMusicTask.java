package com.example.genlan.lonely.data;

import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.example.genlan.lonely.base.BaseApplication;
import com.example.genlan.lonely.util.LogUtil;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by GenLan on 2016/8/30.
 *
 */
public class LocalMusicTask extends AsyncTask<Integer,Integer,List<LocalMusicIndex>> {

    private LocalMusicIndex musicIndex;
    private onReadMusicListener listener;

    public void setListener(onReadMusicListener listener){
        this.listener = listener;
    }

    @Override
    protected List<LocalMusicIndex> doInBackground(Integer... params) {
        DataSupport.deleteAll(LocalMusicIndex.class);
        Cursor cursor = BaseApplication.getInstance().getApplicationContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor.getCount() != 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                musicIndex = new LocalMusicIndex();
                musicIndex.setmMusicTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                musicIndex.setmMusicArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                musicIndex.setmMusicDuration(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                musicIndex.setmMusicSize(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
                musicIndex.setmMusicUrl(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                musicIndex.save();
            }
        }
        List<LocalMusicIndex> list = DataSupport.findAll(LocalMusicIndex.class);

        return list;
    }

    @Override
    protected void onPostExecute(List<LocalMusicIndex> localMusicIndices) {
        super.onPostExecute(localMusicIndices);
        listener.onSuccess(localMusicIndices);
    }

    public interface onReadMusicListener{
        void onSuccess(List<LocalMusicIndex> list);
    }
}
