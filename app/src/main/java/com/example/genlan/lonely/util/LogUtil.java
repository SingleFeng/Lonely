package com.example.genlan.lonely.util;

import android.util.Log;

/**
 * Created by GenLan on 2016/8/26.
 */
public class LogUtil {


    private static boolean isDebug = true;

    private static final String TAG = "日志";

    private LogUtil() {
    }

    public static void setDebug(boolean isDebug) {
        LogUtil.isDebug = isDebug;
    }

    public static void d(Class<?> clazz, String msg) {
        if (isDebug) {
            Log.d("[" + clazz.getSimpleName() + "]", msg + "");
        }
    }

    public static void e(Class<?> clazz, String msg) {
        if (isDebug) {
            Log.e("[" + clazz.getSimpleName() + "]", msg + "");
        }
    }

    public static void i(Class<?> clazz, String msg) {
        if (isDebug) {
            Log.i("[" + clazz.getSimpleName() + "]", msg + "");
        }
    }

    public static void w(Class<?> clazz, String msg) {
        if (isDebug) {
            Log.w("[" + clazz.getSimpleName() + "]", msg + "");
        }
    }

    public static void v(Class<?> clazz, String msg) {
        if (isDebug) {
            Log.v("[" + clazz.getSimpleName() + "]", msg + "");
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d("[" + tag + "]", msg + "");
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e("[" + tag + "]", msg + "");
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i("[" + tag + "]", msg + "");
        }
    }

    public static void w(String tag, String msg) {
        if (isDebug) {
            Log.w("[" + tag + "]", msg + "");
        }
    }

    public static void v(String tag, String msg) {
        if (isDebug) {
            Log.v("[" + tag + "]", msg + "");
        }
    }

    public static void d(String msg) {
        if (isDebug) {
            Log.d("[" + TAG + "]", msg + "");
        }
    }

    public static void e(String msg) {
        if (isDebug) {
            Log.e("[" + TAG + "]", msg + "");
        }
    }

    public static void i(String msg) {
        if (isDebug) {
            Log.i("[" + TAG + "]", msg + "");
        }
    }

    public static void w(String msg) {
        if (isDebug) {
            Log.w("[" + TAG + "]", msg + "");
        }
    }

    public static void v(String msg) {
        if (isDebug) {
            Log.v("[" + TAG + "]", msg + "");
        }
    }
}
