package cn.insightsresearch.fgi;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {
    private static String TAG = SplashActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.i("Show App Timeline"," - - "+TAG+" - -");
        startMainAvtivity();
    }

    public void startMainAvtivity() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();//结束本Activity
            }
        }, 3000);//设置执行时间
    }
}