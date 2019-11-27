package com.example.ssas;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private List<Record> mRecordList;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView studentName;
        TextView studentID;
        Button deleteRecord;
        RadioGroup attendance;

        public ViewHolder(View view)
        {
            super(view);
            studentName = (TextView) view.findViewById(R.id.student_name);
            studentID = (TextView) view.findViewById(R.id.student_id);
            deleteRecord = (Button) view.findViewById(R.id.delete_record);
            attendance = (RadioGroup) view.findViewById(R.id.attendance);
        }
    }

    public RecordAdapter(List<Record> recordList)
    {
        mRecordList=recordList;
    }

    public RecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false);
        final RecordAdapter.ViewHolder holder = new RecordAdapter.ViewHolder(view);

        holder.attendance.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                final int position = holder.getAdapterPosition();
                Record record = mRecordList.get(position);
                RadioButton radbtn = (RadioButton) view.findViewById(checkedId);
                record.setStatus(radbtn.getText().toString());
            }
        });
        holder.deleteRecord.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        final int position = holder.getAdapterPosition();
                        Record record = mRecordList.get(position);

                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());//通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                        builder.setTitle("Do you want to delete this record? All the information in this record will be deleted.");//设置Title的内容
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                //TODO: delete course from database
                                mRecordList.remove(position);
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

    public void onBindViewHolder(RecordAdapter.ViewHolder holder, int position)
    {
        Record record = mRecordList.get(position);
        holder.studentName.setText(record.getRegisteredStudent().getStudentName());
        holder.studentID.setText("ID: " + record.getRegisteredStudent().getStudentID());
    }

    @Override
    public int getItemCount()
    {
        return mRecordList.size();
    }
}