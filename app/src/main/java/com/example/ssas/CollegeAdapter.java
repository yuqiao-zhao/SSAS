package com.example.ssas;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CollegeAdapter extends RecyclerView.Adapter<CollegeAdapter.ViewHolder> {
    private List<University> mCollegeList;
    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView collegeName;
        Button deleteCollege;

        public ViewHolder(View view)
        {
            super(view);
            collegeName = (TextView) view.findViewById(R.id.college_name);
            deleteCollege = (Button)view.findViewById(R.id.delete_college);
        }
    }

    public CollegeAdapter(List<University> collegeList)
    {
        mCollegeList=collegeList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.college_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.collegeName.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        int position = holder.getAdapterPosition();
                        University university = mCollegeList.get(position);
                        Toast.makeText(view.getContext(), university.getUniversityName(),Toast.LENGTH_SHORT).show();

                        CourseListActivity.actionStart(view.getContext(),university);
                    }
                }
        );
        holder.deleteCollege.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        final int position = holder.getAdapterPosition();
                        final University university = mCollegeList.get(position);

                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());//通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                        builder.setTitle("Do you want to delete the college? All the information in this college will be deleted.");//设置Title的内容
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                //TODO: delete course from database
                                MainActivity.database.deleteUniversity(university.getUniversityId());
                                mCollegeList.remove(position);
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

    public void onBindViewHolder(ViewHolder holder, int position)
    {
        String collegeName= mCollegeList.get(position).getUniversityName();
        holder.collegeName.setText(collegeName);
    }


    @Override
    public int getItemCount()
    {
        return mCollegeList.size();
    }
}
