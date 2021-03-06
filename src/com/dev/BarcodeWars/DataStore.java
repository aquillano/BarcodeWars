package com.dev.BarcodeWars;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataStore 
{
    public static final String KEY_ROWID = "_id";
    public static final String KEY_WINS = "wins";
    public static final String KEY_LOSSES = "losses";
    public static final String KEY_ENERGY = "energy";
    public static final String KEY_INFANTRY = "infantry";
    public static final String KEY_KNOWLEDGE = "knowledge";
    private static final String TAG = "DBAdapter";
    
    private static final String DATABASE_NAME = "game_stats";
    private static final String DATABASE_TABLE = "user_stats";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
        "create table titles (_id integer primary key autoincrement, "
        + "wins integer(5), "
        + "losses integer(5), "
        + "energy integer(5), "
        + "infantry ineteger(5), "
        + "knowledge integer(5));";
        
    private final Context context; 
    
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DataStore(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
        
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
        int newVersion) 
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion 
                    + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS titles");
            onCreate(db);
        }
    }    
    
    //---opens the database---
    public DataStore open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
    
    //---insert a title into the database---
    public long insertRecord(int wins, int losses, int energy, int infantry, int knowledge) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_WINS, wins);
        initialValues.put(KEY_LOSSES, losses);
        initialValues.put(KEY_ENERGY, energy);
        initialValues.put(KEY_INFANTRY, infantry);
        initialValues.put(KEY_KNOWLEDGE, knowledge);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular record---
    public boolean deleteRecord(long rowId) 
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + 
        		"=" + rowId, null) > 0;
    }

    //---retrieves all the records---
    public Cursor getAllRecords() 
    {
        return db.query(DATABASE_TABLE, new String[] {
        		KEY_ROWID, 
        		KEY_WINS,
        		KEY_LOSSES,
                KEY_ENERGY,
                KEY_INFANTRY,
                KEY_KNOWLEDGE}, 
                null, 
                null, 
                null, 
                null, 
                null);
    }

    //---retrieves a particular title---
    public Cursor getRecord(long rowId) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                		KEY_ROWID,
                		KEY_WINS, 
                		KEY_LOSSES,
                		KEY_ENERGY,
                		KEY_INFANTRY,
                		KEY_KNOWLEDGE
                		}, 
                		KEY_ROWID + "=" + rowId, 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a record---
    public boolean updateRecord(long rowId, int wins, int losses, int energy, int infantry, int knowledge) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_WINS, wins);
        args.put(KEY_LOSSES, losses);
        args.put(KEY_ENERGY, energy);
        args.put(KEY_INFANTRY, infantry);
        args.put(KEY_KNOWLEDGE, knowledge);
        return db.update(DATABASE_TABLE, args, 
                         KEY_ROWID + "=" + rowId, null) > 0;
    }
}
