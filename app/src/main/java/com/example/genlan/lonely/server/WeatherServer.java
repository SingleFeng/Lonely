package com.example.genlan.lonely.server;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

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
public class WeatherServer extends Service implements BaiduWeatherApi.OnConnectionSuccessListener{

    BaiduWeatherApi mWeather;
    ConfigSettings mConfig = ConfigSettings.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(getClass(),"---------------ServiceOnCreate---------------");
        mWeather = new BaiduWeatherApi(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.d(getClass(),"---------------ServiceOnBind---------------");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d(getClass(),"---------------ServiceOnStartCommand---------------");
        mWeather.setOnConnectionListener(this);
        String s = mConfig.getParameter(Config.HISTORY_CITY);
        mWeather.getWeather(s);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(getClass(),"---------------ServiceOnDestroy---------------");
        stopForeground(true);
        Intent intent = new Intent("com.example.genlan.lonely.restart");
        sendBroadcast(intent);
    }

    @Override
    public void onSuccess(WeatherBean data) {
        PendingIntent pendingintent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentTitle(mConfig.getParameter(Config.HISTORY_CITY))
                .setContentText("实时气温："+data.getNow().getTmp()+"℃")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pendingintent)
                .build();
        startForeground(0x111, notification);
    }

    @Override
    public void onFailed(String error) {

    }


}
