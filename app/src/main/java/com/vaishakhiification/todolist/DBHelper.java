package com.vaishakhiification.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "ToDoListDB";
    private static final int DB_VERSION = 1;

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        updateDatabase(sqLiteDatabase, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        updateDatabase(sqLiteDatabase, i, i1);
    }

    public void updateDatabase(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("CREATE TABLE TODOLIST(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "NAME TEXT, " +
                "DESCRIPTION TEXT, " +
                "STATUS_ID INTEGER ," +
                "IS_COMPLETED INTEGER " +
                ")");
    }

    public int insertTask(TaskDetails task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", task.getName());
        contentValues.put("DESCRIPTION", task.getDescription());
        contentValues.put("STATUS_ID", task.getStatusId());
        contentValues.put("IS_COMPLETED", task.isCompleted());
        long result = db.insert("TODOLIST", null, contentValues);
        return (int) result;
    }

    public void deletetask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("TODOLIST", "_id=?", new String[]{Integer.toString(taskId)});
    }

    public List<TaskDetails> getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("TODOLIST",
                new String[]{"_id", "NAME", "DESCRIPTION", "STATUS_ID", "IS_COMPLETED"},
                null, null, null, null, null);
        return convertCursorToList(cursor);
    }

    public List<TaskDetails> searchTasks(String searchText) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id, NAME, DESCRIPTION, STATUS_ID, IS_COMPLETED FROM "
                + "TODOLIST" + " WHERE " + "NAME" + " LIKE '" + searchText
                + "%'", null);
        return convertCursorToList(cursor);
    }


    public void updateTask(TaskDetails task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", task.getName());
        contentValues.put("DESCRIPTION", task.getDescription());
        contentValues.put("STATUS_ID", task.getStatusId());
        contentValues.put("IS_COMPLETED", task.isCompleted());
        db.update("TODOLIST", contentValues, "_id=?", new String[]{Integer.toString(task.getId())});
    }

    public List<TaskDetails> getSortedTasks(boolean sortById) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> result = new ArrayList<>();
        Cursor cursor = null;
        if (sortById) {
            cursor = db.query("TODOLIST",
                    new String[]{"_id", "NAME", "DESCRIPTION", "STATUS_ID", "IS_COMPLETED"},
                    null,
                    null,
                    null, null, "_id ASC");

        } else {
            cursor = db.query("TODOLIST",
                    new String[]{"_id", "NAME", "DESCRIPTION", "STATUS_ID", "IS_COMPLETED"},
                    null,
                    null,
                    null, null, "NAME ASC");

        }
        return convertCursorToList(cursor);
    }

    private List<TaskDetails> convertCursorToList(Cursor cursor) {
        List<TaskDetails> taskDetails = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TaskDetails task = new TaskDetails(
                    cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getString(cursor.getColumnIndex("NAME")),
                    cursor.getString(cursor.getColumnIndex("DESCRIPTION")),
                    cursor.getInt(cursor.getColumnIndex("IS_COMPLETED")) > 0,
                    cursor.getInt(cursor.getColumnIndex("STATUS_ID"))
            );
            taskDetails.add(task);
            cursor.moveToNext();
        }
        cursor.close();
        return taskDetails;
    }

}
