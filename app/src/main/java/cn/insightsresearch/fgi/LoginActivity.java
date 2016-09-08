package cn.insightsresearch.fgi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;


import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import cn.insightsresearch.fgi.Api.RetroFactory;
import cn.insightsresearch.fgi.Model.User;
import cn.insightsresearch.fgi.Process.BaseTask;
import cn.insightsresearch.fgi.Process.UserProcess;
import cn.insightsresearch.fgi.util.NetworkUtil;

public class LoginActivity extends AppCompatActivity {
    private final static String TAG = LoginActivity.class.getName();
    private long exitTime = 0;
    private EditText userName,passWord;
    private CheckBox ck_auto,ck_mima;
    private String uname,upwd;
    private View mProgressView;
    private View mLoginFormView;
    private int uid =0;
    private Activity activity;

    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(!NetworkUtil.isConnected(getBaseContext())){
            Log.i(TAG,"Network error");
            Toast.makeText(LoginActivity.this,R.string.error_network,Toast.LENGTH_LONG).show();
        }
        activity = this;
        sp = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        ck_mima =(CheckBox)findViewById(R.id.cb_mima);
        ck_auto = (CheckBox)findViewById(R.id.cb_auto) ;

        // Set up the login form.
        userName = (EditText) findViewById(R.id.email);
        passWord = (EditText) findViewById(R.id.password);

        if(sp.getBoolean("MMCHECK",false)){
            ck_mima.setChecked(true);
            userName.setText(sp.getString("USER_NAME",""));
            passWord.setText(sp.getString("PASSWORD",""));

            if(sp.getBoolean("AUTO_ISCHECK",false)){
                ck_auto.setChecked(true);
                if(sp.getInt("USER_ID",0)>0){
                    Log.i(TAG,"AutoLogin in");
                    loginIn();
                }
            }
        }

        passWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button SignInButton = (Button) findViewById(R.id.email_sign_in_button);
        SignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        ck_mima.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(ck_mima.isChecked()){
                    sp.edit().putBoolean("MMCHECK",true).apply();
                }else{
                    sp.edit().putBoolean("MMCHECK",false).apply();
                }
                Log.i(TAG,"MMCHECK "+sp.getBoolean("MMCHECK",false));
            }
        });

        ck_auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(ck_auto.isChecked()){
                    sp.edit().putBoolean("AUTO_ISCHECK",true).apply();
                }else{
                    sp.edit().putBoolean("AUTO_ISCHECK",false).apply();
                }
                Log.i(TAG,"AUTO_ISCHECK "+sp.getBoolean("AUTO_ISCHECK",false));
            }
        });
    }

    private void attemptLogin() {
        userName.setError(null);
        passWord.setError(null);
        uname = userName.getText().toString();
        upwd = passWord.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(upwd) || !isLengthOk(upwd)) {
            passWord.setError(getString(R.string.error_invalid_password));
            focusView = passWord;
            cancel = true;
        }

        if (TextUtils.isEmpty(uname)) {
            userName.setError(getString(R.string.error_field_required));
            focusView = userName;
            cancel = true;
        } else if (!isLengthOk(uname)) {
            userName.setError(getString(R.string.error_invalid_email));
            focusView = userName;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    UserProcess userProcess = new UserProcess(getBaseContext(),activity);

                    Map<String, String> map = new HashMap<String, String>();
                    map.put("mtype", "fgi.user");
                    map.put("username",uname);
                    map.put("userpwd", upwd);
                    //userProcess.getUser(map);
                    //userProcess.getUsersId(uname,upwd);

                    new BaseTask<User>(LoginActivity.this, RetroFactory.getApiService().getUser(map)).handleResponse(new BaseTask.ResponseListener<User>() {
                                @Override
                                public void onSuccess(User user) {
                                    uid = user.getId();
                                    if(uid>0){
                                        Log.i(TAG,"Login in"+uid);
                                        if(ck_mima.isChecked()){
                                            SharedPreferences.Editor editor = sp.edit();
                                            editor.putInt("USER_ID",uid);
                                            editor.putString("USER_NAME",uname);
                                            editor.putString("PASSWORD",upwd);
                                            editor.apply();
                                        }
                                        loginIn();
                                    }else{
                                        Log.i(TAG,"Login error");
                                        Toast.makeText(LoginActivity.this, user.getUsername(), Toast.LENGTH_SHORT).show();
                                    }
                                    showProgress(false);
                                }

                                @Override
                                public void onFail() {
                                    showProgress(false);
                                }
                    });
                }
            }).start();
        }
    }

    private boolean isLengthOk(String str) {
        return str.length()>2;
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
            Toast.makeText(getApplicationContext(), R.string.rloginout,
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    public void loginIn(){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}