package com.example.genlan.lonely.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.genlan.lonely.util.LogUtil;

/**
 * Created by GenLan on 2016/8/29.
 *
 */
public class RestartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.example.genlan.lonely.restart")) {
            //TODO
            //在这里写重新启动service的相关操作
            LogUtil.d(getClass(),"Activity被重新创建？   ");
            startUploadService(context);
        }
    }

    void startUploadService(Context context){
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        context.startActivity(intent);
    }
}
