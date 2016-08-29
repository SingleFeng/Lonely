package com.example.genlan.lonely.connection;

import android.content.Context;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;
import com.example.genlan.lonely.config.Config;
import com.example.genlan.lonely.config.ConfigSettings;
import com.example.genlan.lonely.data.WeatherBean;
import com.example.genlan.lonely.util.LogUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by GenLan on 2016/8/26.
 */
public class BaiduWeatherApi {

    private static final String SEARCH_SUCCESS = "ok";
    ConfigSettings mConfig = ConfigSettings.getInstance();
    OnConnectionSuccessListener listener;

    private Context mContext;

    public BaiduWeatherApi(Context context) {
        this.mContext = context;
    }

    public void setOnConnectionListener( OnConnectionSuccessListener listener){
        this.listener = listener;
    }

    public void getWeather(String city) {
        final Gson gson = new GsonBuilder().create();
        Parameters param = new Parameters();
        param.put("city", city);
        ApiStoreSDK.execute(Config.WEATHER_PATH, ApiStoreSDK.GET, param, new ApiCallBack() {
            @Override
            public void onSuccess(int i, String s) {
                super.onSuccess(i, s);
                String json = "{" + s.substring(32, s.length() - 3);
                WeatherBean data = gson.fromJson(json, WeatherBean.class);
                String result = "";
                if (data.getStatus().equals(SEARCH_SUCCESS)) {
                    save(data);
                } else {
                    result = data.getStatus();
                    if (listener != null)
                        listener.onFailed(result);
                }
                LogUtil.d(result);
            }

            @Override
            public void onError(int i, String s, Exception e) {
                super.onError(i, s, e);
                LogUtil.e(getStackTrace(e));
            }
        });

    }

    private static String getStackTrace(Throwable e) {
        if (e == null) {
            return "";
        }
        StringBuilder str = new StringBuilder();
        str.append(e.getMessage()).append("\n");
        for (int i = 0; i < e.getStackTrace().length; i++) {
            str.append(e.getStackTrace()[i]).append("\n");
        }
        return str.toString();
    }

    private void save(WeatherBean data) {
        String temp_cnty = data.getBasic().getCnty();
        String temp_city = data.getBasic().getCity();
        String temp_loc = data.getBasic().getUpdate().getLoc();
        String temp_tmp_min = data.getDaily_forecast().get(0).getTmp().getMin();
        String temp_tmp_max = data.getDaily_forecast().get(0).getTmp().getMax();
        String temp_cond = data.getNow().getCond().getTxt();
        String temp_api = data.getAqi().getCity().getQlty();
        mConfig.setParameter(Config.HISTORY_CNTY, temp_cnty);
        mConfig.setParameter(Config.HISTORY_CITY, temp_city);
        mConfig.setParameter(Config.HISTORY_LOC, temp_loc);
        mConfig.setParameter(Config.HISTORY_TMP_MIN, temp_tmp_min);
        mConfig.setParameter(Config.HISTORY_TMP_MAX, temp_tmp_max);
        mConfig.setParameter(Config.HISTORY_COND, temp_cond);
        mConfig.setParameter(Config.HISTORY_API, temp_api);
        if (listener != null) {
            listener.onSuccess(data);
        }

    }

    public interface OnConnectionSuccessListener {
        void onSuccess(WeatherBean data);

        void onFailed(String error);
    }

}
