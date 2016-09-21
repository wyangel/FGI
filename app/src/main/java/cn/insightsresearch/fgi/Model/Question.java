package cn.insightsresearch.fgi.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/18.
 */

public class Question implements Serializable {
    private int id;
    private int paperid;
    private int qid;
    private String qtitle;
    private int qtype;
    private int qsort;
    private int radom;
    private int cmin;
    private int cmax;
    private ArrayList<Logic> logics;
    private ArrayList<Answer> alist;

    public String toString() {
            return "- - Question:"+id+" | "+paperid+" | "+qid+" | "+qtitle+" | "+qtype+" | "+qsort+" | "+radom+" | "+cmin+" | "+cmax+" | logics size:"+logics.size()+" | Alist size:"+alist.size();
    }

    public int getCmax() {
        return cmax;
    }

    public void setCmax(int cmax) {
        this.cmax = cmax;
    }

    public int getCmin() {
        return cmin;
    }

    public void setCmin(int cmin) {
        this.cmin = cmin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPaperid() {
        return paperid;
    }

    public void setPaperid(int paperid) {
        this.paperid = paperid;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public int getQsort() {
        return qsort;
    }

    public void setQsort(int qsort) {
        this.qsort = qsort;
    }

    public String getQtitle() {
        return qtitle;
    }

    public void setQtitle(String qtitle) {
        this.qtitle = qtitle;
    }

    public int getQtype() {
        return qtype;
    }

    public void setQtype(int qtype) {
        this.qtype = qtype;
    }

    public int getRadom() {
        return radom;
    }

    public void setRadom(int radom) {
        this.radom = radom;
    }

    public ArrayList<Logic> getLogics() {
        return logics;
    }

    public void setLogics(ArrayList<Logic> logics) {
        this.logics = logics;
    }

    public ArrayList<Answer> getAlist() {
            return alist;
        }

        public void setAlist(ArrayList<Answer> alist) {
            this.alist = alist;
        }

}