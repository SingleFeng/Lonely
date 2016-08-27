package com.example.genlan.lonely.util;

import android.app.Activity;
import android.graphics.Point;

/**
 * Created by GenLan on 2016/8/26.
 * 获取屏幕参数的工具类
 */
public class ScreenUtil {


    private Activity activity;
    private Point mPoint;

    public ScreenUtil(Activity activity) {
        this.activity = activity;
        mPoint = new Point();
    }

    public int getScreenWidth() {
        activity.getWindowManager().getDefaultDisplay().getSize(mPoint);
        return mPoint.x;
    }

    public int getScreenHeigth() {
        activity.getWindowManager().getDefaultDisplay().getSize(mPoint);
        return mPoint.y;
    }
}
