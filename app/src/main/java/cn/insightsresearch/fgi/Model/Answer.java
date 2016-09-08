package cn.insightsresearch.fgi.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/23.
 */
public class Answer implements Serializable {
    private int id;
    private int questionid;
    private int aid;
    private String atitle;
    private int toqid;
    private int asort;
    private int isshow;
    private int paita;

    public String toString() {
        return "- - Answer:"+id+" | "+questionid+" | "+aid+" | "+atitle+" | "+toqid+" | "+asort+" | "+isshow+" | "+paita;
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

    public int getQuestionid() {
        return questionid;
    }

    public void setQuestionid(int questionid) {
        this.questionid = questionid;
    }

    public int getToqid() {
        return toqid;
    }

    public void setToqid(int toqid) {
        this.toqid = toqid;
    }

    public int getPaita() {
        return paita;
    }

    public void setPaita(int paita) {
        this.paita = paita;
    }
}