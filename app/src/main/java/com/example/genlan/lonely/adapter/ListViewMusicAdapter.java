package com.example.genlan.lonely.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.genlan.lonely.R;
import com.example.genlan.lonely.data.LocalMusicIndex;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by GenLan on 2016/8/30.
 */
public class ListViewMusicAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<LocalMusicIndex> mList;

    public ListViewMusicAdapter(LayoutInflater mLayoutInflater,List<LocalMusicIndex> mList,
                               Context context) {
        super();
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mList = mList;
        this.context = context;
    }

    public ListViewMusicAdapter(List<LocalMusicIndex> mList,
                                Context context) {
        super();
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mList = mList;
        this.context = context;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_music_list,null);
            mHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_item_music_title);
            mHolder.tvArtist = (TextView) convertView.findViewById(R.id.tv_item_music_artist);
            mHolder.tvDuration = (TextView) convertView.findViewById(R.id.tv_item_music_duration);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        Date date = new Date(mList.get(position).getmMusicDuration());
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String dateStr = sdf.format(date);
        mHolder.tvTitle.setText(mList.get(position).getmMusicTitle());
        mHolder.tvArtist.setText(mList.get(position).getmMusicArtist());
        mHolder.tvDuration.setText(dateStr);
        return convertView;
    }

    private final class ViewHolder {
        TextView tvTitle;
        TextView tvArtist;
        TextView tvDuration;
    }

}
