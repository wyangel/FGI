package cn.insightsresearch.fgi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import cn.insightsresearch.fgi.fragment.HomeFragment;
import cn.insightsresearch.fgi.fragment.PaperFragment;
import cn.insightsresearch.fgi.fragment.RListFragment;
import cn.insightsresearch.fgi.fragment.WchatFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static String TAG = MainActivity.class.getName();
    private long exitTime = 0;
    private FragmentManager fragmentManager;
    private Fragment homeFragment;
    private Fragment listFragment;
    private Fragment list1Fragment;
    private Fragment wchatFragment;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        textView = (TextView) findViewById(R.id.toolbar_title);
        fragmentManager = getSupportFragmentManager();
        setTabSection(0);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            LoginOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            setTabSection(0);
        } else if (id == R.id.nav_gallery) {
            setTabSection(1);
        } else if (id == R.id.nav_send) {
            setTabSection(2);
        } else if (id == R.id.nav_slideshow) {
            setTabSection(3);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(MainActivity.this, HelpActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setTabSection(int which){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (which){
            case 1:
                textView.setText(R.string.list_wenjuan);
                if (listFragment == null) {
                    listFragment = new PaperFragment().newInstance("问卷");
                    transaction.add(R.id.content, listFragment);
                } else {
                    transaction.show(listFragment);
                }
                break;
            case 2:
                textView.setText(R.string.list_jieguo);
                if (list1Fragment == null) {
                    list1Fragment = new RListFragment().newInstance("结果");
                    transaction.add(R.id.content, list1Fragment);
                } else {
                    transaction.show(list1Fragment);
                }
                break;
            case 3:
                textView.setText(R.string.list_gongzhonghao);
                if (wchatFragment == null) {
                    wchatFragment = new WchatFragment();
                    transaction.add(R.id.content, wchatFragment);
                } else {
                    transaction.show(wchatFragment);
                }
                break;
            case 0:
            default:
                textView.setText(R.string.list_index);
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.content, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }
                break;
        }
        transaction.commit();

    }

    private void hideFragments(FragmentTransaction transaction){
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (listFragment != null) {
            transaction.hide(listFragment);
        }
        if (list1Fragment != null) {
            transaction.hide(list1Fragment);
        }
        if (wchatFragment != null) {
            transaction.hide(wchatFragment);
        }
    }

    public void LoginOut() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("提示：").setMessage("请问您确认要退出登陆吗？\n重新登陆需要连接上网络！").setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setNegativeButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences sp = getApplication().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove("USER_ID");
                //editor.remove("AUTO_ISCHECK");
                editor.apply();
                MainActivity.this.finish();
            }
        }).create().show();
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            this.finish();
            System.exit(0);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            exit();
            //super.onBackPressed();
        }
    }
}
