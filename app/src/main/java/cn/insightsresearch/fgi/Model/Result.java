package cn.insightsresearch.fgi.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/23.
 */
public class Result implements Serializable {
    private int id;
    private String uid;
    private int qid;
    private String value;
    private String adate;

    public Result(){
    }

    public Result(int qid, String value){
        this.qid = qid;
        this.value = value;
    }

    public String toString() {
        return "- - Answer:"+id+" | "+uid+" | "+qid+" | "+value+" | "+adate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAdate() {
        return adate;
    }

    public void setAdate(String adate) {
        this.adate = adate;
    }

}
