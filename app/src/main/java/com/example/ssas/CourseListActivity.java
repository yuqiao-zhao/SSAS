package com.example.ssas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CourseListActivity extends AppCompatActivity implements View.OnClickListener{
    private List<Course> courseList = new ArrayList<>();
    private RecyclerView recyclerView = null;
    private static University college;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);//通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                final AlertDialog dialog = builder.create();
                View dialogView = View.inflate(this, R.layout.add_course, null);
                dialog.setView(dialogView);
                dialog.setTitle("Please enter a new course");
                dialog.show();

                final EditText editCourseName = (EditText)dialogView.findViewById(R.id.courseName_new);
                final EditText editSemester = (EditText)dialogView.findViewById(R.id.semester_new);
                Button confirm = (Button)dialogView.findViewById(R.id.confirm_new_course);
                Button cancel = (Button)dialogView.findViewById(R.id.cancel_new_course);

                confirm.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        String courseName = editCourseName.getText().toString();
                        String semester = editSemester.getText().toString();
                        if (!TextUtils.isEmpty(courseName) && !TextUtils.isEmpty(semester))
                        {
                            Course newCourse = new Course();
                            newCourse.setCourseName(courseName);
                            newCourse.setSemester(semester);

                            MainActivity.database.addCourse(MainActivity.user.getId(),courseName, college.getUniversityId(),semester);

                            getCourseList();
                            adapter = new CourseAdapter(courseList);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(CourseListActivity.this, "Course name and semester cannot be empty.", Toast.LENGTH_SHORT).show();

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

        }
    }

    /**
     * 启动activity
     */
    public static void actionStart(Context context, University university)
    {
        college = university;
        Intent intent = new Intent(context, CourseListActivity.class);
        context.startActivity(intent);
    }

    private void getCourseList() {
        courseList = MainActivity.database.queryCourses(MainActivity.user.getId(),college.getUniversityId());

    }
}
