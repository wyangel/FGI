package cn.insightsresearch.fgi.util;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import cn.insightsresearch.Constants;
import cn.insightsresearch.fgi.Model.BaseEntity;
import cn.insightsresearch.fgi.Model.Paper;
import cn.insightsresearch.fgi.Model.Question;
import cn.insightsresearch.fgi.Model.QuestionEntity;
import cn.insightsresearch.fgi.Model.User;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/17.
 */
public class HttpUtil {
    private String TAG = HttpUtil.class.getName();

    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public User getUser(String username, String password) throws IOException {
        RequestBody body = new FormBody.Builder().add("mtype","fgi.user").add("username",username).add("userpwd",password).build();
        Request request = new Request.Builder()
                .url(Constants.APP_SERVER+Constants.APP_LOGIN)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String result = response.body().string();
        Log.i(TAG,"getUser:"+result);
        User user = new Gson().fromJson(result,User.class);
        return user;
    }

    public BaseEntity<ArrayList<Paper>> getPaperList(int userid) throws IOException {
        RequestBody body = new FormBody.Builder().add("mtype","fgi.paper").add("userid",userid+"").build();
        Request request = new Request.Builder()
                .url(Constants.APP_SERVER+Constants.APP_LOGIN)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String result = response.body().string();
        Log.i(TAG,"getPaperList:"+result);
        BaseEntity<ArrayList<Paper>> paperData = new Gson().fromJson(result,BaseEntity.class);
        return paperData;
    }


    public QuestionEntity getQuestionList(int paperid) throws IOException {
        RequestBody body = new FormBody.Builder().add("mtype","fgi.question").add("paperid",paperid+"").build();
        Request request = new Request.Builder()
                .url(Constants.APP_SERVER+Constants.APP_LOGIN)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String result = response.body().string();
        Log.i(TAG,"getQuestionList:"+result);
        QuestionEntity questionData = new Gson().fromJson(result,QuestionEntity.class);
        return questionData;
    }

    public String getQuestionListJson(int paperid) throws IOException {
        RequestBody body = new FormBody.Builder().add("mtype","fgi.question").add("paperid",paperid+"").build();
        Request request = new Request.Builder()
                .url(Constants.APP_SERVER+Constants.APP_LOGIN)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String result = response.body().string();
        Log.i(TAG,"getQuestionListJson:"+result);
        return result;
    }

    public User loginUser(String url, String json) throws IOException {
        //把请求的内容字符串转换为json
        //RequestBody body = RequestBody.create(JSON, json);
        RequestBody body = new FormBody.Builder().add("username","wss").add("userpwd","1231").build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String result = response.body().string();
        System.out.print(result);
        Gson gson = new Gson();
        User userData = gson.fromJson(result,User.class);
        return userData;
    }

    public String makeJson(String username, String password) {
        return "{'userName':" + username + "," + "'userPwd':" + password + "}";
    }

}