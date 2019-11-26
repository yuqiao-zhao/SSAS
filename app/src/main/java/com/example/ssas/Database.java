package com.example.ssas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class Database extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    public static final String CREATE_TEACHER_TABLE = "CREATE TABLE IF NOT EXISTS teacher("
            + "teacherId integer primary key, "
            + "teacherName text, "
            + "password text, "
            + "email text)";

    public static final String CREATE_STUDENT_TABLE = "CREATE TABLE IF NOT EXISTS student("
            + "studentId integer primary key, "
            + "studentName text)";

    public static final String CREATE_UNIVERSITY_TABLE = "create table university("
            + "teacherId integer, "
            + "universityName text)";

    public static final String CREATE_COURSE_TABLE = "create table course("
            + "courseId integer primary key autoincrement, "
            + "courseName text, "
            + "teacherId integer, "
            + "semester text)";

    public static final String CREATE_CLASS_TABLE = "create table class("
            + "classId integer primary key autoincrement, "
            + "startTime text, "
            + "courseId integer)";

    public static final String CREATE_RECORD_TABLE = "create table record("
            + "recordId integer primary key autoincrement, "
            + "studentId integer, "
            + "classId integer, "
            + "status text)";

    private Context mContext;

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
        mContext = context;
    }
    public Database(String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(null, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TEACHER_TABLE);
        sqLiteDatabase.execSQL(CREATE_CLASS_TABLE);
        sqLiteDatabase.execSQL(CREATE_COURSE_TABLE);
        sqLiteDatabase.execSQL(CREATE_STUDENT_TABLE);
        sqLiteDatabase.execSQL(CREATE_UNIVERSITY_TABLE);
        sqLiteDatabase.execSQL(CREATE_RECORD_TABLE);

        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public ReturnMsg queryLogin(String userID, String userPassword)
    {
        ReturnMsg returnMsg = new ReturnMsg();
        Boolean isSuccess = true;
        String message = "";

        Cursor cursor = db.rawQuery("select * from teacher where teacherId = ?", new String[]{userID});

        //teacher has registered
        if(cursor!=null && cursor.moveToFirst())
        {
            do {
                //check password
                String password = cursor.getString(cursor.getColumnIndex("password"));

                //password are equal, then login success
                if (password.equals(userPassword)) {
                    isSuccess = true;
                    message = "Login Success";
                } else//password is wrong
                {
                    isSuccess = false;
                    message = "Password is wrong";
                }
            }while (cursor.moveToNext());
        }
        else // teacher has not registered
        {
            isSuccess = false;
            message = "User has not registered";
        }

        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return  returnMsg;
    }

    public ReturnMsg insertNewTeacher(String userID, String userPassword, String userName, String email)
    {
        ReturnMsg returnMsg = new ReturnMsg();
        Boolean isSuccess = true;
        String message = "";

        Cursor cursor = db.rawQuery("select * from teacher where teacherId = ?", new String[]{userID});

        //teacher has registered
        if(cursor!=null && cursor.moveToFirst())
        {
            isSuccess = false;
            message = "You have already registered";
        }
        else // teacher has not registered
        {
            ContentValues values = new ContentValues();
            values.put("teacherId", userID);
            values.put("teacherName", userName);
            values.put("password", userPassword);
            values.put("email", email);
            db.insert("teacher", null, values);

            isSuccess = true;
            message = "Register success";
        }

        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return  returnMsg;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }
}