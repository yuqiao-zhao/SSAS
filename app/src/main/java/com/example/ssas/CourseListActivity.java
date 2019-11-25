package com.example.ssas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class CourseListActivity extends AppCompatActivity implements View.OnClickListener{
    private List<Course> courseList = new ArrayList<>();
    private RecyclerView recyclerView = null;
    private static String collegeName;
    private CourseAdapter adapter=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getCourseList();
        bind();
    }

    //Back to the father activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                returnHome(this);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void returnHome(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 绑定控件
     */
    private void bind()
    {
        recyclerView = (RecyclerView) findViewById(R.id.course_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CourseAdapter(courseList);
        recyclerView.setAdapter(adapter);

        View addCourse = (Button)findViewById(R.id.add_new_course);
        addCourse.setOnClickListener(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    public void refreshAdapter()
    {
        CourseAdapter adapter = new CourseAdapter(courseList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    /**
     * 注册监听事件
     * */
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.add_new_course:
                final EditText editText = new EditText(this);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);//通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                builder.setTitle("Please enter a new course");//设置Title的内容
                builder.setView(editText);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //TODO: change the database and refresh the page
                        Course newCourse = new Course();
                        newCourse.setCourseName(editText.getText().toString());
                        courseList.add(newCourse);
                        adapter.notifyDataSetChanged();

                    }
                });
                builder.setNegativeButton("Cancel",null);
                builder.create().show();
                break;

        }
    }

    /**
     * 启动activity
     */
    public static void actionStart(Context context, String name)
    {
        collegeName = name;
        Intent intent = new Intent(context, CourseListActivity.class);
        context.startActivity(intent);
    }

    //TODO: get info from datebase
    private void getCourseList() {
        courseList = new ArrayList<>();
        Course course1=new Course();
        Course course2=new Course();
        course1.setCourseID("111");
        course1.setCourseName("Software Engineering");
        course1.setSemester("2019 Fall");

        course2.setCourseID("222");
        course2.setCourseName("Cyber Security");
        course2.setSemester("2019 Spring");

        courseList.add(course1);
        courseList.add(course2);

    }
}
