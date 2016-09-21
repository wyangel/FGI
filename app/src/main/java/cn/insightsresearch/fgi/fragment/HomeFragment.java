package cn.insightsresearch.fgi.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import cn.insightsresearch.fgi.common.PaperManager;
import cn.insightsresearch.fgi.Api.RetroFactory;
import cn.insightsresearch.fgi.Model.BaseEntity;
import cn.insightsresearch.fgi.Model.Paper;
import cn.insightsresearch.fgi.Model.Question;
import cn.insightsresearch.fgi.Process.BaseTask;
import cn.insightsresearch.fgi.R;
import cn.insightsresearch.fgi.common.ResultManager;
import cn.insightsresearch.fgi.util.DateUtil;
import cn.insightsresearch.fgi.util.NetworkUtil;

/**
 * Created by Administrator on 2016/8/11.
 */
public class HomeFragment extends Fragment {
    private static String TAG = HomeFragment.class.getName();
    private static final String ARG_PARAM1 = "HomeFragment_index";
    private long exitTime = 0;
    private View mParent;
    private FragmentActivity mActivity;
    private TextView utime;
    private TextView mText;
    private Button button;
    private Button qingkong;
    private int uid =0;
    private PaperManager paperManager;
    private SharedPreferences sp;
    private String uptime;

    public static HomeFragment newInstance(String index) {
        HomeFragment f = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, index);
        f.setArguments(args);
        return f;
    }

    public String getShownIndex() {
        return getArguments().getString(ARG_PARAM1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        Log.i(TAG, "--------onCreateView HomeF");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "--------onActivityCreated HomeF");
        mActivity = getActivity();
        mParent = getView();

        paperManager = new PaperManager(getContext());
        sp= getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        uid=sp.getInt("USER_ID",0);
        uptime = sp.getString("UPDATE_TIME","暂无");
        String uname = sp.getString("USER_NAME","Err");
        checkUserPaper(uid,false);

        mText = (TextView) mParent.findViewById(R.id.fragment_home_text);
        mText.setText(uname+" : "+getResources().getString(R.string.welcome));
        Log.i(TAG, DateUtil.getTime1String()+"|"+ DateUtil.getTimeString());
        button =(Button)mParent.findViewById(R.id.shoudong);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setText("已经更新");
                button.setEnabled(false);
                Toast.makeText(mActivity,"清空问卷回答数据表！", Toast.LENGTH_SHORT).show();
            }
        });
        qingkong =(Button)mParent.findViewById(R.id.qingkong);
        qingkong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("提示：").setMessage("请问您确认要清空全部回答数据吗？").setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setNegativeButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        checkUserPaper(uid,true);
                        qingkong.setText("已经清空");
                        qingkong.setEnabled(false);
                        Toast.makeText(mActivity,"全部数据已经清空！", Toast.LENGTH_SHORT).show();
                    }
                }).create().show();
            }
        });
        utime = (TextView)mParent.findViewById(R.id.update_time);
        utime.setText("数据更新时间："+uptime);
    }

    public void checkUserPaper(final int uid,boolean b){
        paperManager.openDataBase();
        if(b) {
            paperManager.deleteAllPapers();
            ResultManager resultManager = new ResultManager(mActivity,0);
            resultManager.openDataBase();
            resultManager.deleteAllTables();
            resultManager.closeDataBase();
        }
        if(NetworkUtil.isConnected(getContext())){
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    try {
                        new BaseTask<ArrayList<Paper>>(mActivity, RetroFactory.getApiService().getPaperList("fgi.paper",uid+"")).handleBaseEntityResponse(new BaseTask.ResponseListener<ArrayList<Paper>>() {
                            @Override
                            public void onSuccess(ArrayList<Paper> list) {
                                for(final Paper p : list){
                                    if(paperManager.checkPaper(p)>0){
                                        final int pid = p.getPid();
                                        new BaseTask<BaseEntity<ArrayList<Question>>>(mActivity, RetroFactory.getApiService().getQuestionList("fgi.question",pid+""))
                                                .handleResponse(new BaseTask.ResponseListener<BaseEntity<ArrayList<Question>>>() {
                                                    @Override
                                                    public void onSuccess(BaseEntity<ArrayList<Question>> arrayListBaseEntity) {
                                                        Gson gson = new Gson();
                                                        String json = gson.toJson(arrayListBaseEntity);
                                                        paperManager.updatePaperByPid("json",pid,json);
                                                        ResultManager resultManager = new ResultManager(mActivity,pid);
                                                        resultManager.openDataBase();
                                                        p.setJson(json);
                                                        resultManager.rebuildTable(p);
                                                        resultManager.closeDataBase();
                                                    }

                                                    @Override
                                                    public void onFail() {
                                                        Log.i(TAG, "--CheckPaper-GetQuestion-onFail");
                                                        Toast.makeText(mActivity,"问题数据更新失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                    Log.d(TAG, "onSuccess Paper: "+p.toString());
                                    uptime = DateUtil.getDateTime();
                                    sp.edit().putString("UPDATE_TIME",uptime).apply();
                                    utime.setText("检查更新时间："+uptime);
                                }
                            }

                            @Override
                            public void onFail() {
                                Log.i(TAG, "--CheckPaper--onFail");
                                Toast.makeText(mActivity,"数据检查更新失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                            }
                        });
//                        HttpUtil httpUtil = new HttpUtil();
//                        final PaperData paperData = httpUtil.getPaperList(uid);
//                        //int total = paperData.getTotal();
//                        ArrayList<PaperData.Paper> list = paperData.getList();
//                        for(PaperData.Paper paper:list){
//                            if(paperManager.checkPaper(paper)>0){
//                                int pid = paper.getPid();
//                                String json = httpUtil.getQuestionListJson(pid);
//                                paperManager.updatePaperByPid("json",pid,json);
//                            }
//                        }
//                        Log.i(TAG, "--CheckPaper--loadData----getPaperList:userid=" + uid);
//                        Message msg = new Message();
//                        msg.what = 1;
//                        mHandler.sendMessage(msg);
//                    } catch (Exception e) {
//                        Message msg = new Message();
//                        msg.what = 10;
//                        mHandler.sendMessage(msg);
//                        e.printStackTrace();
//                    }
                }
            }).start();
        }else{
            if(paperManager.fetchAllPapers().isEmpty()){
                Toast.makeText(mActivity,"请先连接网络更新调查问卷数据！", Toast.LENGTH_LONG).show();
            }
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Log.i(TAG, "--CheckPaper--new Handler----Refresh"+msg.what);
                    //mTitle.setTitle("Total:"+total);
                    break;
                case 10:
                    Log.i(TAG, "--CheckPaper--new Handler----Refresh10 -- error");
                    Toast.makeText(mActivity,"数据检查更新失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}

