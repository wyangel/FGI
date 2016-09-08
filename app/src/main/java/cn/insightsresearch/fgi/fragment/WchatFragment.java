package cn.insightsresearch.fgi.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.insightsresearch.fgi.R;


/**
 * Created by Administrator on 2016/8/11.
 */
public class WchatFragment extends Fragment {
    private long exitTime = 0;
    private View mParent;
    private FragmentActivity mActivity;
    private TitleView mTitle;
    private TextView mText;

    public WchatFragment() {
    }

    public static WchatFragment newInstance(int index) {
        WchatFragment f = new WchatFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wchat,container,false);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        mParent = getView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
