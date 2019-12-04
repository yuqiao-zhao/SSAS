package com.example.ssas;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
        Button modifyCourse;

        public ViewHolder(View view)
        {
            super(view);
            courseName = (TextView) view.findViewById(R.id.course_name);
            deleteCourse = (Button) view.findViewById(R.id.delete_course);
            semester = (TextView) view.findViewById(R.id.semester);
            modifyCourse = (Button) view.findViewById(R.id.modify_course);
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
                        ClassListActivity.actionStart(view.getContext(),course);
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
                        final Course course = mCourseList.get(position);

                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());//通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                        builder.setTitle("Do you want to delete the course? All the information in this course will be deleted.");//设置Title的内容
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                MainActivity.database.deleteCourse(course.getCourseID());
                                mCourseList.remove(position);
                                notifyDataSetChanged();
                            }

                        });
                        builder.setNegativeButton("Cancel",null);
                        builder.create().show();

                    }
                }
        );

        holder.modifyCourse.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        final int position = holder.getAdapterPosition();
                        final Course course = mCourseList.get(position);

                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());//通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                        final AlertDialog dialog = builder.create();
                        View dialogView = View.inflate(view.getContext(), R.layout.add_course, null);
                        dialog.setView(dialogView);
                        dialog.setTitle("Please enter course information");
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
                                    MainActivity.database.modifyCourse(course.getCourseID(),courseName,semester);
                                    course.setCourseName(courseName);
                                    course.setSemester(semester);

                                    notifyDataSetChanged();
                                }
                                else
                                {
                                    Toast.makeText(view.getContext(), "Course name and semester cannot be empty.", Toast.LENGTH_SHORT).show();

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

                    }
                });

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