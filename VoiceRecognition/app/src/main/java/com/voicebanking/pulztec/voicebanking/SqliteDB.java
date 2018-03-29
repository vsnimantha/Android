package com.voicebanking.pulztec.voicebanking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nimantha on 2/9/2017.
 */

public class SqliteDB extends SQLiteOpenHelper {

    //region Variables

    public static String DATABASE_NAME="VoiceBanking.db"; //Database name

    public static final String TABLE_1="TB_CONVO";   //Table 1 name
    public static final String TB1_COL1="CONVOID";  //Table 1 Column 1
    public static final String TB1_COL2="QUESTION";  //Table 1 Column 2
    public static final String TB1_COL3="ANSWER";    //Table 1 Column 3

    private static SqliteDB instance;
    //endregion

    public static SqliteDB getInstance(Context context){
        if(instance==null){
            instance=new SqliteDB(context);
        }
        return instance;
    }

    public SqliteDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
        Cursor cursor=getAllFromTable1();
        if(cursor.getCount()>0){

        }else{
            insert_to_trans("How are you?","I am fine thank you");
            insert_to_trans("Good morning","Good morning");
            insert_to_trans("Good afternoon","Good afternoon");
            insert_to_trans("How is your day?","Good, How is yours?");
            insert_to_trans("What is the balance of my account","It's 10000 rupees");
        }
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_1 + " (CONVOID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,QUESTION TEXT,ANSWER TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_1);
    }

    //region Functions

    /**
     *
     * @param question Question which we are adding to the database
     * @param answer    answer for thr question
     * @return
     */
    public boolean insert_to_trans(String question,String answer) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TB1_COL2, question);
        contentValues.put(TB1_COL3, answer);

        long result = db.insert(TABLE_1, null, contentValues);


        if (result == -1) {
            return false;
        } else
            return true;
    }

    /**
     * get everything from table1
     * @return cursor type of object
     */
    public  Cursor getAllFromTable1(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from "+TABLE_1,null);
        return cursor;
    }
    /**
     * get all the detais from table 1
     * @param question question you want to search
     * @return cursor type of object
     */
    public Cursor getAllFromTable1(String question){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from "+TABLE_1+" Where QUESTION LIKE '%"+question+"%'",null);
        return cursor;
    }

    /**
     * clear the table 1
     */
    public void deleteData_Table1() {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("Delete From " + TABLE_1 );

    }
    //endregion


}
