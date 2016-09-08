package cn.insightsresearch;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by Administrator on 2016/8/17.
 */
public class MyApplication extends Application{
    private static String TAG = MyApplication.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Show App Timeline"," - - "+TAG+" - -");
    }
}
