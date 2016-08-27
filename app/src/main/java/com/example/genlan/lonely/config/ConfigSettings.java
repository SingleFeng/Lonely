package com.example.genlan.lonely.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.genlan.lonely.activity.mainfragment.FragmentId;
import com.example.genlan.lonely.util.LogUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by GenLan on 2016/8/26.
 */
public class ConfigSettings {
    private static final String TAG = "<ConfigSettings>";
    public static final String APP_PARAM = "ConfigSettings";

    private SharedPreferences mPrefs;
    private static ConfigSettings instance = null;

    private ConfigSettings(Context context) {
        mPrefs = context.getSharedPreferences(APP_PARAM, Context.MODE_PRIVATE);
    }

    public static ConfigSettings getInstance(Context context) {
        if (instance == null) {
            synchronized (ConfigSettings.class) {
                if (instance == null) {
                    instance = new ConfigSettings(context);
                }
            }
        }
        return instance;
    }

    public static ConfigSettings getInstance() {
        if (instance == null) {
            synchronized (ConfigSettings.class) {
                if (instance == null) {
                    return null;
                }
            }
        }
        return instance;
    }

    private boolean initDefaultParameter() {
        Map<String, String> mapParam = new HashMap<>();
        mapParam.put(Config.MAIN_FRAGMENT_INDEX, FragmentId.FIRST_FRAGMENT);
        mapParam.put(Config.HISTORY_CNTY, "");
        mapParam.put(Config.HISTORY_CITY, "");
        mapParam.put(Config.HISTORY_LOC, "");
        mapParam.put(Config.HISTORY_TMP_MAX, "");
        mapParam.put(Config.HISTORY_TMP_MIN, "");
        mapParam.put(Config.HISTORY_COND, "");
        mapParam.put(Config.HISTORY_API, "");
        return loadDefaultParameter(mapParam);
    }

    /**
     * 生成默认参数项
     *
     * @param defParameters 默认参数列表
     * @return
     */
    private boolean loadDefaultParameter(Map<String, String> defParameters) {
        SharedPreferences.Editor editor = mPrefs.edit();
        Set<String> keys = defParameters.keySet();
        for (String key : keys) {
            String param = mPrefs.getString(key, "");
            if (TextUtils.isEmpty(param)) {
                editor.putString(key, defParameters.get(key));
            }
        }
        return editor.commit();
    }

    public void initApplicationParameter(Context context) {
        LogUtil.d(TAG, "------------------------initApplicationParameter------------------------");
        initDefaultParameter();
    }

    /**
     * 新增或修改配置字符串参数项
     *
     * @param key   参数项标识
     * @param value 参数项的值
     * @return
     */
    public boolean setParameter(String key, String value) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * @param key   参数项标识
     * @param value 参数项的值
     * @return
     */
    public boolean setParameter(String key, boolean value) {
        return setParameter(key, value ? Config.TRUE : Config.FALSE);
    }

    /**
     * 新增或修改配置整数参数项
     *
     * @param key   参数项标识
     * @param value 参数项的值
     * @return
     */
    public boolean setParameter(String key, int value) {
        return setParameter(key, Integer.toString(value));
    }

    /**
     * 获取配置参数
     *
     * @param key 参数项标识
     * @return 配置参数
     */
    public String getParameter(String key) {
        return mPrefs.getString(key, "");
    }
}
