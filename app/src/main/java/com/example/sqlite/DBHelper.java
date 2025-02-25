package com.example.sqlite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "hongdroid.db";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //데이터 베이스가 생성될 때 호출
        db.execSQL("CREATE TABLE IF NOT EXISTS TodoList (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, content TEXT NOT NULL, writeDate TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    // SELECT 문 (할 일 목록 조회)
    public ArrayList<TodoItem> getTodoList(){
        ArrayList<TodoItem> todoItems = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TodoList ORDER BY writeDate DESC", null);
        if(cursor.getCount() != 0){
            // 호출한 데이터가 있을 때 내부 수행
            while (cursor.moveToNext()){
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
                @SuppressLint("Range") String writeDate = cursor.getString(cursor.getColumnIndex("writeDate"));

                TodoItem todoItem = new TodoItem();
                todoItem.setId(id);
                todoItem.setTitle(title);
                todoItem.setContent(content);
                todoItem.setWriteDate(writeDate);
                todoItem.add(todoItem);

            }
        }
        cursor.close();

        return todoItems;
    }

    // INSERT 문 (할 일 목록을 db에 넣는다)
    public void InsertTodo(String _title, String _content, String _writeDate) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO TodoList (title, content, writeDate) VALUES('" + _title + "', '" + _content + "', '" + _writeDate + "');");
    }

    // UPDATE 문 (할 일 목록을 수정한다)
    public void UpdateTodo(String _title, String _content, String _writeDate, String _beforeDate){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE TodoList SET title = '" + _title +  "', content = '" + _content +  "', writeDate = '" + _writeDate + "' WHERE writeDate = '" + _beforeDate +  "'");
    }


    // DELETE 문 (할 일 목록을 제거한다)
    public void deleteTodo(String _beforeDate ){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM TodoList WHERE writeDate = '" + _beforeDate + "'");
    }

}
