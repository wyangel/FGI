package cn.insightsresearch.fgi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.insightsresearch.fgi.Model.Result;
import cn.insightsresearch.fgi.common.ResultManager;
import cn.insightsresearch.fgi.Model.Paper;

public class ResultActivity extends AppCompatActivity {
    private String TAG = ResultActivity.class.getName();
    private Paper paper;
    private SwipeRefreshLayout srl;
    private TextView mTitle;
    private ListView listView;
    private ArrayList<Result> nlist ;
    private ResultAdapter adapter;
    private ResultManager resultManager;
    private int pid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        Intent intent = getIntent();
        paper= (Paper) intent.getSerializableExtra("PAPER");
        pid = paper.getPid();

        srl = (SwipeRefreshLayout)findViewById(R.id.srl2);
        srl.setColorSchemeResources(R.color.colorPrimary,R.color.colorAccent);
        mTitle = (TextView) findViewById(R.id.toolbar_title);
        mTitle.setText(R.string.list_result);
        listView = (ListView) findViewById(R.id.listresult);
        nlist = new ArrayList<Result>();
        resultManager = new ResultManager(this,pid);
        resultManager.openDataBase();
        nlist  = resultManager.getResultListByPid(pid+"");
        if(nlist.isEmpty()){
            Toast.makeText(this,"暂时没有数据，请稍后查看", Toast.LENGTH_SHORT).show();
            (new Handler()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    ResultActivity.this.finish();
                }
            },800);
        }
        adapter = new ResultAdapter(this,nlist);
        listView.setAdapter(adapter);

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                Result result = (Result) nlist.get(i);
                Intent intent = new Intent(ResultActivity.this, ResultDetailActivity.class);
                intent.putExtra("RESULT", result);
                startActivity(intent);
                Log.i(TAG," ----Result onItemClick ----Uid="+result.getUid());
            }
        });
        resultManager.closeDataBase();
    }

    public class ResultAdapter extends BaseAdapter{
        private ArrayList<Result> list;
        private Context context;

        public ResultAdapter(Context context,ArrayList<Result> list){
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
                view = LayoutInflater.from(context).inflate(R.layout.result_list, null);
                viewHolder.id = (TextView)view.findViewById(R.id.id);
                viewHolder.title = (TextView)view.findViewById(R.id.title);
                viewHolder.text = (TextView)view.findViewById(R.id.text);
                view.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.title.setText((i+1)+"、"+list.get(i).getUid());
            viewHolder.id.setText(list.get(i).getId()+"");
            viewHolder.text.setText("回答时间："+list.get(i).getAdate());
            Log.i(TAG," ----getPaper ----Uid="+list.get(i).getUid()+list.get(i).toString());
            return view;
        }

        class ViewHolder {
            TextView id;
            TextView title;
            TextView text;
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
