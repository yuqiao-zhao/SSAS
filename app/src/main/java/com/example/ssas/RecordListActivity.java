package com.example.ssas;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import jxl.read.biff.BiffException;
import jxl.write.WriteException;

public class RecordListActivity extends AppCompatActivity implements View.OnClickListener{
    private List<Record> recordList = new ArrayList<>();
    private RecyclerView recyclerView = null;
    private RecordAdapter adapter=null;
    private static Class currentClass;
    private static String signInTime;
    private static String courseID;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);
        getRecordList();
        bind();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Records");
        actionBar.setDisplayHomeAsUpEnabled(true);
        if(recordList.size() == 0)
        {
            establishDefaultRecords();
        }
    }

    public void establishDefaultRecords()
    {
        Boolean hasClassBefore = MainActivity.database.hasClassBefore(courseID,currentClass.getClassId());
        if(hasClassBefore)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);//通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
            builder.setTitle("");//设置Title的内容
            builder.setMessage("This course has class before, do you want to use the student list created last time?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.database.createRecordsDefault(currentClass.getClassId(),courseID);
                    getRecordList();
                    adapter = new RecordAdapter(recordList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }

            });
            builder.setNegativeButton("No", null);
            builder.create().show();

        }
    }

    //Back to the father activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                returnHome(this);
                this.finish();
                return true;
            case R.id.more_item:
                final Course course = MainActivity.database.queryCourseById(courseID);
                //TODO:
                final Export export = new Export();
                try {
                    export.exportRecords(course.getCourseName(), course.getSemester(), currentClass.getStartTime(), recordList);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                } catch (BiffException e) {
                    e.printStackTrace();
                }
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            GMailSender sender = new GMailSender("jbddyyh2819@gmail.com",
                                    "www1234com");
                            sender.addAttachment(export.getFilePath(), export.getFileName());
                            sender.sendMail("The attendance information from SSAS", "The attachment is the attendance record of class held on: " + currentClass.getStartTimeInString() + " in course: " + course.getCourseName() + ".",
                                    "jbddyyh2819@gmail.com",MainActivity.user.getEmail());
                            //Toast.makeText(view.getContext(),"The verification code was sent!", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }).start();

                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);//通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                builder2.setMessage("The records were send to your email.");
                builder2.setPositiveButton("OK",null);
                builder2.create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void returnHome(Context context) {
        Intent intent = new Intent(context, ClassListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 绑定控件
     */
    private void bind()
    {
        toolbar = (Toolbar) findViewById(R.id.record_toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.record_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecordAdapter(recordList);
        recyclerView.setAdapter(adapter);

        View addRecord = (Button)findViewById(R.id.add_new_record);
        addRecord.setOnClickListener(this);
        View saveRecord = (Button)findViewById(R.id.save_record);
        saveRecord.setOnClickListener(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    /**
     * 注册监听事件
     * */
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.add_new_record:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);//通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                final AlertDialog dialog = builder.create();
                View dialogView = View.inflate(this, R.layout.add_record, null);
                dialog.setView(dialogView);
                dialog.setTitle("Please enter a new record");
                dialog.show();

                final EditText editStudentName = (EditText)dialogView.findViewById(R.id.studentName_edit);
                final EditText editStudentID = (EditText)dialogView.findViewById(R.id.studentID_edit);
                Button confirm = (Button)dialogView.findViewById(R.id.confirm);
                Button cancel = (Button)dialogView.findViewById(R.id.cancel);

                confirm.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        String studentName = editStudentName.getText().toString();
                        String studentID = editStudentID.getText().toString();
                        Log.d("StudentName", studentName);
                        Log.d("StudentID", studentID);
                        if (!TextUtils.isEmpty(studentName) && !TextUtils.isEmpty(studentID))
                        {
                            Record newRecord = new Record();
                            Student student = new Student();
                            student.setStudentName(studentName);
                            student.setStudentID(studentID);
                            newRecord.setRegisteredStudent(student);
                            newRecord.setSignInTime(currentClass.getStartTime());
                            newRecord.setTeacherID(MainActivity.user.getId());
                            MainActivity.database.addStudent(studentID,studentName);
                            MainActivity.database.addRecord(studentID,currentClass.getClassId(),newRecord.getStatus());
                            getRecordList();
                            adapter = new RecordAdapter(recordList);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(RecordListActivity.this, "Student name and id cannot be empty.", Toast.LENGTH_SHORT).show();

                        }
                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.save_record:
                MainActivity.database.saveRecords(recordList);
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);//通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                builder2.setTitle("Save success!");//设置Title的内容

                builder2.setPositiveButton("OK",null);
                builder2.create().show();
                break;
        }
    }

    /**
     * 启动activity
     */
    public static void actionStart(Context context, Class c,String courseId)
    {
        currentClass = c;
        courseID = courseId;
        Intent intent = new Intent(context, RecordListActivity.class);
        context.startActivity(intent);
    }

    private void getRecordList()
    {
        recordList = MainActivity.database.queryRecord(currentClass.getClassId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

}
