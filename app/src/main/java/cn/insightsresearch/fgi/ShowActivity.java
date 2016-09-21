package cn.insightsresearch.fgi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;

import cn.insightsresearch.fgi.Model.Logic;
import cn.insightsresearch.fgi.Model.Question;
import cn.insightsresearch.fgi.Model.QuestionEntity;
import cn.insightsresearch.fgi.common.PaperManager;
import cn.insightsresearch.fgi.Model.Result;
import cn.insightsresearch.fgi.common.ResultManager;
import cn.insightsresearch.fgi.Model.Paper;
import cn.insightsresearch.fgi.util.DateUtil;
import cn.insightsresearch.fgi.util.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import cn.insightsresearch.fgi.fragment.SpringProgressView;
import cn.insightsresearch.fgi.fragment.TitleView;
import cn.insightsresearch.fgi.util.QuestionUtil;

public class ShowActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = ShowActivity.class.getName();
    private TitleView mTitle;
    private ImageView imageView;
    private TextView textView;
    private Button button;
    private ProgressBar bar;
    private LinearLayout showLayout;
    private ArrayList<Question> nlist;
    private int total=0;
    private Paper paper;
    private int now = 0;
    private HashMap<Integer,Integer> map= new HashMap<Integer, Integer>();
    private ArrayList<Result> rlist = new ArrayList<Result>();
    private QuestionUtil qutil = new QuestionUtil().getQuestionUtil();
    private String uid = "";
    private int pid = 0;
    private PaperManager paperManager;

    private SpringProgressView progressView;
    private Random random = new Random(System.currentTimeMillis());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Intent intent = getIntent();
        paper= (Paper) intent.getSerializableExtra("PAPER");
        pid = paper.getPid();
        paperManager= new PaperManager(this);
        SharedPreferences sp = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        uid=String.valueOf(sp.getInt("USER_ID",0))+"u"+ DateUtil.getTime1String()+String.valueOf(random.nextInt(999));
        Log.i(TAG,"----uid----"+uid);

        //imageView = (ImageView)findViewById(R.id.imageView2);
        bar = (ProgressBar)findViewById(R.id.progressBar);
        showLayout = (LinearLayout)findViewById(R.id.showLayout);
        textView = (TextView)findViewById(R.id.tvPaper);
        button = (Button)findViewById(R.id.button);
        qutil.setShowLayout(showLayout);
        qutil.setTextView(textView);
        qutil.setButton(button);

        progressView = (SpringProgressView)findViewById(R.id.spring_progress_view);
        progressView.setMaxCount(100);
        //button.setBackgroundColor(Color.parseColor("#00000000"));

//        mTitle = (TitleView)findViewById(R.id.title);
//        mTitle.setTitle(paper.getPtitle());
//        mTitle.removeLeftButton();
//        mTitle.removeRightButton();

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadLocalData(pid);
            }
        }).start();

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        button.setEnabled(false);
        switch (view.getId()){
            case R.id.button:
                now = now + 1 ;
                Result result = qutil.getResult();
                rlist.add(result);

                Log.i(TAG,"----Question Result--now--"+now);
                if(total>now){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                    Log.i(TAG,"----Question Result----"+result.getQid()+"="+result.getValue());
                    ArrayList<Logic> logics = nlist.get(now).getLogics();
                    if(logics.size()>0){
                        int toLqid = doLogic(logics,rlist);
                        int logicnow = 0;
                        if(toLqid>0) {
                            if(toLqid==9999){
                                doSave();
                            }else {
                                if (map.get(toLqid) != null) {
                                    logicnow = map.get(toLqid);
                                    if (logicnow > now && logicnow < total) now = logicnow;
                                }
                                Log.i(TAG, "----Logic--toqid--" + toLqid + ",--Logic Now--" + logicnow);
                            }
                        }
                    }
                    progressView.setCurrentCount((now+1)*100/(total+1));
                    qutil.makeQuestion(this.getBaseContext(), now,rlist);
                }else{
                    doSave();
                }
                break;
            default:
                break;
        }

    }

    private void loadLocalData(int i) {
        paperManager.openDataBase();
        Paper paper = paperManager.getPaperByPid(pid+"");
        String result = paper.getJson();
        QuestionEntity questionData = new Gson().fromJson(result,QuestionEntity.class);
        nlist = questionData.getList();
        total = nlist.size();
        Log.i(TAG,"----loadLocalData----getAllPaperList"+nlist.size());
        Message msg = new Message();
        msg.what=1;
        mHandler.sendMessage(msg);
    }

    private void loadData(int pid) {
        bar.setVisibility(View.VISIBLE);
        try {
            HttpUtil httpUtil = new HttpUtil();
            final QuestionEntity questionData = httpUtil.getQuestionList(pid);
            nlist = questionData.getList();
            total = questionData.getTotal();
            Log.i(TAG,"----loadData----getQuestion:pid="+pid);
            Message msg = new Message();
            msg.what=1;
            mHandler.sendMessage(msg);
        } catch (Exception e) {
            Message msg = new Message();
            msg.what=10;
            mHandler.sendMessage(msg);
            e.printStackTrace();
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressView.setCurrentCount((now+1)*100/(total+1));
            bar.setVisibility(View.GONE);
            switch (msg.what) {
                case 1:
                    if(total>now){      //初始化问卷
                        for(int i=0;i<nlist.size();i++) {
                            map.put(nlist.get(i).getQid(), i);
                        }
                        //qutil.setMap(map);
                        qutil.setList(nlist);
                        qutil.makeQuestion(getBaseContext(),0,null);
                      Log.i(TAG, "----new Handler----makeQuestion:qid="+nlist.get(0).getQid());
                    }else{
                        button.setEnabled(false);
                        Toast.makeText(ShowActivity.this,R.string.papernoanswer, Toast.LENGTH_SHORT).show();
                        (new Handler()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ShowActivity.this.finish();
                                }
                        },800);
                    }
                    break;
                case 2:
                    button.setText(R.string.submitsucess);
                    Toast.makeText(ShowActivity.this,R.string.submitsucess, Toast.LENGTH_SHORT).show();
                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ShowActivity.this.finish();
                        }
                    },800);
                    break;
                case 10:
                    Log.i(TAG, "----new Handler----10 -- error");
                    Toast.makeText(ShowActivity.this,R.string.error_network, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    private void doSave(){
        showLayout.removeAllViews();
        bar.setVisibility(View.VISIBLE);

        textView.setText("提交中...");
        textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView tv = new TextView(this);
        tv.setText("即将提交成功...");
        tv.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv.setTextSize(25);
        tv.setMinLines(5);
        tv.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
        showLayout.addView(tv,params);

        button.setText("正在提交中...");
        try {
            Log.i(TAG,"----PostData----ResultList:size="+rlist.size());
            ResultManager resultManager = new ResultManager(getBaseContext(),pid);
            resultManager.openDataBase();
            paper.setUdate(DateUtil.getDateTime());
            paper.setTotal((paper.getTotal()+1));
            paperManager.updatePaperTotal(paper);
            long r =  resultManager.insertResultData(rlist,uid,pid+"");
            resultManager.closeDataBase();
            Log.i(TAG,"----PostData----AddResult:"+r);
            Message msg = new Message();
            msg.what=2;
            mHandler.sendMessage(msg);
        } catch (Exception e) {
            Message msg = new Message();
            msg.what=10;
            mHandler.sendMessage(msg);
            e.printStackTrace();
        }
    }

    private int doLogic(ArrayList<Logic> logics,ArrayList<Result> list){
        int toqid = 0;
        String value = ",,";
        for(Result r : list){
            String[] strr  = r.getValue().split(",");
            for(int i=0;i<strr.length;i++){
                value = value + "Q"+ r.getQid() +"-A"+ strr[i] + ",";
            }
        }
        //Log.i(TAG, "----logic--value--"+value);
        for(Logic logic : logics){
            Log.i(TAG, "----logic----"+logic.toString());
            boolean isb = false;
            if("and".equals(logic.getTag())) isb = true;
            String[] strl  = logic.getLogicid().split(",");
            for(int i=0;i<strl.length;i++){
                if("or".equals(logic.getTag()) && value.indexOf(","+strl[i]+",")>0){  isb = true;break; }
                if("and".equals(logic.getTag()) && value.indexOf(","+strl[i]+",")<0){  isb = false;break; }
            }
            if(logic.isornot()==1){
                if(isb){    toqid = logic.getToqid();break;   }
            }else{
                if(!isb){    toqid = logic.getToqid();break;   }
            }
        }
        return toqid;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("提示：").setMessage("请问您确认要退出回答问卷吗？").setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setNegativeButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ShowActivity.this.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }).create().show();
    }

}
