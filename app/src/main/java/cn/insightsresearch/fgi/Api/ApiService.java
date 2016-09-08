package cn.insightsresearch.fgi.Api;

import java.util.ArrayList;
import java.util.Map;

import cn.insightsresearch.Constants;
import cn.insightsresearch.fgi.Model.BaseEntity;
import cn.insightsresearch.fgi.Model.Paper;
import cn.insightsresearch.fgi.Model.Question;
import cn.insightsresearch.fgi.Model.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2016/8/30.
 */
public interface ApiService {
    public static final String APP_LOGIN = Constants.APP_LOGIN;

    @FormUrlEncoded
    @POST(APP_LOGIN)
    Call<BaseEntity<ArrayList<Question>>> getQuestionList(@Field("mtype") String mtype, @Field("paperid") String paperid);

    @FormUrlEncoded
    @POST(APP_LOGIN)
    Call<BaseEntity<ArrayList<Paper>>> getPaperList(@Field("mtype") String mtype, @Field("userid") String userid);

    @FormUrlEncoded
    @POST(APP_LOGIN)
    Call<User> getUser(@FieldMap Map<String,String > map);

    @FormUrlEncoded
    @POST(APP_LOGIN)
    Observable<BaseEntity<ArrayList<Question>>> getQuestionListRj(@Field("mtype") String mtype, @Field("paperid") String paperid);

    @FormUrlEncoded
    @POST(APP_LOGIN)
    Observable<BaseEntity<ArrayList<Paper>>> getPaperListRj(@Field("mtype") String mtype, @Field("userid") String userid);

    @FormUrlEncoded
    @POST(APP_LOGIN)
    Observable<User> getUserRj(@FieldMap Map<String,String > map);
}
