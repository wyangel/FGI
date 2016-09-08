package cn.insightsresearch.fgi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.insightsresearch.fgi.Model.Answer;
import cn.insightsresearch.fgi.Model.Paper;
import cn.insightsresearch.fgi.Model.Question;
import cn.insightsresearch.fgi.Model.QuestionEntity;
import cn.insightsresearch.fgi.Model.Result;
import cn.insightsresearch.fgi.common.PaperManager;
import cn.insightsresearch.fgi.common.ResultManager;

public class ResultDetailActivity extends AppCompatActivity {
    private static String TAG = ResultDetailActivity.class.getName();
    private Paper paper;
    private Result result;
    private SwipeRefreshLayout srl;
    private TextView mTitle,paper_tv;
    private ListView listView;
    private ArrayList<Question> nlist ;
    private BaseAdapter adapter;
    private PaperManager paperManager;
    private ResultManager resultManager;
    private int pid = 0 ,total=0;
    private String uid = "";
    private HashMap<String ,String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultdetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        Intent intent = getIntent();
        result= (Result) intent.getSerializableExtra("RESULT");
        pid = result.getQid();
        uid = result.getUid();

        paperManager = new PaperManager(this);
        paperManager.openDataBase();
        paper = paperManager.getPaperByPid(pid+"");

        ResultManager resultManager = new ResultManager(this,pid);
        resultManager.openDataBase();
        map = resultManager.getMapResultByUid(uid);
        resultManager.closeDataBase();


        String result = paper.getJson();
        //Log.i(TAG," ----getPaper ----json="+result);
        QuestionEntity questionData = new Gson().fromJson(result,QuestionEntity.class);
        total = questionData.getTotal();
        nlist = questionData.getList();


        srl = (SwipeRefreshLayout)findViewById(R.id.srl_paper);
        srl.setColorSchemeResources(R.color.colorPrimary,R.color.colorAccent);
        mTitle = (TextView) findViewById(R.id.toolbar_title);
        mTitle.setText(paper.getPtitle());
        listView = (ListView) findViewById(R.id.list_paper);

        adapter = new ListAdapter(this,nlist);
        listView.setAdapter(adapter);
        Log.i(TAG," - - "+TAG+" - -");

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                nlist.clear();
//                ArrayList list  = resultManager.getResultByPid(pid+"");
//                nlist.addAll(list);
//                adapter.notifyDataSetChanged();
                srl.setRefreshing(false);
            }
        });
    }

    public class ListAdapter extends BaseAdapter {
        private ArrayList<Question> list;
        private Context context;

        public ListAdapter(Context context,ArrayList<Question> list){
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder = new ViewHolder();
            if(view==null){
                view = LayoutInflater.from(context).inflate(R.layout.detail_list, null);
                viewHolder.id = (TextView)view.findViewById(R.id.id);
                viewHolder.num = (TextView)view.findViewById(R.id.textNum);
                viewHolder.title = (TextView)view.findViewById(R.id.title);
                viewHolder.text = (TextView)view.findViewById(R.id.text);
                viewHolder.content = (TextView)view.findViewById(R.id.content);
                view.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) view.getTag();
            }
            Question question = list.get(i);
            ArrayList<Answer> answers = list.get(i).getAlist();
            int qtype = question.getQtype();
            String title = question.getQtitle()+ "" +getType(qtype)+ "";
            if(qtype==2){title = title + getMinMax(question.getCmin(),question.getCmax(),answers.size());    }
            String qvalue = map.get("q"+question.getQid());
            String content = "";
            if(qtype<5){
                for(Answer answer : answers){
                    if(!"".equals(content)){
                        content = content + "<br>";
                    }
                    if(isInside(qvalue,""+answer.getAid())>0){
                        content = content + "<font color=red><b>"+answer.getAid() + "、" + answer.getAtitle()+"√</b></font>";
                    }else{
                        content = content + answer.getAid() + "、" + answer.getAtitle();
                    }
                }

            }else{
                content = qvalue;
                if(qtype==10){
                    content = question.getQtitle();
                }
            }
            if(content==null){  content="";}
            viewHolder.title.setText(title);
            viewHolder.content.setText(Html.fromHtml(content));
            viewHolder.id.setText(question.getId()+"");
            viewHolder.num.setText((i+1)+"");
            if(qvalue==null){
                viewHolder.text.setText("");
            }else{
                viewHolder.text.setText("回答："+qvalue);
            }
            Log.i(TAG," ----getPaper ----Uid="+list.get(i).getQid()+list.get(i).toString());
            return view;
        }

        class ViewHolder {
            TextView id;
            TextView num;
            TextView title;
            TextView content;
            TextView text;
        }
    }

    public int isInside(String a,String b){
        a = ",," + a + ",";
        b = "," + b + ",";
        //Log.i(TAG," ----getPaper ----Uid="+a+"|"+b+is);
        return a.indexOf(b);
    }

    public String getMinMax(int cmin,int cmax,int atotal) {
        if (cmin < 1 || cmin > atotal) cmin = 1;
        if (cmax < 1 || cmax > atotal) cmax = atotal;
        if (cmin > cmax) cmin = 1;
        return "(选"+cmin+"-"+cmax+"项)";
    }

    public String getType(int i){
        switch (i){
            case 1:
                return "(单选)";
            case 2:
                return "(多选)";
            case 3:
                return "(各项单选)";
            case 4:
                return "(各项多选)";
            case 5:
                return "(问答)";
            default:
                return "（说明）";
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 点击返回图标事件
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}
