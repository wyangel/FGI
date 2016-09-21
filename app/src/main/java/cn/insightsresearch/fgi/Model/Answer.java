package cn.insightsresearch.fgi.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/23.
 */
public class Answer implements Serializable {
    private int id;
    private int qid;
    private int aid;
    private String atitle;
    private Logic alogic;
    private int asort;
    private int isshow;
    private int paita;

    public String toString() {
        return "- - Answer:"+id+" | "+qid+" | "+aid+" | "+atitle+" | "+asort+" | "+isshow+" | "+paita+" | aLogic:"+alogic.toString();
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getAsort() {
        return asort;
    }

    public void setAsort(int asort) {
        this.asort = asort;
    }

    public String getAtitle() {
        return atitle;
    }

    public void setAtitle(String atitle) {
        this.atitle = atitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsshow() {
        return isshow;
    }

    public void setIsshow(int isshow) {
        this.isshow = isshow;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public int getPaita() {
        return paita;
    }

    public void setPaita(int paita) {
        this.paita = paita;
    }

    public Logic getAlogic() {
        return alogic;
    }

    public void setAlogic(Logic alogic) {
        this.alogic = alogic;
    }
}