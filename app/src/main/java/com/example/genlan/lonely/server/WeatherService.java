package com.example.genlan.lonely.server;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.genlan.lonely.R;
import com.example.genlan.lonely.activity.MainActivity;
import com.example.genlan.lonely.config.Config;
import com.example.genlan.lonely.config.ConfigSettings;
import com.example.genlan.lonely.connection.BaiduWeatherApi;
import com.example.genlan.lonely.data.WeatherBean;
import com.example.genlan.lonely.util.LogUtil;

/**
 * Created by GenLan on 2016/8/29.
 */
public class WeatherService extends Service implements BaiduWeatherApi.OnConnectionSuccessListener {

    private onServiceListener listener;
    BaiduWeatherApi mWeather;
    ConfigSettings mConfig = ConfigSettings.getInstance();
    private static final int SECOND = 1000;
    private boolean runFlag = false;

    public void setServiceListener(onServiceListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(getClass(), "---------------ServiceOnCreate---------------");
        mWeather = new BaiduWeatherApi(this);
        if (listener != null)
            listener.onSerCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.d(getClass(), "---------------ServiceOnBind---------------");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d(getClass(), "---------------ServiceOnStartCommand---------------");
        if (!runFlag) {
            runFlag = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long startTime = System.currentTimeMillis();
                    while (runFlag) {
                        long endTime = System.currentTimeMillis();
                        if (endTime - startTime > SECOND * 60 * 60) {
                            startTime = endTime;
                            mWeather.setOnConnectionListener(WeatherService.this);
                            String s = mConfig.getParameter(Config.HISTORY_CITY);
                            mWeather.getWeather(s);
                            LogUtil.d(this.getClass(),"---------------onRefresh---------------");
                        }
                    }
                }
            }).start();
        } else {
            runFlag = false;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(getClass(), "---------------ServiceOnDestroy---------------");
        stopForeground(true);
        Intent intent = new Intent("com.example.genlan.lonely.restart");
        sendBroadcast(intent);
        if (listener != null)
            listener.onSerDestroy();
    }

    @Override
    public void onSuccess(WeatherBean data) {
        PendingIntent pendingintent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentTitle(mConfig.getParameter(Config.HISTORY_CITY))
                .setContentText("实时气温：" + data.getNow().getTmp() + "℃")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pendingintent)
                .setDeleteIntent(null)
                .build();
        startForeground(0x111, notification);
    }

    @Override
    public void onFailed(String error) {

    }

    public interface onServiceListener {
        void onSerCreate();

        void onSerDestroy();
    }

}
