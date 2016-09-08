package cn.insightsresearch.fgi.fragment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cn.insightsresearch.fgi.Model.Paper;
import cn.insightsresearch.fgi.R;
import cn.insightsresearch.fgi.util.DateUtil;

/**
 * Created by Administrator on 2016/8/19.
 */
public class MyPaperAdapter extends BaseAdapter {
    private String TAG = MyListAdapter.class.getName();
    private ArrayList<Paper> list;
    private Context context;

    public MyPaperAdapter(Context context, ArrayList<Paper> list){
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
            view = LayoutInflater.from(context).inflate(R.layout.item_list, null);
            viewHolder.id = (TextView)view.findViewById(R.id.id);
            viewHolder.num = (TextView)view.findViewById(R.id.textNum);
            viewHolder.title = (TextView)view.findViewById(R.id.title);
            viewHolder.text = (TextView)view.findViewById(R.id.text);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.title.setText(list.get(i).getPtitle());
        viewHolder.num.setText((i+1)+"");
        switch (i){
            case 0:
                viewHolder.num.setBackgroundResource(R.drawable.num1);
                break;
            case 1:
                viewHolder.num.setBackgroundResource(R.drawable.num2);
                break;
            case 2:
                viewHolder.num.setBackgroundResource(R.drawable.num3);
                break;
            default:
                break;
        }
        viewHolder.id.setText(list.get(i).getPid()+"");
        viewHolder.text.setText(DateUtil.getFormatDate(list.get(i).getAdate()));
        Log.i(TAG," ----getPaper ----Pid="+list.get(i).getPid()+list.get(i).toString());
        return view;
    }

    class ViewHolder {
        TextView id;
        TextView num;
        TextView title;
        TextView text;
    }
}
