package cn.insightsresearch.fgi.Process;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import cn.insightsresearch.fgi.Model.BaseEntity;
import cn.insightsresearch.fgi.Model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/30.
 */
public class BaseTask<T> {
    private static String TAG = BaseTask.class.getName();
    private Call<BaseEntity<T>> callBase;
    private Call<T> call;
    private Context context;
    private Observable<T> observable;

    public BaseTask(Context context,Call call){
        this.call = call;
        this.callBase = call;
        this.context = context;
    }

    public BaseTask(Context context,Observable observable){
        this.observable = observable;
        this.context = context;
    }

    public void handleResponse(final ResponseListener<T> listener){
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if(response.isSuccessful()&&response.errorBody()==null){
                    listener.onSuccess((T) response.body());
                }else{
                    listener.onFail();
                    Log.d(TAG, "error message:" + response.message());
                    Toast.makeText(context, "网络请求返回异常！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                listener.onFail();
                Log.d(TAG, "error:" + t.getMessage());
                Toast.makeText(context, "网络请求出现异常！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void handleBaseEntityResponse(final ResponseListener<T> listener){
        callBase.enqueue(new Callback<BaseEntity<T>>() {
            @Override
            public void onResponse(Call<BaseEntity<T>> call, Response<BaseEntity<T>> response) {
                if(response.isSuccessful()&&response.errorBody()==null){
                    if(response.body().getTotal()>0){
                        listener.onSuccess((T) response.body().getList());
                    }else{
                        Toast.makeText(context, "暂无数据！", Toast.LENGTH_SHORT).show();
                        listener.onFail();
                    }
                }else{
                    Log.d(TAG, "error message:" + response.message());
                    Toast.makeText(context, "网络请求返回异常！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseEntity<T>> call, Throwable t) {
                Log.d(TAG, "error:" + t.getMessage());
                Toast.makeText(context, "网络请求出现异常！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void handleResponseRj(final ResponseListener<T> listener){
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<T>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted:");
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onFail();
                        Toast.makeText(context, "网络请求出现异常！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(T t) {
                        listener.onSuccess(t);
                        Log.d(TAG, "onNext:"+t.toString());
                    }
                });

    }

    public interface ResponseListener<T> {
        void onSuccess(T t);

        void onFail();
    }
}
