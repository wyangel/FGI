package cn.insightsresearch.fgi.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/30.
 */
public class QuestionEntity implements Serializable{
    private int total;
    private ArrayList<Question> list;

    public ArrayList<Question> getList() {
        return list;
    }

    public void setList(ArrayList<Question> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
