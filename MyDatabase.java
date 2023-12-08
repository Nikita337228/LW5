package com.example.lw1;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.Objects;

public class MyDatabase extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Users.db";
    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + DBContract.UserEntry.TABLE_NAME + "("
                + DBContract.UserEntry.COLUMN_NAME_KEY_ID + " INTEGER PRIMARY KEY," +
                DBContract.UserEntry.COLUMN_NAME_LOGIN + " TEXT," + DBContract.UserEntry.COLUMN_NAME_PASS + " TEXT" + ")";

        db.execSQL(CREATE_USERS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.UserEntry.TABLE_NAME);
        onCreate(db);
    }
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.UserEntry.COLUMN_NAME_LOGIN, user.getLogin());
        values.put(DBContract.UserEntry.COLUMN_NAME_PASS, user.getPass());

        db.insert(DBContract.UserEntry.TABLE_NAME, null, values);
        db.close();
    }
    public boolean checkUser(User user){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DBContract.UserEntry.TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do{
                if(Objects.equals(user._login, cursor.getString(1)) && Objects.equals(user._pass, cursor.getString(2)))
                    return true;
            }while (cursor.moveToNext());
        }
        return false;
    }
    public boolean refreshPass(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + DBContract.UserEntry.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ContentValues values = new ContentValues();
        if (cursor.moveToFirst()){
            do {
                if(Objects.equals(user._login, cursor.getString(1))){
                    values.put(DBContract.UserEntry.COLUMN_NAME_PASS, user.getPass());
                    int upd = db.update(DBContract.UserEntry.TABLE_NAME, values, "id = ?", new String[] {cursor.getString(0).toString()});
                    Log.i("Обновленно строк в бд", String.valueOf(upd));
                    return true;
                }
            }while(cursor.moveToNext());
        }
        return false;
    }
    public boolean deleteUser(User user){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DBContract.UserEntry.TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do{
                if(Objects.equals(user._login, cursor.getString(1)) && Objects.equals(user._pass, cursor.getString(2))){
                    int del = db.delete(DBContract.UserEntry.TABLE_NAME, "id = " + cursor.getString(0).toString(), null);
                    Log.i("Удалено строк в бд", String.valueOf(del));
                    return true;
                }
            }while (cursor.moveToNext());
        }
        return false;
    }

}
