package cn.insightsresearch.fgi.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import cn.insightsresearch.fgi.HelpActivity;
import cn.insightsresearch.fgi.R;


/**
 * Created by Administrator on 2016/8/11.
 */
public class SearchFragment extends Fragment {
    private static String TAG = SearchFragment.class.getName();
    private long exitTime = 0;
    private View mParent;
    private FragmentActivity mActivity;
    private TitleView mTitle;
    private TextView mText;

    public SearchFragment() {
    }

    public static SearchFragment newInstance(int index) {
        SearchFragment f = new SearchFragment();

        // Supply index input as an argument.
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
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        Log.i("all","SearchF");
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "--------onCreate SearchF");
        mActivity = getActivity();
        mParent = getView();

        mTitle = (TitleView) mParent.findViewById(R.id.title);
        mTitle.setTitle(R.string.tab_search);
        mTitle.setLeftButton(R.string.loginout, new TitleView.OnLeftButtonClickListener(){

            @Override
            public void onClick(View button) {
                exit();
            }

        });
        mTitle.setRightButton(R.string.tab_help, new TitleView.OnRightButtonClickListener() {

            @Override
            public void onClick(View button) {
                goHelpActivity();
            }
        });

        mText = (TextView) mParent.findViewById(R.id.fragment_search_text);
        mText.setText(R.string.tab_search);
    }

    private void goHelpActivity(){
        Intent intent = new Intent(mActivity, HelpActivity.class);
        startActivity(intent);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(mActivity, "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            mActivity.finish();
            System.exit(0);
        }
    }
}
