package cn.insightsresearch.fgi.Model;

import java.io.Serializable;


public class User implements Serializable {
    private int id;
    private String username;
    private String userpwd;
    private String lastlogin;
    private int isok;

    @Override
    public String toString() {
        return "- - User:"+id+" | "+username+" | "+userpwd+" | "+lastlogin+" | "+isok;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsok() {
        return isok;
    }

    public void setIsok(int isok) {
        this.isok = isok;
    }

    public String getLastlogin() {
         return lastlogin;
    }

    public void setLastlogin(String lastlogin) {
            this.lastlogin = lastlogin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpwd() {
        return userpwd;
    }

    public void setUserpwd(String userpwd) {
        this.userpwd = userpwd;
    }
}
