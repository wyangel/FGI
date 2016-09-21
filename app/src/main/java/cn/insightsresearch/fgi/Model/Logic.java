package cn.insightsresearch.fgi.Model;

/**
 * Created by Administrator on 2016/9/14.
 */

import java.io.Serializable;

public class Logic implements Serializable {
    private int qid;
    private int toqid;
    private String tag;
    private String logicid;
    private int isornot;

    public String toString(){
        return "- - Logic:"+qid+" | "+toqid+" | "+tag+" | "+logicid+" | "+isornot;
    }

    public int isornot() {
        return isornot;
    }

    public void setIsornot(int isornot) {
        this.isornot = isornot;
    }

    public String getLogicid() {
        return logicid;
    }

    public void setLogicid(String logicid) {
        this.logicid = logicid;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getToqid() {
        return toqid;
    }

    public void setToqid(int toqid) {
        this.toqid = toqid;
    }
}
