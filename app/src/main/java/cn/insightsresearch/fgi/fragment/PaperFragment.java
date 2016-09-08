package cn.insightsresearch.fgi.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.insightsresearch.fgi.Model.BaseEntity;
import cn.insightsresearch.fgi.common.PaperManager;
import cn.insightsresearch.fgi.Model.Paper;
import cn.insightsresearch.fgi.R;
import cn.insightsresearch.fgi.ShowActivity;
import cn.insightsresearch.fgi.common.ResultManager;
import cn.insightsresearch.fgi.util.HttpUtil;

public class PaperFragment extends ListFragment {
    private String TAG = PaperFragment.class.getName();
    private static final String ARG_PARAM1 = "NewsFragment_index";
    private TitleView mTitle;
    private View fragView;
    private ArrayList<Paper> nlist ;
    private int total;
    private SwipeRefreshLayout srl;
    private BaseAdapter adapter;
    private PaperManager paperManager;
    private int uid =0;
    private SharedPreferences sp;

    public static PaperFragment newInstance(String index) {
        PaperFragment f = new PaperFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, index);
        f.setArguments(args);
        return f;
    }

    public String getShownIndex() {
        return getArguments().getString(ARG_PARAM1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "--------onCreate NewsF");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragView =inflater.inflate(R.layout.fragment_news,null);
        Log.i(TAG, "--------onCreateView NewsF");

        sp= getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        uid=sp.getInt("USER_ID",0);

        initView();
        paperManager= new PaperManager(getContext());
        paperManager.openDataBase();

        loadLocalData(uid);

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadLocalData(uid);
            }
        });
        return fragView;
    }

    private void loadLocalData(int uid) {
        nlist.clear();
        total = 0;
        ArrayList<Paper> list = paperManager.fetchAllPapers();
        nlist.addAll(list);
        adapter.notifyDataSetChanged();
        Log.i(TAG,"----loadLocalData----getAllPaperList"+uid);
        srl.setRefreshing(false);
    }

    private void updateTotalData() {
        ResultManager resultManager = new ResultManager(getActivity(),1);
        resultManager.openDataBase();
        for(Paper paper:nlist){
            int pid = paper.getPid();
            long mytotal = resultManager.countResult(pid);
            Log.i(TAG, "----mytotal---"+mytotal);
            paperManager.updatePaperByPid("total",pid,mytotal+"");
        }
        resultManager.closeDataBase();
        Log.i(TAG,"----updateTotalData----");
        loadLocalData(uid);
        srl.setRefreshing(false);
    }

    private void loadData(int uid) {
        try {
            nlist.clear();
            HttpUtil httpUtil = new HttpUtil();
            final BaseEntity<ArrayList<Paper>> paperData = httpUtil.getPaperList(uid);
            total = paperData.getTotal();
            ArrayList<Paper> list = paperData.getList();
            nlist.addAll(list);
            Log.i(TAG,"----loadData----getPaperList:userid="+uid);
            Message msg = new Message();
            msg.what=1;
            mHandler.sendMessage(msg);
        } catch (Exception e) {
            Message msg = new Message();
            msg.what=10;
            mHandler.sendMessage(msg);
            e.printStackTrace();
//            (new Handler()).postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        srl.setRefreshing(false);
//                    }
//            },5000);
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            srl.setRefreshing(false);
            switch (msg.what) {
                case 1:
                    Log.i(TAG, "----new Handler----Refresh"+msg.what);
                    adapter.notifyDataSetChanged();
                    break;
                case 2:
//                    Bundle bundle = msg.getData();
//                    ArrayList<Paper> list = (ArrayList<Paper>) bundle.getSerializable("mlist");
//                    nlist.clear();
//                    nlist.addAll(list);
                    Log.i(TAG, "----new Handler----Refresh2");
                    adapter.notifyDataSetChanged();
                    break;
                case 10:
                    Log.i(TAG, "----new Handler----Refresh10 -- error");
                    Toast.makeText(getContext(),R.string.error_network, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onListItemClick(ListView lv, View v, int position, long id) {
        super.onListItemClick(lv, v, position, id);
        Paper paper = (Paper)getListAdapter().getItem(position);
        Intent intent = new Intent(getActivity(), ShowActivity.class);
        intent.putExtra("PAPER", paper);
        startActivity(intent);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void initView() {
        srl = (SwipeRefreshLayout)fragView.findViewById(R.id.srl);
        srl.setColorSchemeResources(R.color.colorPrimary,R.color.colorAccent);
        srl.setRefreshing(true);

        nlist = new ArrayList<Paper>();
        adapter= new MyPaperAdapter(getContext(),nlist);
        setListAdapter(adapter);
    }

}
