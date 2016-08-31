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
import android.widget.ImageButton;

import com.example.genlan.lonely.R;
import com.example.genlan.lonely.adapter.ListViewMusicAdapter;
import com.example.genlan.lonely.data.LocalMusicIndex;
import com.example.genlan.lonely.data.LocalMusicTask;
import com.example.genlan.lonely.server.MusicService;
import com.example.genlan.lonely.server.WeatherService;
import com.example.genlan.lonely.util.LogUtil;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by GenLan on 2016/8/26.
 */
public class SecondFragment extends Fragment implements View.OnClickListener, LocalMusicTask.onReadMusicListener {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private PullToRefreshListView mListView;
    private ServiceConnection mServiceConn;
    private List<LocalMusicIndex> mList;
    private ImageButton btnLast, btnNext, btnPlay, btnSearch;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;

    public SecondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecondFragment newInstance(String param1, String param2) {
        SecondFragment fragment = new SecondFragment();
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
        mListView = (PullToRefreshListView) view.findViewById(R.id.lv_music);
        mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                LogUtil.v("Show_V", "onServiceDisconnected");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                LogUtil.v("Show_V", "onServiceConnected");
            }
        };
        return view;
    }

    private void initButton(View v) {
        btnLast = (ImageButton) v.findViewById(R.id.btn_music_last);
        btnNext = (ImageButton) v.findViewById(R.id.btn_music_next);
        btnPlay = (ImageButton) v.findViewById(R.id.btn_music_play);
        btnSearch = (ImageButton) v.findViewById(R.id.btn_music_read_in_sdcard);
        btnSearch.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
    }


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
            mListView.setAdapter(new ListViewMusicAdapter(DataSupport.findAll(LocalMusicIndex.class), getActivity()));
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
                break;
            case R.id.btn_music_next:
                break;
            case R.id.btn_music_play:
                playMusic();
                break;
            case R.id.btn_music_read_in_sdcard:
                setListView();
                break;
            default:
                break;
        }
    }

    private void playMusic(){
        LogUtil.d(getClass(),"---------------playMusic---------------");
        Intent intent = new Intent(getActivity(), MusicService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent,mServiceConn, Service.BIND_AUTO_CREATE);
    }

    @Override
    public void onSuccess(List<LocalMusicIndex> list) {
        mListView.setAdapter(new ListViewMusicAdapter(list, getActivity()));
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}