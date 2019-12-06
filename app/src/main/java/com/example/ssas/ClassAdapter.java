package com.example.ssas;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {
    private List<Class> mClassList;
    private String courseId;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView classStartTime;
        TextView studentNum;
        Button deleteClass;
        Button modifyClass;

        public ViewHolder(View view)
        {
            super(view);
            classStartTime = (TextView) view.findViewById(R.id.class_date);
            studentNum = (TextView) view.findViewById(R.id.student_num);
            deleteClass = (Button) view.findViewById(R.id.delete_class);
            modifyClass = (Button) view.findViewById(R.id.modify_class);
        }
    }

    public ClassAdapter(List<Class> classList)
    {
        mClassList=classList;
    }

    public ClassAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item, parent, false);
        final ClassAdapter.ViewHolder holder = new ClassAdapter.ViewHolder(view);
        holder.classStartTime.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        int position = holder.getAdapterPosition();
                        Class aClass = mClassList.get(position);
                        Toast.makeText(view.getContext(), aClass.getStartTime().toString(),Toast.LENGTH_SHORT).show();

                        RecordListActivity.actionStart(view.getContext(),aClass,courseId);
                    }
                }
        );
        holder.deleteClass.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        final int position = holder.getAdapterPosition();
                        final Class aClass = mClassList.get(position);

                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());//通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                        builder.setTitle("WARNING");//设置Title的内容
                        builder.setMessage("Do you want to delete the class? All the information in this class will be deleted.");
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {

                                MainActivity.database.deleteClass(aClass.getClassId());
                                mClassList.remove(position);
                                notifyDataSetChanged();
                            }

                        });
                        builder.setNegativeButton("Cancel",null);
                        builder.create().show();

                    }
                }
        );
        holder.modifyClass.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        final int position = holder.getAdapterPosition();
                        final Class aClass = mClassList.get(position);

                        final EditText editText = new EditText(view.getContext());
                        editText.setOnTouchListener(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                    showDialogPick((TextView) v, view.getContext());
                                    return true;
                                }
                                return false;
                            }
                        });
                        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {
                                    showDialogPick((TextView) v, view.getContext());
                                }
                            }
                        });

                        //为TextView设置点击事件
                        editText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //将timeText传入用于显示所选择的时间
                                showDialogPick((TextView) v, view.getContext());
                            }
                        });

                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());//通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                        builder.setTitle("Please select a new class date");//设置Title的内容
                        builder.setView(editText);
                        builder.setPositiveButton("Modify", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                //newClass.setStartTime(editText.getText().toString());
                                Toast.makeText(view.getContext(), editText.getText().toString(),Toast.LENGTH_SHORT).show();
                                SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
                                Date date = null;
                                try
                                {
                                    date = sdf.parse(editText.getText().toString() + ":00");
                                    aClass.setStartTime(date);
                                    MainActivity.database.modifyClass(aClass.getClassId(),aClass.getStartTimeInString());
                                    notifyDataSetChanged();
                                } catch (ParseException e)
                                {
                                    e.printStackTrace();
                                    Toast.makeText(view.getContext(),"The date is invalid.",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.setNegativeButton("Cancel",null);
                        builder.create().show();

                    }
                }
        );

        return holder;

    }

    //将两个选择时间的dialog放在该函数中
    private void showDialogPick(final TextView timeText, Context v) {
        final StringBuffer time = new StringBuffer();
        //获取Calendar对象，用于获取当前时间
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        //实例化TimePickerDialog对象
        final TimePickerDialog timePickerDialog = new TimePickerDialog(v, new TimePickerDialog.OnTimeSetListener() {
            //选择完时间后会调用该回调函数
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time.append(" "  + hourOfDay + ":" + minute);
                //设置TextView显示最终选择的时间
                timeText.setText(time);
            }
        }, hour, minute, true);
        //实例化DatePickerDialog对象
        DatePickerDialog datePickerDialog = new DatePickerDialog(v, new DatePickerDialog.OnDateSetListener() {
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

    public void onBindViewHolder(ClassAdapter.ViewHolder holder, int position)
    {
        Class aClass = mClassList.get(position);
        holder.classStartTime.setText(aClass.getStartTimeInString());
        holder.studentNum.setText(aClass.getRecords().size() + " students");
    }

    @Override
    public int getItemCount()
    {
        return mClassList.size();
    }
}