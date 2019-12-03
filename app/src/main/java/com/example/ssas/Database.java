package com.example.ssas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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
            + "universityId integer primary key autoincrement, "
            + "teacherId integer, "
            + "universityName text)";

    public static final String CREATE_COURSE_TABLE = "create table course("
            + "courseId integer primary key autoincrement, "
            + "courseName text, "
            + "universityName text, "
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
                String email = cursor.getString(cursor.getColumnIndex("email"));

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




    public List<String> queryUniversity (String teacherId) 
    {
        List<String> res=new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from university where teacherId = ?", new String[] {teacherId});
        if(cursor!=null && cursor.moveToFirst()) 
        {
            do {
                String current_university=cursor.getString(cursor.getColumnIndex("universityName"));          
                res.add(current_university);    
            }while (cursor.moveTonext());
        }
        return res;

    }



    public ReturnMsg addUniversity(String teacherId, String universityName) 
    {
        ReturnMsg returnMsg=new ReturnMsg();
        boolean isSuccess=true;
        String message= "";
        Cursor cursor=db.rawQuery("select * from university where teacherId = ?", new String[] {teacherId});
        if(cursor!=null && cursor.moveToFirst())
        {
            do {
                String current_university=cursor.getString(cursor.getColumnIndex("universityName"));
                if(current_university.equals(universityName)) {
                    isSuccess=false;
                    message="The information already exists";
                    break;
                }
            }while (cursor.moveToNext());
        }
        if(isSuccess) {
            SQLiteDatabase db=this.getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(COL_1,teacherId);
            contentValues.put(COL_2,universityName);
            db.insert(university, null, contentValues);
            message="Information added";
        }
        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return returnMsg;
    }


    public ReturnMsg deleteUniversity(String universityId) {
        ReturnMsg returnMsg=new ReturnMsg();
        boolean isSuccess=true;
        String message="";
        SQLiteDatabase db=this.getWritableDatabase();
        int res=db.delete(university,"universityId = ?", new String[] {universityId});
        if(res==0) {
            isSuccess=false;
            message="The university does not exist";
        }
        else {
            message="University deleted";
        }
        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return returnMsg;
    }


    public List<String> queryCourses(String teacherId, String universityName) {
        List<String> res=new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from course where universityName = ? AND teacherId = ?", new String[] {universityName,teacherId});
        if(cursor!=null && cursor.moveToFirst())
        {
            do {
                String course=cursor.getString(cursor.getColumnIndex("courseName"));
                res.add(course);
            }while (cursor.moveToNext());
        }
        return res;
        
    }


    public ReturnMsg addCourse(String teacherId, String courseName, String universityName, String semester) {
        ReturnMsg returnMsg=new ReturnMsg();
        boolean isSuccess=true;
        String message="";
        Cursor cursor=db.rawQuery("select * from course where teacherId = ? AND courseName = ? AND universityName = ?", new String[] {teacherId, courseName, universityName});
        if(cursor !=null && cursor.moveToFirst())
        {
            do {
                String current_course=cursor.getString(cursor.getColumnIndex("courseName"));
                if(current_course.equals(courseName)) {
                    isSuccess=false;
                    message="The information already exists";
                    break;
                }
            }while (cursor.moveToNext());
        }
        if(isSuccess) {
            SQLiteDatabase db=this.getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(COL_2, courseName);
            contentValues.put(COL_3, universityName);
            int tem=Integer.parseInt(teacherId);
            contentValues.put(COL_4, tem);
            contentValues.put(COL_5, semester);
            db.insert(course,null,contentValues);
            message="new course added";
        }
        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return returnMsg;
    }



    public ReturnMsg deleteCourse(String courseId) {
        ReturnMsg returnMsg=new ReturnMsg();
        boolean isSuccess=true;
        String message="";
        SQLiteDatabase db=this.getWritableDatabase();
        int res=db.delete(course,"courseId = ?", new String[] {courseId});
        if(res==0) {
            isSuccess=false;
            message="The course does't exist.";
        }
        else {
            message="Course deleted";
        }
        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return returnMsg;
    }




    public List<String> queryClasses(String courseId){
        List<String> res=new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from class where courseId = ?", new String[] {courseId});
        if(cursor!=null && cursor.moveToFirst()) {
            do {
                String CLASS=cursor.getString(cursor.getColumnIndex("classId"));
                res.add(CLASS);
            }while(cursor.moveToNext());
        }
        return res;
    }


    public ReturnMsg addClass(String startTime, String courseId) {
        ReturnMsg returnMsg=new ReturnMsg();
        boolean isSuccess=true;
        String message="";
        Cursor cursor=db.rawQuery("select * from course where courseId = ? AND startTime = ?", new String[] {courseId, startTime});
        if(cursor !=null && cursor.moveTofirst())
        {
            isSuccess=false;
            message="The class already exists";
        }
        if(isSuccess) {
            SQLiteDatabase db=this.getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(COL_1,startTime);
            int tem=Integer.parseInt(courseId);
            contentValues.put(COL_2,tem);
            db.insert(CLASS,null,contentValues);
            message="new class added";
        }
        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return returnMsg;
    }



    public ReturnMsg deleteClass(String classId) {
        ReturnMsg returnMsg=new ReturnMsg();
        boolean isSuccess=true;
        String message="";
        SQLiteDatabase db=this.getWritableDatabase();
        int res=db.delete(CLASS,"classId = ?", new String[] {classId});
        if(res==0) {
            isSuccess=false;
            message="The class does not exist.";
        }
        else {
            message="Class deleted";
        }
        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return returnMsg;
    }





    public List<String> queryRecords(String classId){
        List<String> res=new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from record where classId = ?", new String[] {classId});
        if(cursor!=null && cursor.moveToFirst()) {
            do {
                String record=cursor.getString(cursor.getColumnIndex("classId"));
                res.add(record);
            }while(cursor.moveToNext());
        }
        return res;
    }


    public ReturnMsg changeProfile(String teacherName, String teacherId, String email) {
        ReturnMsg returnMsg=new ReturnMsg();
        boolean isSuccess=true;
        String message="";
        Cursor cursor=db.rawQuery("select * from teacher where teacherId = ?", new String[] {teacherId});
        if(cursor==null) {
            isSuccess=false;
            message="The teacher's information is not found";
        }
        else {
            message="The teacher's information updated";
        }
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL2,teacherName);
        contentValues.put(COL_4,email);
        db.update(teacher,contentValues,"teacherId = ?", new String[] {teacherId});
        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return returnMsg;
    }



    public ReturnMsg changePassword(String teacherId, String oldPassword, String newPassword) {
        ReturnMsg returnMsg=new ReturnMsg();
        boolean isSuccess=true;
        String message="";
        Cursor cursor=db.rawQuery("select * from teacher where teacherId = ? AND password = ?", new String[] {teacherId, oldPassword});
        if(cursor==null) {
            isSuccess=false;
            message="The password is not correct";
        }
        else {
            message="Your password has been updated";
        }
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_3,newPassword);
        db.update(teacher,contentValues,"teacherId = ?", new String[] {teacherId});
        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return returnMsg;
    }









