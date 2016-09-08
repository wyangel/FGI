package cn.insightsresearch.fgi;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.insightsresearch.fgi.fragment.HomeFragment;
import cn.insightsresearch.fgi.fragment.PaperFragment;

public class ShouYeActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = ShouYeActivity.class.getName();
    private long exitTime = 0;
    private FragmentManager fragmentManager;
    private View messagelayout;
    private View contactlayout;
    private View newslayout;
    private ImageView messageImg;
    private ImageView contactImg;
    private ImageView newsImg;
    private TextView messageText;
    private TextView contactText;
    private TextView newsText;
    private Fragment messageFragment;
    private Fragment contactFragment;
    private Fragment newsFragment;
    private int uid=0;
    private int total=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shouye);
        initView();
        fragmentManager = getSupportFragmentManager();
        setTabSection(0);

        Log.i(TAG, "--------onCreate ShouYeActivity");
    }

    private void initView(){
        messagelayout = findViewById(R.id.message_layout);
        contactlayout = findViewById(R.id.contacts_layout);
        newslayout = findViewById(R.id.news_layout);
        messageImg = (ImageView)findViewById(R.id.message_image);
        contactImg = (ImageView)findViewById(R.id.contacts_image);
        newsImg = (ImageView)findViewById(R.id.news_image);
        messageText = (TextView)findViewById(R.id.message_text);
        contactText =  (TextView)findViewById(R.id.contacts_text);
        newsText = (TextView)findViewById(R.id.news_text);
        messagelayout.setOnClickListener(this);
        contactlayout.setOnClickListener(this);
        newslayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.message_layout:
                setTabSection(0);
                break;
            case R.id.contacts_layout:
                setTabSection(1);
                break;
            case R.id.news_layout:
                setTabSection(2);
                break;
            default:
                break;
        }
    }

    private void setTabSection(int which){
        clearSection();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (which){
            case 0:
                // 当点击了消息tab时，改变控件的图片和文字颜色
                messageImg.setImageResource(R.drawable.ic_home_focus);
                messageText.setTextColor(Color.WHITE);
                messagelayout.setBackgroundColor(Color.parseColor("#333333"));
                if (messageFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    messageFragment = new HomeFragment();
                    transaction.add(R.id.content, messageFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(messageFragment);
                }
                break;
            case 1:
                // 当点击了消息tab时，改变控件的图片和文字颜色
                contactImg.setImageResource(R.drawable.ic_search_normal);
                contactText.setTextColor(Color.WHITE);
                contactlayout.setBackgroundColor(Color.parseColor("#333333"));
                if (contactFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    contactFragment = new PaperFragment().newInstance("问卷");
                    transaction.add(R.id.content, contactFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(contactFragment);
                }
                break;
            case 2:
            default:
                // 当点击了消息tab时，改变控件的图片和文字颜色
                newsImg.setImageResource(R.drawable.ic_setting_focus);
                newsText.setTextColor(Color.WHITE);
                newslayout.setBackgroundColor(Color.parseColor("#333333"));
                if (newsFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    newsFragment = new PaperFragment().newInstance("结果");
                    transaction.add(R.id.content, newsFragment);

                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(newsFragment);
                }
                break;

        }
        transaction.commit();

    }

    private void clearSection(){
        messageImg.setImageResource(R.drawable.ic_home_normal);
        messageText.setTextColor(Color.parseColor("#82858b"));
        messagelayout.setBackgroundColor(Color.parseColor("#000000"));
        contactImg.setImageResource(R.drawable.ic_search_normal);
        contactText.setTextColor(Color.parseColor("#82858b"));
        contactlayout.setBackgroundColor(Color.parseColor("#000000"));
        newsImg.setImageResource(R.drawable.ic_setting_normal);
        newsText.setTextColor(Color.parseColor("#82858b"));
        newslayout.setBackgroundColor(Color.parseColor("#000000"));
    }

    private void hideFragments(FragmentTransaction transaction){
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (contactFragment != null) {
            transaction.hide(contactFragment);
        }
        if (newsFragment != null) {
            transaction.hide(newsFragment);
        }
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
