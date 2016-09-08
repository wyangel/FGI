package cn.insightsresearch.fgi.util;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import cn.insightsresearch.fgi.Model.Answer;
import cn.insightsresearch.fgi.Model.Question;
import cn.insightsresearch.fgi.Model.Result;
import cn.insightsresearch.fgi.R;

/**
 * Created by Administrator on 2016/8/23.
 */
public class QuestionUtil {
    private String TAG = this.getClass().getName();
    private QuestionUtil qutil;
    private LinearLayout showLayout;
    private TextView textView;
    private Button button;
    private Result result;
    private HashMap<String,Question> map;
    private ArrayList<Question> list;


    public QuestionUtil getQuestionUtil(){
        qutil = new QuestionUtil();
        return qutil;
    }

    public LinearLayout getShowLayout() {
        return showLayout;
    }

    public void setShowLayout(LinearLayout showLayout) {
        this.showLayout = showLayout;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public HashMap<String, Question> getMap() {
        return map;
    }

    public void setMap(HashMap<String, Question> map) {
        this.map = map;
    }

    public ArrayList<Question> getList() {
        return list;
    }

    public void setList(ArrayList<Question> list) {
        this.list = list;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public void makeQuestion(final Context context, int now){
        showLayout.removeAllViews();
        //final Question question = map.get(qid+"");
        final Question question = list.get(now);
        final ArrayList<Answer> alist = question.getAlist();
        final int qtype = question.getQtype();
        final int qradom = question.getRadom();
        result = new Result();
        result.setQid(question.getQid());
        result.setToqid(0);
        //result.setAdate(DateUtil.getDateTime());
        int cmin = question.getCmin(),cmax=question.getCmax(),atotal=alist.size();
        if(cmin<1||cmin>atotal) cmin = 1;
        if(cmax<1||cmax>atotal) cmax = atotal;
        if(cmin>cmax) cmin = 1;
        final int amin = cmin;
        final int amax = cmax;

        String title ="Q"+(now+1)+"."+question.getQtitle()+getType(qtype);
        if(qtype==2) title = title+"(选"+cmin+"-"+cmax+"项)";
        textView.setText(title);

        if(qtype<3&&qradom>0) { //随机排序
            final Random random = new Random(System.currentTimeMillis());
            final ArrayList<Answer> klist = new ArrayList<>();
            int len = alist.size();
            for(int i=len-qradom; i<len; i++){
                Answer ans = alist.get(i);
                klist.add(ans);
                alist.remove(i);
            }

            Collections.sort(alist, new Comparator<Answer>() {
                @Override
                public int compare(Answer answer, Answer t1) {
                    int a1 = random.nextInt(99);
                    int a2 = random.nextInt(99);
                    if (a1 > a2) {
                        return 1;
                    }
                    if (a1 == a2) {
                        return 0;
                    }
                    return -1;
                }
            });
            alist.addAll(klist);
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(5,3,5,3);
        switch (qtype) {
            case 1:
                RadioGroup rg = new RadioGroup(context);
                RadioGroup.LayoutParams paramsR = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                paramsR.setMargins(5,3,5,3);
                for (Answer answer : alist) {
                    RadioButton rb = new RadioButton(context);
                    rb.setText(answer.getAtitle());
                    rb.setId(answer.getAid());
                    rb.setTag(answer.getToqid());
                    rb.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    rb.setBackgroundResource(R.drawable.corners);
                    rg.addView(rb,paramsR);
                }
                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int id) {
                        for(int a=0;a<radioGroup.getChildCount();a++){
                            RadioButton rbtn = (RadioButton) radioGroup.getChildAt(a);
                            int rbtnid = rbtn.getId();
                            if(rbtnid==id){
                                rbtn.setTextColor(Color.WHITE);
                                rbtn.setBackgroundResource(R.drawable.corners_checked);
                                result.setToqid(Integer.parseInt(rbtn.getTag().toString()));
                                result.setValue(rbtnid+"");
                                button.setEnabled(true);
                            }else {
                                rbtn.setTextColor(Color.BLUE);
                                rbtn.setBackgroundResource(R.drawable.corners);
                            }
                        }

                    }
                });
                showLayout.addView(rg);
                break;
            case 2:
                final ArrayList<String> list = new ArrayList();
                final ArrayList<CheckBox> listm = new ArrayList();
                final ArrayList<CheckBox> listn = new ArrayList(); //排他list
                for (final Answer answer : alist) {
                    final CheckBox cb = new CheckBox(context);
                    cb.setText(answer.getAtitle());
                    cb.setId(answer.getAid());
                    cb.setTag(answer.getToqid());
                    //cb.setPadding(50, 10, 10, 10);
                    cb.setTextColor(Color.BLUE);
                    cb.setBackgroundResource(R.drawable.corners);
                    if(answer.getPaita()==1) {
                        listn.add(cb);
                    }else{
                        listm.add(cb);
                    }
                    cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton cbut, boolean b) {
                            if(b){
                                result.setToqid(Integer.parseInt(cbut.getTag().toString()));
                                if(answer.getPaita()==1){
                                    for (CheckBox cbox : listm) {
                                        cbox.setTextColor(Color.BLUE);
                                        cbox.setBackgroundResource(R.drawable.corners);
                                        cbox.setChecked(false);
                                    }
                                    for (CheckBox cbox : listn) {
                                        if(cbox.getId()!=cbut.getId())cbox.setChecked(false);cbox.setTextColor(Color.BLUE);cbox.setBackgroundResource(R.drawable.corners);
                                    }
                                }else{
                                    for (CheckBox cbox : listn) {
                                        cbox.setTextColor(Color.BLUE);
                                        cbox.setBackgroundResource(R.drawable.corners);
                                        cbox.setChecked(false);
                                    }
                                }
                                list.add(cbut.getId()+"");
                                cbut.setTextColor(Color.WHITE);
                                cbut.setBackgroundResource(R.drawable.corners_checked);
                            }
                            else{
                                list.remove(cbut.getId()+"");
                                cbut.setTextColor(Color.BLUE);
                                cbut.setBackgroundResource(R.drawable.corners);
                            }
                            //Log.i(TAG,"makeQuestion:"+list.size());

                            result.setValue(formatList(list));

                            if(list.size()>amin-1&&list.size()<amax+1){
                                button.setEnabled(true);
                                button.setText(R.string.buttonnext);
                            }
                            else{
                                button.setEnabled(false);
                                String text = context.getResources().getString(R.string.mustchoise);
                                text = String.format(text,amin,amax);
                                button.setText(text);
                            }
                        }
                    });
                    showLayout.addView(cb,params);
                }
                break;
            case 10:
                TextView tv = new TextView(context);
                tv.setText(question.getQtitle());
                tv.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                //tv.setPadding(50, 0, 0, 0);
                tv.setTextSize(16);
                tv.setMinLines(5);
                tv.setGravity(Gravity.CENTER_VERTICAL);
                tv.setBackgroundResource(R.drawable.corners1);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        button.setEnabled(true);
                    }
                },2000);
                showLayout.addView(tv,params);
                break;
            default:
                final EditText et = new EditText(context);
                //et.setId(qid);
                et.setMinLines(5);
                et.setTextColor(Color.BLACK);
                //et.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
                et.setHint(R.string.textinput);
                et.setBackgroundResource(R.drawable.corners1);
                et.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if(editable.length()<1){button.setEnabled(false);}else{button.setEnabled(true);}
                        result.setValue(editable.toString());
                    }
                });
                showLayout.addView(et,params);
                break;
        }
    }

    public String formatList(List<?> list) {
        StringBuilder b = new StringBuilder();
        boolean flag = false;
        for(Object o : list) {
            if (flag) {
                b.append(",");
            }else {
                flag=true;
            }
            b.append(o);
        }
        return b.toString();
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
}
