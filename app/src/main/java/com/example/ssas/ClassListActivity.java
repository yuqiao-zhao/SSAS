package com.example.ssas;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.drm.DrmStore;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jxl.read.biff.BiffException;
import jxl.write.WriteException;

public class ClassListActivity extends AppCompatActivity implements View.OnClickListener{
    public ClassListActivity classListActivity;
    private List<Class> classList = new ArrayList<>();
    private RecyclerView recyclerView = null;
    private static Course course;
    private ClassAdapter adapter = null;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        getClassList();
        bind();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Classes");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public ClassListActivity()
    {
        classListActivity = this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                returnHome(this);
                this.finish();
                return true;
            case R.id.more_item:
                final Export export = new Export();

                for(int i=0;i<classList.size();i++)
                {
                    Class aClass = classList.get(i);
                    List<Record> recordList = MainActivity.database.queryRecord(aClass.getClassId());
                    try {
                        export.exportRecords(course.getCourseName(), course.getSemester(), aClass.getStartTime(), recordList);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (WriteException e) {
                        e.printStackTrace();
                    } catch (BiffException e) {
                        e.printStackTrace();
                    }
                }
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            GMailSender sender = new GMailSender("jbddyyh2819@gmail.com",
                                    "www1234com");
                            sender.addAttachment(export.getFilePath(), export.getFileName());
                            sender.sendMail("The attendance information from SSAS", "The attachment is the all the attendance records of classes " + " in course: " + course.getCourseName() + ".",
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
        Intent intent = new Intent(context, CourseListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 绑定控件
     */
    private void bind()
    {
        toolbar = (Toolbar) findViewById(R.id.class_toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.class_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ClassAdapter(classList);
        adapter.setCourseId(course.getCourseID());
        recyclerView.setAdapter(adapter);

        View addClass = (Button)findViewById(R.id.add_new_class);
        addClass.setOnClickListener(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    public void refreshAdapter()
    {
        ClassAdapter adapter = new ClassAdapter(classList);
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
            case R.id.add_new_class:
                final EditText editText = new EditText(this);
                final View tmpView = v;
                editText.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            showDialogPick((TextView) v);
                            return true;
                        }
                        return false;
                    }
                });
                editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            showDialogPick((TextView) v);
                        }
                    }
                });

                //为TextView设置点击事件
                editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //将timeText传入用于显示所选择的时间
                        showDialogPick((TextView) v);
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(this);//通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                builder.setTitle("Please enter a new class date");//设置Title的内容
                builder.setView(editText);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Class newClass = new Class();
                        //newClass.setStartTime(editText.getText().toString());
                        Toast.makeText(ClassListActivity.this, editText.getText().toString(),Toast.LENGTH_SHORT).show();
                        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
                        Date date = null;
                        try
                        {
                            date = sdf.parse(editText.getText().toString() + ":00");
                            newClass.setStartTime(date);
                            MainActivity.database.addClass(newClass.getStartTimeInString(),course.getCourseID());
                            getClassList();
                            adapter = new ClassAdapter(classList);
                            adapter.setCourseId(course.getCourseID());
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } catch (ParseException e)
                        {
                            e.printStackTrace();
                            Toast.makeText(tmpView.getContext(),"The date is invalid.",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.setNegativeButton("Cancel",null);
                builder.create().show();
                break;

        }
    }

    //将两个选择时间的dialog放在该函数中
    private void showDialogPick(final TextView timeText) {
        final StringBuffer time = new StringBuffer();
        //获取Calendar对象，用于获取当前时间
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        //实例化TimePickerDialog对象
        final TimePickerDialog timePickerDialog = new TimePickerDialog(ClassListActivity.this, new TimePickerDialog.OnTimeSetListener() {
            //选择完时间后会调用该回调函数
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time.append(" "  + hourOfDay + ":" + minute);
                //设置TextView显示最终选择的时间
                timeText.setText(time);
            }
        }, hour, minute, true);
        //实例化DatePickerDialog对象
        DatePickerDialog datePickerDialog = new DatePickerDialog(ClassListActivity.this, new DatePickerDialog.OnDateSetListener() {
            //选择完日期后会调用该回调函数
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //因为monthOfYear会比实际月份少一月所以这边要加1
                time.append(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                //选择完日期后弹出选择时间对话框
                timePickerDialog.show();
            }
        }, year, month, day);
        //弹出选择日期对话框
        datePickerDialog.show();
    }

    /**
     * 启动activity
     */
    public static void actionStart(Context context, Course c)
    {
        course = c;
        Intent intent = new Intent(context, ClassListActivity.class);
        context.startActivity(intent);
    }

    private void getClassList() {
        classList = MainActivity.database.queryClasses(course.getCourseID());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu,menu);
        MenuItem item = menu.getItem(0);
        item.setTitle("Export records from all classes");
        return super.onCreateOptionsMenu(menu);
    }
}
