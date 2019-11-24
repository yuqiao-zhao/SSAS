package com.example.ssas;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    private List<Course> mCourseList;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView courseName;
        TextView semester;
        Button deleteCourse;

        public ViewHolder(View view)
        {
            super(view);
            courseName = (TextView) view.findViewById(R.id.course_name);
            deleteCourse = (Button) view.findViewById(R.id.delete_course);
            semester = (TextView) view.findViewById(R.id.semester);
        }
    }

    public CourseAdapter(List<Course> courseList)
    {
        mCourseList=courseList;
    }

    public CourseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
        final CourseAdapter.ViewHolder holder = new CourseAdapter.ViewHolder(view);
        holder.courseName.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        int position = holder.getAdapterPosition();
                        Course course = mCourseList.get(position);
                        Toast.makeText(view.getContext(), course.getCourseName(),Toast.LENGTH_SHORT).show();
                        //TODO: get course list from database
                    }
                }
        );
        holder.deleteCourse.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        final int position = holder.getAdapterPosition();
                        Course course = mCourseList.get(position);

                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());//通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                        builder.setTitle("Do you want to delete the course? All the information in this course will be deleted.");//设置Title的内容
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                //TODO: delete course from database
                                mCourseList.remove(position);
                                notifyDataSetChanged();
                            }

                        });
                        builder.setNegativeButton("Cancel",null);
                        builder.create().show();

                    }
                }
        );
        return holder;

    }

    public void onBindViewHolder(CourseAdapter.ViewHolder holder, int position)
    {
        Course course= mCourseList.get(position);
        holder.courseName.setText(course.getCourseName());
        holder.semester.setText(course.getSemester());
    }

    @Override
    public int getItemCount()
    {
        return mCourseList.size();
    }
}