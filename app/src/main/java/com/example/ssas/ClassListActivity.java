package com.example.ssas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ClassListActivity extends AppCompatActivity implements View.OnClickListener{
    private List<Class> classList = new ArrayList<>();
    private RecyclerView recyclerView = null;
    private static Course course;
    private ClassAdapter adapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getClassList();
        bind();
    }

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
        Intent intent = new Intent(context, CourseListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 绑定控件
     */
    private void bind()
    {
        recyclerView = (RecyclerView) findViewById(R.id.class_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ClassAdapter(classList);
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
                        //TODO: change the database and refresh the page
                        Class newClass = new Class();
                        //newClass.setStartTime(editText.getText().toString());
                        Toast.makeText(ClassListActivity.this, editText.getText().toString(),Toast.LENGTH_SHORT).show();
                        Log.d("Date",editText.getText().toString());
                        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
                        Date date = null;
                        try
                        {
                            date = sdf.parse(editText.getText().toString() + ":00");
                        } catch (ParseException e)
                        {
                            e.printStackTrace();
                        }
                        newClass.setStartTime(date);
                        classList.add(newClass);
                        adapter.notifyDataSetChanged();

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

    //TODO: get info from datebase
    private void getClassList() {
        classList = new ArrayList<>();

        Class class1 = new Class();
        Class class2 = new Class();
        Date date1 = new Date();
        class1.setStartTime(date1);
        class2.setStartTime(date1);
        classList.add(class1);
        classList.add(class2);
    }
}
