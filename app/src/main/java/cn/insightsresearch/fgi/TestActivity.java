package cn.insightsresearch.fgi;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import cn.insightsresearch.fgi.fragment.FragmentIndicator;

public class TestActivity extends AppCompatActivity {
    private long exitTime = 0;
    public  static Fragment[] mFragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shouye);
        setFragmentIndicator(0);
    }

    private void setFragmentIndicator(int which){
        mFragments = new Fragment[3];
        mFragments[0] = getSupportFragmentManager().findFragmentById(R.id.fragment_home);
        mFragments[1] = getSupportFragmentManager().findFragmentById(R.id.fragment_search);
        mFragments[2] = getSupportFragmentManager().findFragmentById(R.id.fragment_settings);
        getSupportFragmentManager().beginTransaction().hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]).show(mFragments[which]).commit();

        FragmentIndicator mIndicator = (FragmentIndicator)findViewById(R.id.indicator);
        FragmentIndicator.setIndicator(which);
        mIndicator.setOnIndicateListener(new FragmentIndicator.OnIndicateListener() {
            @Override
            public void onIndicate(View v, int which) {
                getSupportFragmentManager().beginTransaction().hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]).show(mFragments[which]).commit();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
}
