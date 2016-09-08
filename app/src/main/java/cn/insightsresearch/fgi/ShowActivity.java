package cn.insightsresearch.fgi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;

import cn.insightsresearch.fgi.Model.BaseEntity;
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
    private Result result = null;
    private ArrayList<Result> rlist = new ArrayList<Result>();
    private HashMap<Integer,String> hmap = new HashMap<Integer, String>();
    private ArrayList<String> list = new ArrayList();
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
                int toqid = result.getToqid();
                Log.i(TAG,"----Question Result--toqid--"+toqid);
                if(toqid>0){
                    if(map.get(toqid)==null){  now = 999;}else{     now = map.get(toqid);}
                }
                Log.i(TAG,"----Question Result--now--"+now);
                if(total>now){
                    progressView.setCurrentCount((now+1)*100/(total+1));
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                    Log.i(TAG,"----Question Result----"+result.getQid()+"="+result.getValue());
                    rlist.add(result);
                    int qid = nlist.get(now).getQid();
                    qutil.makeQuestion(this.getBaseContext(), now);
                }else{
                    showLayout.removeAllViews();
                    rlist.add(result);
                    bar.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
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
                    if(total>now){
                        int qid = nlist.get(0).getQid();
                        for(int i=0;i<nlist.size();i++) {
                            map.put(nlist.get(i).getQid(), i);
                        }
                        //qutil.setMap(map);
                        qutil.setList(nlist);
                        qutil.makeQuestion(getBaseContext(),0);
                      Log.i(TAG, "----new Handler----makeQuestion:qid="+qid);
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
                    paperManager.closeDataBase();
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}
