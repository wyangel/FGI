package cn.insightsresearch.fgi.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import cn.insightsresearch.fgi.Model.Paper;

public class PaperManager {

    private static final String TAG = PaperManager.class.getName();
    private static final String DB_NAME = "fgi_bdata";
    private static final String TABLE_NAME = "paper";
    public static final String ID = "_id";

    public static final String PID = "pid";
    public static final String PTITLE = "ptitle";
    public static final String PSORT = "psort";
    public static final String ISSHOW = "isshow";
    public static final String ADATE = "adate";
    public static final String TOTAL = "total";
    public static final String UDATE = "udate";
    public static final String JSON = "json";

    private static final int DB_VERSION = 2;
    private Context mContext = null;

    private static String DB_CREATE = "CREATE TABLE " + TABLE_NAME + " ("
            + ID + " integer primary key," + PID + " integer," + PTITLE + " varchar," + PSORT + " integer," + ISSHOW + " integer,"
            + ADATE + " varchar," + TOTAL + " integer,"+ UDATE + " varchar," + JSON + " varchar" + ");";

    private SQLiteDatabase mSQLiteDatabase = null;
    private static DataBaseManagementHelper mDatabaseHelper = null;

    private static class DataBaseManagementHelper extends SQLiteOpenHelper {

        DataBaseManagementHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG,"db.getVersion()="+db.getVersion());
            db.execSQL(DB_CREATE);
            Log.i(TAG, "db.execSQL(DB_CREATE)");
            Log.e(TAG, DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(TAG, "DataBaseManagementHelper onUpgrade");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
            onCreate(db);
        }
    }

    public PaperManager(Context context) {
        mContext = context;
        Log.i(TAG, "PaperManager construction!");
    }

    public synchronized static DataBaseManagementHelper getInstance(Context context) {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = new DataBaseManagementHelper(context);
        }
        return mDatabaseHelper;
    };

    public void openDataBase() throws SQLException {
        mDatabaseHelper = getInstance(mContext);
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
        //mDatabaseHelper.onCreate(mSQLiteDatabase);
    }

    public void closeDataBase() throws SQLException {
        mDatabaseHelper.close();
    }

    public long insertPaperData(Paper paper) {
        Log.i(TAG,"insertPaperData() , pid="+paper.getPid());
        ContentValues values = new ContentValues();
        values.put(PID,paper.getPid());
        values.put(PTITLE, paper.getPtitle());
        values.put(PSORT, paper.getPsort());
        values.put(ISSHOW,paper.getIsshow());
        values.put(ADATE,paper.getAdate());
        values.put(JSON, paper.getJson());
        values.put(TOTAL,paper.getTotal());
        values.put(UDATE,paper.getUdate());
        return mSQLiteDatabase.insert(TABLE_NAME, ID, values);
    }

    public boolean updatePaperData(Paper paper) {
        int pid = paper.getPid();
        Log.i(TAG,"updatePaperData() , pid="+pid);
        ContentValues values = new ContentValues();
        values.put(PTITLE, paper.getPtitle());
        values.put(PSORT, paper.getPsort());
        values.put(ISSHOW,paper.getIsshow());
        values.put(ADATE,paper.getAdate());
        values.put(JSON, paper.getJson());
        values.put(TOTAL,paper.getTotal());
        values.put(UDATE,paper.getUdate());
        return mSQLiteDatabase.update(TABLE_NAME, values, PID + "=" + pid, null) > 0;
    }

    public boolean updatePaperTotal(Paper paper) {
        int pid = paper.getPid();
        Log.i(TAG,"updatePaperTotal() , pid="+pid);
        ContentValues values = new ContentValues();
        values.put(TOTAL,paper.getTotal());
        values.put(UDATE,paper.getUdate());
        return mSQLiteDatabase.update(TABLE_NAME, values, PID + "=" + pid, null) > 0;
    }

    public Cursor fetchPaperData(int pid) throws SQLException {
        Cursor mCursor = mSQLiteDatabase.query(false, TABLE_NAME, null, PID
                + "=" + pid, null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public ArrayList<Paper> fetchAllPapers() {
        ArrayList list = new ArrayList<Paper>();
        Cursor mCursor = mSQLiteDatabase.query(TABLE_NAME, null, null, null, null, null, null);
        while (mCursor.moveToNext()) {
            Paper paper = new Paper();
                paper.setPid(mCursor.getInt(1));
                paper.setPtitle(mCursor.getString(2));
                paper.setPsort(Integer.parseInt(mCursor.getString(3)));
                paper.setIsshow(Integer.parseInt(mCursor.getString(4)));
                paper.setAdate(mCursor.getString(5));
                paper.setTotal(mCursor.getInt(6));
                paper.setUdate(mCursor.getString(7));
                //paper.setJson(mCursor.getString(8));
            list.add(paper);
        }
        return list;
    }

    public boolean deletePaperData(int pid) {
        return mSQLiteDatabase.delete(TABLE_NAME, PID + "=" + pid, null) > 0;
    }

    public boolean deleteAllPapers() {
        return mSQLiteDatabase.delete(TABLE_NAME, null, null) > 0;
    }

    public String getStringByColumnName(String columnName, int pid) {
        Cursor mCursor = fetchPaperData(pid);
        int columnIndex = mCursor.getColumnIndex(columnName);
        String columnValue = mCursor.getString(columnIndex);
        mCursor.close();
        return columnValue;
    }

    public boolean updatePaperByPid(String columnName, int pid,
                                      String columnValue) {
        ContentValues values = new ContentValues();
        values.put(columnName, columnValue);
        return mSQLiteDatabase.update(TABLE_NAME, values, PID + "=" + pid, null) > 0;
    }

    public Paper getPaperByPid(String pid){
        Log.i(TAG,"getPaperByPid , pid="+pid);
        Cursor mCursor=mSQLiteDatabase.query(TABLE_NAME, null, PID+"="+pid, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        Paper paper = new Paper();
            paper.setPid(mCursor.getInt(1));
            paper.setPtitle(mCursor.getString(2));
            paper.setPsort(mCursor.getInt(3));
            paper.setIsshow(mCursor.getInt(4));
            paper.setAdate(mCursor.getString(5));
            paper.setTotal(mCursor.getInt(6));
            paper.setUdate(mCursor.getString(7));
            paper.setJson(mCursor.getString(8));
        return paper;
    }

    public long checkPaper(Paper paper){

        long isok=0;
        int pid = paper.getPid();
        String adate = paper.getAdate();
        Log.i(TAG,"checkPaperByPid , pid="+pid);
        Cursor mCursor=mSQLiteDatabase.query(TABLE_NAME, null, PID+"="+pid, null, null, null, null);
        if (mCursor.moveToFirst()) {
           if(!mCursor.getString(5).equals(adate)){
                isok = updatePaperData(paper)?1:0;
           }else{
               Log.i(TAG,"NoNeed Update pid="+pid);
           }
        }else{
            isok = insertPaperData(paper);
        }
        return isok;
    }
}