package com.example.genlan.lonely.activity.mainfragment;

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
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.genlan.lonely.R;
import com.example.genlan.lonely.adapter.ListViewMusicAdapter;
import com.example.genlan.lonely.config.Config;
import com.example.genlan.lonely.config.ConfigSettings;
import com.example.genlan.lonely.data.LocalMusicIndex;
import com.example.genlan.lonely.data.LocalMusicTask;
import com.example.genlan.lonely.server.MusicService;
import com.example.genlan.lonely.util.LogUtil;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by GenLan on 2016/8/26.
 * Music
 */
public class MusicFragment extends Fragment implements View.OnClickListener, LocalMusicTask.onReadMusicListener, MusicService.onPlayingListener {
    private PullToRefreshListView mListView;
    private ImageButton btnLast, btnNext, btnPlay, btnSearch, btnStop;
    private TextView tvOnPlayTitle, tvOnPlayArtist, tvOnPlayTime;
    private SeekBar sbr;

    private MusicService musicService;
    private MusicService.MyBind myBind;
    private ServiceConnection serviceConnection;
//    private ConfigSettings mConfig;

    private static boolean isPlaying = false;
    private static boolean isRegistered =false;
    private static int mMusicIndex = 0;
    private static int mMusicCount = 0;

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
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_music, container, false);
        initButton(view);
//        mConfig = ConfigSettings.getInstance();
        musicService = new MusicService();
        mListView = (PullToRefreshListView) view.findViewById(R.id.lv_music);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                stopMusicService();
                mMusicIndex = position - 1;
                LogUtil.d(getClass(),"mMusicIndex = "+ mMusicIndex);
                playMusic(mMusicIndex);
//                mConfig.setParameter(Config.MUSIC_INDEX,position - 1);
//                playMusic(mConfig.getIntParameter(Config.MUSIC_INDEX));
            }
        });
        initServiceConnection();
        return view;
    }

    /**
     * 控件初始化
     */
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
//                mConfig.setParameter(Config.MUSIC_COUNT,list.size());
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

    /**
     * View点击事件回调
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //// TODO: 2016/8/30 点击事件
            case R.id.btn_music_last:

//                if (mConfig.getBooleanParameter(Config.MUSIC_IS_PLAYING)) stopMusicService();
//                if (mConfig.getIntParameter(Config.MUSIC_INDEX) - 1 >= 0) {
//                    mConfig.setParameter(Config.MUSIC_INDEX,mConfig.getIntParameter(Config.MUSIC_INDEX) - 1);
//                } else {
//                    mConfig.setParameter(Config.MUSIC_INDEX,mConfig.getIntParameter(Config.MUSIC_COUNT) - 1);
//                }
//                playMusic(mConfig.getIntParameter(Config.MUSIC_INDEX));
                if (isPlaying) stopMusicService();
                if (mMusicIndex - 1 >= 0){
                    mMusicIndex = mMusicIndex - 1;
                }else {
                    mMusicIndex = mMusicCount - 1;
                }
                playMusic(mMusicIndex);
                break;
            case R.id.btn_music_next:
//                if (mConfig.getBooleanParameter(Config.MUSIC_IS_PLAYING)) stopMusicService();
//                if (mConfig.getIntParameter(Config.MUSIC_INDEX) + 1 <= mConfig.getIntParameter(Config.MUSIC_COUNT) - 1) {
//                    mConfig.setParameter(Config.MUSIC_INDEX,mConfig.getIntParameter(Config.MUSIC_INDEX) + 1);
//                    playMusic(mConfig.getIntParameter(Config.MUSIC_INDEX));
//                } else {
//                    playMusic(0);
//                }

                if (isPlaying) stopMusicService();
                if (mMusicIndex + 1 <= mMusicCount - 1){
                    mMusicIndex = mMusicIndex + 1;
                    playMusic(mMusicIndex);
                }else playMusic(mMusicIndex = 0);
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
        if (isRegistered) {
            Intent intent = new Intent(getActivity(), MusicService.class);
            sbr.setProgress(0);
            getActivity().unbindService(serviceConnection);
            getActivity().stopService(intent);
            isRegistered = false;
            isPlaying = false;
//            mConfig.setParameter(Config.MUSIC_IS_REGISTERED,false);
        }
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
            intent.putExtra(Config.MUSIC_IS_PLAYING, false);
            intent.putExtra(Config.MUSIC_INDEX,0);
            isPlaying = true;
            btnPlay.setImageResource(R.drawable.icon_music_pause);
            getActivity().startService(intent);
            musicService.setPlayingListener(this);
            getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            LogUtil.d(getClass(), "---------------playMusic_False---------------");
            intent.putExtra(Config.MUSIC_IS_PLAYING, true);
            isPlaying =false;
            btnPlay.setImageResource(R.drawable.icon_music_play);
            getActivity().startService(intent);
            musicService.setPlayingListener(this);
            getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
        isRegistered = true;

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
//        mConfig.setParameter(Config.MUSIC_IS_PLAYING,true);
        isRegistered = true;
        btnPlay.setImageResource(R.drawable.icon_music_pause);
        getActivity().startService(intent);
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        musicService.setPlayingListener(this);
//        mConfig.setParameter(Config.MUSIC_IS_REGISTERED,true);
    }

    /**
     * 初始化BindServer启动方式的连接
     */
    private void initServiceConnection() {
        if (serviceConnection == null) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    myBind = (MusicService.MyBind) service;
                    //设置进度条的最大长度
                    int max = myBind.getMusicDuration();
//                    pb.setMax(max);
                    sbr.setMax(max);
                    sbr.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if (fromUser)
                            myBind.seekTo(progress);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });
                    new Thread() {
                        public void run() {
                            //改变当前进度条的值
                            //设置当前进度
                            while (true) {
                                try {
                                    Thread.sleep(100);
                                    sbr.setProgress(isPlaying ? myBind.getMusicCurrentPosition() : 0);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
//                                pb.setProgress(myBind.getMusicCurrentPosition());
//                                sbr.setProgress(myBind.getMusicCurrentPosition());
//                                sbr.setProgress(mConfig.getBooleanParameter(Config.MUSIC_IS_REGISTERED)?myBind.getMusicCurrentPosition():0);
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

    /**
     * LocalMusicTask异步查询结果回调
     */
    @Override
    public void onSuccess(List<LocalMusicIndex> list) {
        mListView.setAdapter(new ListViewMusicAdapter(list, getActivity()));
    }

    /**
     * MusicService音乐播放状态回调
     */
    @Override
    public void onPlaying(LocalMusicIndex music) {
        tvOnPlayTitle.setText(music.getmMusicTitle());
        tvOnPlayArtist.setText(music.getmMusicArtist());
        Date date = new Date(music.getmMusicDuration());
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String dateStr = sdf.format(date);
//        String dateStr = SimpleDateFormat.getTimeInstance().format("mm:ss");
        tvOnPlayTime.setText(dateStr);
        musicService.setPlayingListener(this);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}