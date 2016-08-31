package com.example.genlan.lonely.activity.mainfragment;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.genlan.lonely.R;
import com.example.genlan.lonely.adapter.ListViewMusicAdapter;
import com.example.genlan.lonely.config.Config;
import com.example.genlan.lonely.data.LocalMusicIndex;
import com.example.genlan.lonely.data.LocalMusicTask;
import com.example.genlan.lonely.server.MusicService;
import com.example.genlan.lonely.util.LogUtil;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.litepal.crud.DataSupport;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by GenLan on 2016/8/26.
 * Music
 */
public class MusicFragment extends Fragment implements View.OnClickListener, LocalMusicTask.onReadMusicListener, MusicService.onPlayingListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private PullToRefreshListView mListView;
    private ImageButton btnLast, btnNext, btnPlay, btnSearch, btnStop;
    private TextView tvOnPlayTitle, tvOnPlayArtist, tvOnPlayTime;
    //    private ProgressBar pb;
    private SeekBar sbr;

    private List<LocalMusicIndex> mList;
    private boolean isPlaying = false;
    private static int mMusicIndex;
    private static int mMusicCount;

    private MusicService musicService;
    private MusicService.MyBind mybind;
    private ServiceConnection serviceConnection;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;

    public MusicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MusicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MusicFragment newInstance(String param1, String param2) {
        MusicFragment fragment = new MusicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_second, container, false);
        initButton(view);
        musicService = new MusicService();
        mListView = (PullToRefreshListView) view.findViewById(R.id.lv_music);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mMusicIndex = position - 1;
                stopMusicService();
                playMusic(mMusicIndex);
            }
        });
//        initServiceConnection();
        return view;
    }

    private void initButton(View v) {
        btnLast = (ImageButton) v.findViewById(R.id.btn_music_last);
        btnNext = (ImageButton) v.findViewById(R.id.btn_music_next);
        btnPlay = (ImageButton) v.findViewById(R.id.btn_music_play);
        btnStop = (ImageButton) v.findViewById(R.id.btn_music_stop);
//        pb = (ProgressBar) v.findViewById(R.id.pb_music);
        sbr = (SeekBar) v.findViewById(R.id.sb_music);
        tvOnPlayArtist = (TextView) v.findViewById(R.id.tv_item_music_artist);
        tvOnPlayTime = (TextView) v.findViewById(R.id.tv_item_music_duration);
        tvOnPlayTitle = (TextView) v.findViewById(R.id.tv_item_music_title);
        btnSearch = (ImageButton) v.findViewById(R.id.btn_music_read_in_sdcard);
        btnSearch.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnLast.setOnClickListener(this);
    }

    /**
     * 音乐列表
     */
    private void setListView() {
        LocalMusicTask musicTask = new LocalMusicTask();
        musicTask.execute();
        musicTask.setListener(this);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalMusicTask musicTask = new LocalMusicTask();
        musicTask.execute();
        musicTask.setListener(new LocalMusicTask.onReadMusicListener() {
            @Override
            public void onSuccess(List<LocalMusicIndex> list) {
                mMusicCount = list.size();
                mListView.setAdapter(new ListViewMusicAdapter(list, getActivity()));
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //// TODO: 2016/8/30 点击事件
            case R.id.btn_music_last:
                if (isPlaying) stopMusicService();
                if (mMusicIndex - 1 >= 0) {
                    playMusic(mMusicIndex = mMusicIndex - 1);
                } else {
                    playMusic(mMusicIndex = mMusicCount - 1);
                }
                break;
            case R.id.btn_music_next:
                if (isPlaying) stopMusicService();
                if (mMusicIndex + 1 <= mMusicCount - 1) {
                    playMusic(mMusicIndex = mMusicIndex + 1);
                } else {
                    playMusic(mMusicIndex = 0);
                }
                break;
            case R.id.btn_music_play:
                playMusic();
                break;
            case R.id.btn_music_stop:
                btnPlay.setImageResource(R.drawable.icon_music_play);
                stopMusicService();
                break;
            case R.id.btn_music_read_in_sdcard:
                setListView();
                break;
            default:
                break;
        }
    }

    /**
     * 停止音乐播放
     */
    private void stopMusicService() {
        Intent intent = new Intent(getActivity(), MusicService.class);
        getActivity().stopService(intent);
//        getActivity().unbindService(serviceConnection);
    }

    /*
     * 根据MVC思想，应将播放音乐的控制方式从Fragment中剥离
     * */

    /**
     * 开始音乐播放
     * 播放第一曲
     */
    private void playMusic() {
        LogUtil.d(getClass(), "---------------playMusic---------------");
        Intent intent = new Intent(getActivity(), MusicService.class);
        if (!isPlaying) {
            LogUtil.d(getClass(), "---------------playMusic_True---------------");
            intent.putExtra(Config.MUSIC_INDEX, 0);
            intent.putExtra(Config.MUSIC_IS_PLAYING, false);
            isPlaying = true;
            btnPlay.setImageResource(R.drawable.icon_music_pause);
        } else {
            LogUtil.d(getClass(), "---------------playMusic_False---------------");
            intent.putExtra(Config.MUSIC_IS_PLAYING, true);
            isPlaying = false;
            btnPlay.setImageResource(R.drawable.icon_music_play);
        }
        getActivity().startService(intent);
//        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 开始音乐播放
     *
     * @param musicIndex 音乐序号
     */
    private void playMusic(int musicIndex) {
        LogUtil.d(getClass(), "---------------playMusic---------------");
        Intent intent = new Intent(getActivity(), MusicService.class);
        intent.putExtra(Config.MUSIC_INDEX, musicIndex);
        intent.putExtra(Config.MUSIC_IS_PLAYING, false);
        isPlaying = true;
        btnPlay.setImageResource(R.drawable.icon_music_pause);
        getActivity().startService(intent);
//        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        musicService.setPlayingListener(this);
    }

    private void initServiceConnection() {
        if (serviceConnection == null) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    mybind = (MusicService.MyBind) service;
                    //设置进度条的最大长度
                    int max = mybind.getMusicDuration();
//                    pb.setMax(max);
                    sbr.setMax(max);
                    sbr.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                            mybind.seekTo(progress);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });

                    //连接之后启动子线程设置当前进度
                    new Thread() {
                        public void run() {
                            //改变当前进度条的值
                            //设置当前进度
                            while (true) {
//                                pb.setProgress(mybind.getMusicCurrentPosition());
                                sbr.setProgress(mybind.getMusicCurrentPosition());
//                                try {
//                                    Thread.sleep(100);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
                            }
                        }
                    }.start();

                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };
        }
    }

    @Override
    public void onSuccess(List<LocalMusicIndex> list) {
        mListView.setAdapter(new ListViewMusicAdapter(list, getActivity()));
    }

    @Override
    public void onPlaying(LocalMusicIndex music) {
        tvOnPlayTitle.setText(music.getmMusicTitle());
        tvOnPlayArtist.setText(music.getmMusicArtist());
        Date date = new Date(music.getmMusicDuration());
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String dateStr = sdf.format(date);
        tvOnPlayTime.setText(dateStr);
        musicService.setPlayingListener(this);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}