package cn.insightsresearch.fgi.Process;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.insightsresearch.fgi.Api.RetroFactory;
import cn.insightsresearch.fgi.LoginActivity;
import cn.insightsresearch.fgi.Model.BaseEntity;
import cn.insightsresearch.fgi.Model.Paper;
import cn.insightsresearch.fgi.Model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016/8/30.
 */
public class UserProcess {
    private static String TAG = UserProcess.class.getName();
    private Context context;
    private Activity activity;
    private int uid = 0;

    public UserProcess(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    public void getBaseEntityPaperList(String uid) {
        new BaseTask<ArrayList<Paper>>(context, RetroFactory.getApiService().getPaperList("fgi.paper",uid)).handleBaseEntityResponse(new BaseTask.ResponseListener<ArrayList<Paper>>() {
            @Override
            public void onSuccess(ArrayList<Paper> list) {
                for(Paper p : list){
                    Log.d(TAG, "onSuccess Paper: "+p.toString());
                }
                Log.d(TAG, "onSuccess <Paper>: "+list.size());
            }

            @Override
            public void onFail() {

            }
        });
    }

    public void getPaperList(String uid) {
        new BaseTask<BaseEntity<ArrayList<Paper>>>(context, RetroFactory.getApiService().getPaperList("fgi.paper",uid)).handleResponse(new BaseTask.ResponseListener<BaseEntity<ArrayList<Paper>>>() {
            @Override
            public void onSuccess(BaseEntity<ArrayList<Paper>> paperBaseEntity) {
                ArrayList<Paper> list  = paperBaseEntity.getList();
                for(Paper p : list){
                    Log.d(TAG, "onSuccess Paper: "+p.toString());
                }
                Log.d(TAG, "onSuccess BaseEntity<Paper>: "+paperBaseEntity.getTotal());
            }

            @Override
            public void onFail() {

            }
        });
    }

    public void getUser(Map<String,String> map){
        map.put("mtype", "fgi.user");
        RetroFactory.getApiService().getUser(map).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                uid = response.body().getId();
                Log.d(TAG, response.body().toString());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }

    public void getUsersId(String uname,String upwd) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("mtype", "fgi.user");
        map.put("username",uname);
        map.put("userpwd", upwd);

        new BaseTask<User>(context, RetroFactory.getApiService().getUser(map)).handleResponse(new BaseTask.ResponseListener<User>() {
            @Override
            public void onSuccess(User user) {
                uid = user.getId();
                Log.d(TAG, user.toString());
            }

            @Override
            public void onFail() {
            }
        });
    }

    public void getUsersIdRj(String uname,String upwd) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("mtype", "fgi.user");
        map.put("username",uname);
        map.put("userpwd", upwd);

        new BaseTask<User>(context, RetroFactory.getApiServiceRj().getUserRj(map)).handleResponseRj(new BaseTask.ResponseListener<User>() {
            @Override
            public void onSuccess(User user) {
                int uid = user.getId();
                Log.d(TAG, "BaseTask<User> : "+user.toString());
            }

            @Override
            public void onFail() {

            }
        });
    }
}
