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
            + "universityId integer, "
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

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    public Database(String name, SQLiteDatabase.CursorFactory factory, int version) {
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

    public ReturnMsg queryLogin(String userID, String userPassword) {
        ReturnMsg returnMsg = new ReturnMsg();
        Boolean isSuccess = true;
        String message = "";

        Cursor cursor = db.rawQuery("select * from teacher where teacherId = ?", new String[]{userID});

        //teacher has registered
        if (cursor != null && cursor.moveToFirst()) {
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
            } while (cursor.moveToNext());
        } else // teacher has not registered
        {
            isSuccess = false;
            message = "User has not registered";
        }

        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return returnMsg;
    }

    public ReturnMsg insertNewTeacher(String userID, String userPassword, String userName, String email) {
        ReturnMsg returnMsg = new ReturnMsg();
        Boolean isSuccess = true;
        String message = "";

        Cursor cursor = db.rawQuery("select * from teacher where teacherId = ?", new String[]{userID});

        //teacher has registered
        if (cursor != null && cursor.moveToFirst()) {
            isSuccess = false;
            message = "You have already registered";
        } else // teacher has not registered
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
        return returnMsg;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }



    public List<University> queryUniversity(String teacherId) {
        List<University> res = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from university where teacherId = ?", new String[]{teacherId});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                University university = new University();
                String current_university_name = cursor.getString(cursor.getColumnIndex("universityName"));
                String current_university_id = cursor.getString(cursor.getColumnIndex("universityId"));
                university.setUniversityName(current_university_name);
                university.setUniversityId(current_university_id);
                res.add(university);
            } while (cursor.moveToNext());
        }
        return res;

    }


    public ReturnMsg addUniversity(String teacherId, String universityName) {
        ReturnMsg returnMsg = new ReturnMsg();
        boolean isSuccess = true;
        String message = "";

        ContentValues values = new ContentValues();
        values.put("teacherId", teacherId);
        values.put("universityName", universityName);
        db.insert("university", null, values);

        isSuccess = true;
        message = "Add university success";

        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return returnMsg;
    }

    public ReturnMsg deleteUniversity(String universityId)
    {
        ReturnMsg returnMsg = new ReturnMsg();
        boolean isSuccess = true;
        String message = "";

        int res = db.delete("university", "universityId = ?", new String[]{universityId});
        if (res == 0) {
            isSuccess = false;
            message = "Delete university failed.";
        } else {
            message = "Delete university success.";
        }

        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return returnMsg;
    }

    public ReturnMsg modifyUniversity(String universityId, String universityName)
    {
        ReturnMsg returnMsg = new ReturnMsg();
        boolean isSuccess = true;
        String message = "";

        ContentValues values = new ContentValues();
        values.put("universityName", universityName);

        db.update("university", values, "universityId = ?", new String[]{universityId});

        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return returnMsg;
    }


    public List<Course> queryCourses(String teacherId, String universityId) {
        List<Course> res = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from course where universityId = ? AND teacherId = ?", new String[]{universityId, teacherId});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Course course = new Course();
                course.setCourseName(cursor.getString(cursor.getColumnIndex("courseName")));
                course.setCourseID(cursor.getString(cursor.getColumnIndex("courseId")));
                course.setSemester(cursor.getString(cursor.getColumnIndex("semester")));
                res.add(course);
            } while (cursor.moveToNext());
        }
        return res;

    }

    //    public static final String CREATE_COURSE_TABLE = "create table course("
//            + "courseId integer primary key autoincrement, "
//            + "courseName text, "
//            + "universityName text, "
//            + "teacherId integer, "
//            + "semester text)";
    public ReturnMsg modifyCourse(String courseId, String courseName, String semester)
    {
        ReturnMsg returnMsg = new ReturnMsg();
        boolean isSuccess = true;
        String message = "";

        ContentValues values = new ContentValues();
        values.put("courseName", courseName);
        values.put("semester", semester);

        db.update("course", values, "courseId = ?", new String[]{courseId});

        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return returnMsg;
    }

    public ReturnMsg addCourse(String teacherId, String courseName, String universityId, String semester) {
        ReturnMsg returnMsg = new ReturnMsg();
        boolean isSuccess = true;
        String message = "";

        ContentValues contentValues = new ContentValues();
        contentValues.put("courseName", courseName);
        contentValues.put("universityId", universityId);
        int id = Integer.parseInt(teacherId);
        contentValues.put("teacherId", id);
        contentValues.put("semester", semester);
        db.insert("course", null, contentValues);
        message = "new course added";

        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return returnMsg;
    }


    public ReturnMsg deleteCourse(String courseId) {
        ReturnMsg returnMsg = new ReturnMsg();
        boolean isSuccess = true;
        String message = "";
        int res = db.delete("course", "courseId = ?", new String[]{courseId});
        if (res == 0) {
            isSuccess = false;
            message = "The course does't exist.";
        } else {
            message = "Course deleted";
        }
        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return returnMsg;
    }

    public Boolean hasClassBefore(String courseId, String classId)
    {
        Cursor cursor = db.rawQuery("select * from class where courseId = ? and classId <> ?", new String[]{courseId,classId});
        if (cursor != null && cursor.moveToFirst()) {
            return true;
        }
        else
            return false;
    }

    public List<Class> queryClasses(String courseId) {
        List<Class> res = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from class where courseId = ?", new String[]{courseId});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Class aClass=new Class();
                aClass.setStartTime(aClass.StrToDate(cursor.getString(cursor.getColumnIndex("startTime"))));
                aClass.setClassId(cursor.getString(cursor.getColumnIndex("classId")));
                res.add(aClass);
            } while (cursor.moveToNext());
        }
        return res;

    }
    //    public static final String CREATE_CLASS_TABLE = "create table class("
////            + "classId integer primary key autoincrement, "
////            + "startTime text, "
////            + "courseId integer)";
    public ReturnMsg modifyClass(String classId, String startTime)
    {
        ReturnMsg returnMsg = new ReturnMsg();
        boolean isSuccess = true;
        String message = "";

        ContentValues values = new ContentValues();
        values.put("startTime", startTime);

        db.update("class", values, "classId = ?", new String[]{classId});

        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return returnMsg;
    }

    public ReturnMsg addClass(String startTime, String courseId) {
        ReturnMsg returnMsg = new ReturnMsg();
        boolean isSuccess = true;
        String message = "";
        ContentValues contentValues = new ContentValues();
        contentValues.put("startTime", startTime);
        contentValues.put("courseId", courseId);
        db.insert("class", null, contentValues);
        message = "new class added";

        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return returnMsg;
    }

    public ReturnMsg deleteClass(String classId) {
        ReturnMsg returnMsg = new ReturnMsg();
        boolean isSuccess = true;
        String message = "";
        int res = db.delete("class", "classId = ?", new String[]{classId});
        if (res == 0) {
            isSuccess = false;
            message = "The class does't exist.";
        } else {
            message = "Class deleted";
        }
        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return returnMsg;
    }

    public int findMaxClassIdInCourse(String courseId, String classId)
    {
        int max=0;
        Cursor cursor = db.rawQuery("select max(classId) as maxium from class where courseId = ? and classId <> ?", new String[]{courseId,classId});
        if (cursor != null && cursor.moveToFirst()) {
            max = Integer.valueOf(cursor.getString(cursor.getColumnIndex("maxium")));
        }
        return max;
    }

    //    public static final String CREATE_RECORD_TABLE = "create table record("
//            + "recordId integer primary key autoincrement, "
//            + "studentId integer, "
//            + "classId integer, "
//            + "status text)";
    public ReturnMsg createRecordsDefault(String classId, String courseId)
    {
        ReturnMsg returnMsg = new ReturnMsg();
        boolean isSuccess = true;
        String message = "";

        int maxClassId = findMaxClassIdInCourse(courseId,classId);
        Cursor cursor = db.rawQuery("select record.recordId,record.status,student.studentId,student.studentName,class.startTime  " +
                "from record,class,student where class.classId = record.classId and class.classId = ? " +
                "and record.studentId = student.studentId", new String[]{String.valueOf(maxClassId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                //update
                ContentValues contentValues = new ContentValues();
                contentValues.put("studentId", cursor.getString(cursor.getColumnIndex("studentId")));
                contentValues.put("classId", classId);
                contentValues.put("status","Present");
                db.insert("record", null, contentValues);

            } while (cursor.moveToNext());
        }

        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return returnMsg;
    }

    public ReturnMsg changePwd(String teacherId, String newPwd)
    {
        ReturnMsg returnMsg = new ReturnMsg();
        boolean isSuccess = true;
        String message = "";

        ContentValues values = new ContentValues();
        values.put("password", newPwd);

        db.update("teacher", values, "teacherId = ?", new String[]{teacherId});

        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return returnMsg;
    }
    //    public static final String CREATE_TEACHER_TABLE = "CREATE TABLE IF NOT EXISTS teacher("
//            + "teacherId integer primary key, "
//            + "teacherName text, "
//            + "password text, "
//            + "email text)";
    public List<Teacher> queryProfile(String teacherId) {
        List<Teacher> res = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from teacher where teacherId = ?", new String[]{teacherId});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Teacher teacher = new Teacher();
                teacher.setName(cursor.getString(cursor.getColumnIndex("teacherName")));
                teacher.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                res.add(teacher);
            } while (cursor.moveToNext());
        }
        return res;

    }

    public ReturnMsg changeProfile(String teacherName, String teacherId, String email) {
        ReturnMsg returnMsg = new ReturnMsg();
        boolean isSuccess = true;
        String message = "";

        ContentValues contentValues = new ContentValues();
        contentValues.put("teacherName", teacherName);
        contentValues.put("email", email);
        db.update("teacher", contentValues, "teacherId = ?", new String[]{teacherId});
        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return returnMsg;
    }

    public ReturnMsg addStudent(String studentId, String studentName)
    {
        ReturnMsg returnMsg = new ReturnMsg();
        boolean isSuccess = true;
        String message = "";

        Cursor cursor = db.rawQuery("select * from student where studentId = ?", new String[]{studentId});
        if (cursor != null && cursor.moveToFirst()) {
            message = "Student already exist";
            isSuccess = false;
        }
        else {

            ContentValues contentValues = new ContentValues();
            contentValues.put("studentId", studentId);
            contentValues.put("studentName", studentName);
            db.insert("student", null, contentValues);
            message = "new record added";
        }

        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return returnMsg;
    }




    public ReturnMsg addRecord(String studentId, String classId, String status)
    {
        ReturnMsg returnMsg = new ReturnMsg();
        boolean isSuccess = true;
        String message = "";

        ContentValues contentValues = new ContentValues();
        contentValues.put("studentId", studentId);
        contentValues.put("classId", classId);
        contentValues.put("status", status);
        db.insert("record", null, contentValues);
        message = "new record added";

        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return returnMsg;
    }

    public List<Record> queryRecord(String classId)
    {
        List<Record> records = new ArrayList<>();

        Cursor cursor = db.rawQuery("select record.recordId,record.status,student.studentId,student.studentName,class.startTime  " +
                "from record,class,student where class.classId = record.classId and class.classId = ? " +
                "and record.studentId = student.studentId", new String[]{classId});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Record record = new Record();
                record.setRecordID(Long.valueOf(cursor.getString(cursor.getColumnIndex("recordId"))));
                record.setStatus(cursor.getString(cursor.getColumnIndex("status")));
                record.setSignInTime(Class.StrToDate(cursor.getString(cursor.getColumnIndex("startTime"))));
                Student student = new Student();
                student.setStudentID(cursor.getString(cursor.getColumnIndex("studentId")));
                student.setStudentName(cursor.getString(cursor.getColumnIndex("studentName")));
                record.setRegisteredStudent(student);
                records.add(record);
            } while (cursor.moveToNext());
        }
        return records;
    }


    public ReturnMsg saveRecords(List<Record> records)
    {
        ReturnMsg returnMsg = new ReturnMsg();
        boolean isSuccess = true;
        String message = "";

        for(int i=0;i<records.size();i++)
        {
            Record record = records.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put("status", record.getStatus());
            db.update("record", contentValues, "recordId = ?", new String[]{String.valueOf(record.getRecordID())});
        }

        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return returnMsg;
    }

    public ReturnMsg deleteRecord(String recordId) {
        ReturnMsg returnMsg = new ReturnMsg();
        boolean isSuccess = true;
        String message = "";
        int res = db.delete("record", "recordId = ?", new String[]{recordId});
        if (res == 0) {
            isSuccess = false;
            message = "The record does't exist.";
        } else {
            message = "record deleted";
        }
        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return returnMsg;
    }

    public ReturnMsg modifyStudent(String studentId,String studentName)
    {
        ReturnMsg returnMsg = new ReturnMsg();
        boolean isSuccess = true;
        String message = "";

        ContentValues contentValues = new ContentValues();
        contentValues.put("studentName", studentName);
        db.update("student", contentValues, "studentId = ?", new String[]{studentId});

        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return returnMsg;
    }
    public ReturnMsg modifyRecordAnyway(String studentId,String recordId, String studentName)
    {
        ReturnMsg returnMsg = new ReturnMsg();
        boolean isSuccess = true;
        String message = "";

        modifyStudent(studentId, studentName);
        //update record table
        ContentValues contentValues = new ContentValues();
        contentValues.put("studentId", studentId);
        db.update("record", contentValues, "recordId = ?", new String[]{recordId});


        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return returnMsg;
    }


    public ReturnMsg modifyRecord(String newStudentId, String oldStudentId, String recordId, String studentName)
    {
        ReturnMsg returnMsg = new ReturnMsg();
        boolean isSuccess = true;
        String message = "";

        //add a new student
        if(!newStudentId.equals(oldStudentId))
        {
            //judge whether newStudentId exist
            Cursor cursor = db.rawQuery("select * from student where studentId = ?", new String[]{newStudentId});
            if (cursor != null && cursor.moveToFirst()) {
                message = "exist";
                isSuccess = false;
            }
            else {
                //add student to Student table
                addStudent(newStudentId, studentName);
                //update record table
                ContentValues contentValues = new ContentValues();
                contentValues.put("studentId", newStudentId);
                db.update("record", contentValues, "recordId = ?", new String[]{recordId});
            }
        }
        else//modify existing student's name
        {
            //modify student table
            modifyStudent(newStudentId,studentName);
        }

        returnMsg.setMessage(message);
        returnMsg.setSuccess(isSuccess);
        return returnMsg;
    }
}









