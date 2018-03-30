package com.example.nimantha.virtualtranslator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nimantha on 5/25/2016.
 */
public class Sqlite extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="Translator.db";

    public static final String TABLE_NAME1="TRANSLATION";
    public static final String COLTR1="TRANS_ID";
    public static final String COLTR2="LAN_JAP";
    public static final String COLTR3="LAN_SIN";
    public static final String COLTR4="LAN_ENG";

    public Sqlite(Context context) {
        super(context, DATABASE_NAME, null, 1);
        // SQLiteDatabase db=this.getWritableDatabase();
        //  SQLiteDatabase db=this.getReadableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME1 + " (TRANS_ID INTEGER,LAN_JAP TEXT,LAN_SIN TEXT,LAN_ENG TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME1);

    }

    public boolean insert_to_trans(String TransID,String jp,String sin,String eng) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLTR1, TransID);
        contentValues.put(COLTR2, jp);
        contentValues.put(COLTR3, sin);
        contentValues.put(COLTR4, eng);

        long result = db.insert(TABLE_NAME1, null, contentValues);


        if(result==-1){
            return false;
        }else
            return true;
    }
    public Cursor getAll_trans(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from "+TABLE_NAME1,null);
        return cursor;
    }

     public Cursor getAll_trans(String lan){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from "+TABLE_NAME1+" Where LAN_JAP='"+lan+"'",null);
        return cursor;
    }

    public void deleteData_trans() {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("Delete From " + TABLE_NAME1 );

    }
}
