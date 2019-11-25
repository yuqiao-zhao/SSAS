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

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {
    private List<Class> mClassList;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView classStartTime;
        TextView studentNum;
        Button deleteClass;

        public ViewHolder(View view)
        {
            super(view);
            classStartTime = (TextView) view.findViewById(R.id.class_date);
            studentNum = (TextView) view.findViewById(R.id.student_num);
            deleteClass = (Button) view.findViewById(R.id.delete_class);
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
                        //TODO: get course list from database
                        RecordListActivity.actionStart(view.getContext(), aClass);
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
                        Class aClass = mClassList.get(position);

                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());//通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                        builder.setTitle("Do you want to delete the class? All the information in this class will be deleted.");//设置Title的内容
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                //TODO: delete course from database
                                mClassList.remove(position);
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

    public void onBindViewHolder(ClassAdapter.ViewHolder holder, int position)
    {
        Class aClass = mClassList.get(position);
        holder.classStartTime.setText(aClass.getStartTimeInString().toString());
        holder.studentNum.setText(aClass.getRecords().size() + " students");
    }

    @Override
    public int getItemCount()
    {
        return mClassList.size();
    }
}