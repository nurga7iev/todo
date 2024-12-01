package com.example.lab6todo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "taskDB";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "tasks";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_STATUS = "status";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_STATUS + " INTEGER)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Метод для добавления новой задачи
    public long addTask(String title, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_STATUS, status);
        return db.insert(TABLE_NAME, null, values);
    }

    // Метод для получения всех задач
    public Cursor getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_STATUS},
                null, null, null, null, null);
    }

    // Метод для обновления статуса задачи
    public boolean updateTaskStatus(int taskId, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, status);
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(taskId)}) > 0;
    }

    // Метод для удаления задачи
    public boolean deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(taskId)}) > 0;
    }

    // Метод для получения статуса задачи
    public int getTaskStatus(int taskId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_STATUS}, COLUMN_ID + " = ?",
                new String[]{String.valueOf(taskId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS));
        }
        return -1; // Возвращаем -1, если задача не найдена
    }
}
