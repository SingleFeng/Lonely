package com.example.genlan.lonely.base;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import com.baidu.apistore.sdk.ApiStoreSDK;
import com.example.genlan.lonely.config.Config;
import com.example.genlan.lonely.config.ConfigSettings;
import com.example.genlan.lonely.util.LogUtil;

import org.litepal.LitePalApplication;

/**
 * Created by GenLan on 2016/8/26.
 */
public class BaseApplication extends LitePalApplication {

    private static final String TAG = BaseApplication.class.getSimpleName();
    private boolean isInited = false;
    private ApplicationContext mAppContext;
    private Activity activity;
    private volatile static BaseApplication instance;
    private ConfigSettings mConfig;

    public static BaseApplication getInstance() {
        if (instance == null) {
            synchronized (BaseApplication.class) {
                if (instance == null) {
                    instance = new BaseApplication();
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate() {
        LogUtil.d(TAG, "---------------OnCreate---------------");
        instance = this;
        initApplication();
        super.onCreate();
    }

    private void initApplication() {
        if (isInited) {
            return;
        }
        long begin = System.currentTimeMillis();
        ApiStoreSDK.init(this, Config.WEATHER_KEY);
        mConfig = ConfigSettings.getInstance(this);
        isInited = true;
        LogUtil.e(TAG, String.format("启动时间: %d(ms)", (System.currentTimeMillis() - begin)));
    }

    /**
     * 启用系统底部导航栏
     */
    public void enableNavigationBar() {
        Intent intent = new Intent("android.intent.action.NAVIGATION_ENABLE");
        sendBroadcast(intent);
    }

    /**
     * 禁用系统底部的导航栏
     */
    public void disableNavigationBar() {
        Intent intent = new Intent("android.intent.action.NAVIGATION_DISABLE");
        sendBroadcast(intent);
    }

    /**
     * 获取应用上下文对象
     *
     * @return 应用上下文对象
     */
    public ApplicationContext getAppContext() {
        return mAppContext;
    }
}

