package com.example.genlan.lonely.activity.mainfragment;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.genlan.lonely.R;
import com.example.genlan.lonely.config.Config;
import com.example.genlan.lonely.config.ConfigSettings;
import com.example.genlan.lonely.connection.BaiduWeatherApi;
import com.example.genlan.lonely.data.WeatherBean;
import com.example.genlan.lonely.server.WeatherServer;
import com.example.genlan.lonely.util.LogUtil;
import com.example.genlan.lonely.util.ScreenUtil;

/**
 * Created by GenLan on 2016/8/26.
 */
public class WeatherFragment extends Fragment implements View.OnClickListener, BaiduWeatherApi.OnConnectionSuccessListener,WeatherServer.onServiceListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Handler mHandler;
    private String mParam1;
    private String mParam2;
    ConfigSettings mConfig;
    ScreenUtil mScreen;
    private int mScreenX = 0;
    private int mScreenY = 0;
    private LinearLayout llCity;
    private TextView tvCity, tvApi, tvCond, tvTmpMin, tvTmpMax, tvUpdate;
    private ImageView ivLocation, ivRefresh,ivWeatherAbout;
    private BaiduWeatherApi mWeather;
    private ServiceConnection mServiceConn;
    private static boolean mIsServiceBind = false;
    private WeatherServer mService;

    private OnFragmentInteractionListener mListener;

    public WeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeatherFragment.
     */
    public static WeatherFragment newInstance(String param1, String param2) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /*
     * Fragment的生命周期
     * onAttach -> onCreate -> onCreateView -> onActivityCreated -> onStart -> onResume
     * Running...
     * onPause -> onStop -> onDestroyView -> onDestroy -> onDetach
     * */

    @Override
    public void onAttach(Context context) {
        LogUtil.d(getClass(),"---------------onAttach---------------");
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(WeatherFragment.class, "---------------OnCreate---------------");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getScreenParam();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtil.d(getClass(),"---------------onCreateView---------------");
        final View view = inflater.inflate(R.layout.fragment_main_first, container, false);
        initView(view);
        setClickListener();
        mService = new WeatherServer();
        mWeather = new BaiduWeatherApi(getActivity());
        mWeather.setOnConnectionListener(this);
        mService.setServiceListener(this);
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
        initService();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        LogUtil.d(getClass(),"---------------onActivityCreated---------------");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        LogUtil.d(getClass(),"---------------onStart---------------");
        super.onStart();
        getWeatherParam();
    }

    @Override
    public void onResume() {
        LogUtil.d(getClass(),"---------------onResume---------------");
        super.onResume();
    }

    @Override
    public void onPause() {
        LogUtil.d(getClass(),"---------------onPause---------------");
        super.onPause();
    }

    @Override
    public void onStop() {
        LogUtil.d(getClass(),"---------------onStop---------------");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        LogUtil.d(getClass(),"---------------onAttach---------------");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        LogUtil.d(getClass(),"---------------onAttach---------------");
        super.onDestroy();
        getActivity().unbindService(mServiceConn);
    }


    @Override
    public void onDetach() {
        LogUtil.d(getClass(),"---------------onAttach---------------");
        super.onDetach();
        mListener = null;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void initService(){
        LogUtil.d(getClass(),"---------------onStartService---------------");
        Intent intent = new Intent(getActivity(), WeatherServer.class);
        mIsServiceBind = true;
        getActivity().startService(intent);
        getActivity().bindService(intent,mServiceConn, Service.BIND_AUTO_CREATE);
    }

    private void initView(View view) {
        llCity = (LinearLayout) view.findViewById(R.id.ll_change_city_layout);
        tvCity = (TextView) view.findViewById(R.id.tv_weather_city);
        tvApi = (TextView) view.findViewById(R.id.tv_weather_api);
        tvCond = (TextView) view.findViewById(R.id.tv_weather_cond);
        tvTmpMin = (TextView) view.findViewById(R.id.tv_weather_tmp_min);
        tvTmpMax = (TextView) view.findViewById(R.id.tv_weather_tmp_max);
        tvUpdate = (TextView) view.findViewById(R.id.tv_weather_update_time);
        ivLocation = (ImageView) view.findViewById(R.id.iv_weather_location);
        ivRefresh = (ImageView) view.findViewById(R.id.iv_weather_refreshing);
        ivWeatherAbout = (ImageView) view.findViewById(R.id.iv_weather_about);
    }

    private void setClickListener() {
        llCity.setOnClickListener(this);
        ivLocation.setOnClickListener(this);
        ivRefresh.setOnClickListener(this);
        ivWeatherAbout.setOnClickListener(this);
    }

    private void getWeatherParam() {
        mConfig = ConfigSettings.getInstance(getActivity());
        if (!mConfig.getParameter(Config.HISTORY_CITY).equals("")) {
            tvCity.setText(mConfig.getParameter(Config.HISTORY_CNTY) + " " + mConfig.getParameter(Config.HISTORY_CITY));
            tvUpdate.setText(mConfig.getParameter(Config.HISTORY_LOC));
            tvTmpMin.setText(mConfig.getParameter(Config.HISTORY_TMP_MIN) + "℃");
            tvTmpMax.setText(mConfig.getParameter(Config.HISTORY_TMP_MAX) + "℃");
            tvCond.setText(mConfig.getParameter(Config.HISTORY_COND));
            tvApi.setText(mConfig.getParameter(Config.HISTORY_API));
        }
    }

    private void initRefreshWeather(){
        String s = mConfig.getParameter(Config.HISTORY_CITY);
        if (!s.equals("")){
            mWeather.getWeather(s);
        }
    }

    private void getScreenParam() {
        mScreen = new ScreenUtil(getActivity());
        mScreenX = mScreen.getScreenWidth();
        mScreenY = mScreen.getScreenHeigth();
    }


    private void setView(WeatherBean data) {
        String temp_cnty = data.getBasic().getCnty();
        String temp_city = data.getBasic().getCity();
        String temp_loc = data.getBasic().getUpdate().getLoc();
        String temp_tmp_min = data.getDaily_forecast().get(0).getTmp().getMin();
        String temp_tmp_max = data.getDaily_forecast().get(0).getTmp().getMax();
        String temp_cond = data.getNow().getCond().getTxt();
        String temp_api = data.getAqi().getCity().getQlty();
        tvCity.setText(temp_cnty + " " + temp_city);
        tvUpdate.setText(temp_loc);
        tvTmpMin.setText(temp_tmp_min + "℃");
        tvTmpMax.setText(temp_tmp_max + "℃");
        tvCond.setText(temp_cond);
        tvApi.setText(temp_api);
    }

    private void setDialog(){
        final EditText edt = new EditText(getActivity());
        new AlertDialog.Builder(getActivity()).setTitle("请输入").
                setView(edt)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mWeather.getWeather(edt.getText().toString());
                    }
                }).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_change_city_layout:
                setDialog();
                break;
            case R.id.iv_weather_location:
                //todo 点击自动定位城市
                break;
            case R.id.iv_weather_refreshing:
                //todo 点击刷新天气信息
                initRefreshWeather();
                break;
        }
    }

    @Override
    public void onSuccess(WeatherBean data) {
        setView(data);
    }

    @Override
    public void onFailed(String error) {
        Toast.makeText(getActivity(),"获取出错："+error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSerCreate() {

    }

    @Override
    public void onSerDestroy() {
        getActivity().unbindService(mServiceConn);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

