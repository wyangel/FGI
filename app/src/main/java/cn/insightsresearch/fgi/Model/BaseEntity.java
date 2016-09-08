package cn.insightsresearch.fgi.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/30.
 */
public class BaseEntity<E> implements Serializable{
    private int total;
    private E list;

    public E getList() {
        return list;
    }

    public void setList(E list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
