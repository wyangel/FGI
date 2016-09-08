package cn.insightsresearch.fgi.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/30.
 */
public class Paper implements Serializable {
    private int id;
    private int pid;
    private String ptitle;
    private int psort;
    private int isshow;
    private String adate;
    private String udate;
    private int total;
    private String json;

    public Paper(){}

    @Override
    public String toString() {
        return "- - Paper:"+id+" | "+pid+" | "+ptitle+" | "+psort+" | "+isshow+" | "+udate+" | "+total+" | "+adate;
    }

    public String getAdate() {
        return adate;
    }

    public void setAdate(String adate) {
        this.adate = adate;
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

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getPsort() {
        return psort;
    }

    public void setPsort(int psort) {
        this.psort = psort;
    }

    public String getPtitle() {
        return ptitle;
    }

    public void setPtitle(String ptitle) {
        this.ptitle = ptitle;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getUdate() {
        return udate;
    }

    public void setUdate(String udate) {
        this.udate = udate;
    }
}
