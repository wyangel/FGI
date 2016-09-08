package cn.insightsresearch.fgi.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import cn.insightsresearch.fgi.Model.BaseEntity;
import cn.insightsresearch.fgi.Model.Paper;
import cn.insightsresearch.fgi.Model.Question;
import cn.insightsresearch.fgi.Model.QuestionEntity;
import cn.insightsresearch.fgi.Model.Result;
import cn.insightsresearch.fgi.util.DateUtil;

public class ResultManager {

    private static final String TAG = ResultManager.class.getName();
    private static final String DB_NAME = "fgi_data";
    private static final String TABLE_NAME = "result";
    private static String TABLEID = "";
    public static final String ID = "_id";

    public static final String UID = "uid";
    public static final String QID = "qid";
    public static final String PID = "pid";
    public static final String RESULT = "result";
    public static final String ADATE = "adate";

    private static final int DB_VERSION = 2;
    private Context mContext = null;

    private static String DB_CREATE = "CREATE TABLE " + TABLE_NAME+TABLEID + " ("
            + ID + " integer primary key," + UID + " varchar," + QID + " varchar," + RESULT + " varchar,"
            + ADATE + " datetime" + ");";

    private SQLiteDatabase mSQLiteDatabase = null;
    private DataBaseManagementHelper mDatabaseHelper = null;

    private static class DataBaseManagementHelper extends SQLiteOpenHelper {

        DataBaseManagementHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.setVersion(DB_VERSION);
            Log.i(TAG,"db.getVersion()="+db.getVersion());
            db.execSQL(DB_CREATE);
            Log.i(TAG, "db.execSQL(DB_CREATE)");
            Log.e(TAG, DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(TAG, "DataBaseManagementHelper onUpgrade");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME+TABLEID + ";");
            onCreate(db);
        }

    }

    public ResultManager(Context context,int pid) {
        mContext = context;
        if(pid>0)   TABLEID = "_"+pid;
        Log.i(TAG, "ResultManager construction!");
    }

    public void openDataBase() throws SQLException {
        mDatabaseHelper = new DataBaseManagementHelper(mContext);
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
    }

    public void rebuildTable(Paper paper) throws SQLException {
        Gson gson = new Gson();
        TABLEID = "_" + paper.getPid();
        String result = paper.getJson();
        QuestionEntity questionData = new Gson().fromJson(result,QuestionEntity.class);
        ArrayList<Question> nlist = questionData.getList();
        String zuhe = "";
        for (Question qs : nlist) {
                zuhe = zuhe + "q" + qs.getQid() + " varchar,";
        }
        DB_CREATE = "CREATE TABLE " + TABLE_NAME + TABLEID + " ("
                    + ID + " integer primary key," + UID + " varchar," + PID + " varchar," + zuhe + ADATE + " datetime" + ");";
        mSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME+TABLEID + ";");
        mSQLiteDatabase.execSQL(DB_CREATE);;
        Log.i(TAG, "----rebuildTable---"+TABLEID);
    }

    public void dropTable(int pid) {
        mSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME+TABLEID +"_"+pid+ ";");
    }

    public void closeDataBase() throws SQLException {
        mDatabaseHelper.close();
    }

    public long insertResultData(ArrayList<Result> list,String uid,String pid) {
        String date = DateUtil.getDateTime();
        ContentValues values = new ContentValues();
        for(Result rs : list){
            values.put("q"+rs.getQid(), rs.getValue());
        }
        values.put(UID, uid);
        values.put(PID, pid);
        values.put(ADATE,date);
        return mSQLiteDatabase.insert(TABLE_NAME+TABLEID, ID, values);
    }

    public Cursor fetchResultData(String uid) throws SQLException {
        Cursor mCursor = mSQLiteDatabase.query(false, TABLE_NAME+TABLEID, null, UID
                + "='" + uid+"'", null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllResults() {
        return mSQLiteDatabase.query(TABLE_NAME+TABLEID, null, null, null, null, null,
                null);
    }

    public boolean deleteResultData(int id) {
        return mSQLiteDatabase.delete(TABLE_NAME+TABLEID, ID + "=" + id, null) > 0;
    }

    public boolean deleteAllResults() {
        return mSQLiteDatabase.delete(TABLE_NAME+TABLEID, null, null) > 0;
    }

    public String getStringByColumnName(String columnName, String uid) {
        Cursor mCursor = fetchResultData(uid);
        int columnIndex = mCursor.getColumnIndex(columnName);
        String columnValue = mCursor.getString(columnIndex);
        mCursor.close();
        return columnValue;
    }

    public HashMap<String, String> getMapResultByUid(String uid) {
        HashMap<String, String> map = new HashMap<>();
        Cursor mCursor = fetchResultData(uid);
        if(mCursor.moveToFirst()){
            for(int i=0;i<mCursor.getColumnCount();i++){
                String columnName = mCursor.getColumnName(i);
                String columnValue = mCursor.getString(i);
                map.put(columnName,columnValue);
            }
        }
        mCursor.close();
        return map;
    }

    public ArrayList<Result> getResultListByPid(String pid){
        Log.i(TAG,"getResultByPid , pid="+pid);
        ArrayList list = new ArrayList<Result>();
        Cursor mCursor=mSQLiteDatabase.query(TABLE_NAME+TABLEID, null, PID+"="+pid, null, null, null, "_id desc");
        while (mCursor.moveToNext()) {
            Result result = new Result();
            result.setId(mCursor.getInt(0));
            result.setUid(mCursor.getString(1));
            result.setQid(Integer.parseInt(mCursor.getString(2)));
            //result.setValue(mCursor.getString(3));
            result.setAdate(mCursor.getString(mCursor.getColumnIndex(ADATE)));
            list.add(result);
        }
        mCursor.close();
        return list;
    }

    public long countResult(int pid){
        int istotal = 0 ;
        Cursor mCursor=mSQLiteDatabase.query(TABLE_NAME+TABLEID , new String[] {"count(_id)"}, PID+"="+pid, null, null, null, null);
        if (mCursor.moveToFirst()) {
            istotal = mCursor.getInt(0);
        }

//        Cursor mCursor1=mSQLiteDatabase.query(TABLE_NAME+TABLEID , new String[] {"adate"}, PID+"="+pid, null, null, null, "_id desc");
//        if (mCursor1.moveToFirst()) {
//           // Log.i(TAG,"countResult , pid="+mCursor1.getString(0));
//        }
        mCursor.close();
        return istotal;
    }

}