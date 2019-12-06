package com.example.ssas;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
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
        RadioButton present;
        RadioButton absent;
        Button modifyRecord;

        public ViewHolder(View view)
        {
            super(view);
            studentName = (TextView) view.findViewById(R.id.student_name);
            studentID = (TextView) view.findViewById(R.id.student_id);
            deleteRecord = (Button) view.findViewById(R.id.delete_record);
            attendance = (RadioGroup) view.findViewById(R.id.attendance);
            present = (RadioButton) view.findViewById(R.id.present);
            absent = (RadioButton)view.findViewById(R.id.absent);
            modifyRecord = (Button) view.findViewById(R.id.modify_record);
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
                Log.d("sssssssssssssssssss",record.getStatus());
            }
        });
        holder.deleteRecord.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        final int position = holder.getAdapterPosition();
                        final Record record = mRecordList.get(position);

                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());//通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                        builder.setTitle("WARNING");//设置Title的内容
                        builder.setMessage("Do you want to delete this record? All the information in this record will be deleted.");
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                //TODO: delete course from database
                                MainActivity.database.deleteRecord(String.valueOf(record.getRecordID()));
                                mRecordList.remove(position);
                                notifyDataSetChanged();
                            }

                        });
                        builder.setNegativeButton("Cancel",null);
                        builder.create().show();

                    }
                }
        );
        holder.modifyRecord.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        final int position = holder.getAdapterPosition();
                        final Record record = mRecordList.get(position);
                        final ReturnMsg mess = new ReturnMsg();
                        mess.setSuccess(true);

                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());//通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                        final AlertDialog dialog = builder.create();
                        View dialogView = View.inflate(view.getContext(), R.layout.add_record, null);
                        dialog.setView(dialogView);
                        dialog.setTitle("Please enter the new student Information");
                        dialog.show();

                        final EditText editStudentName = (EditText)dialogView.findViewById(R.id.studentName_edit);
                        final EditText editStudentID = (EditText)dialogView.findViewById(R.id.studentID_edit);
                        editStudentID.setText(record.getRegisteredStudent().getStudentID());
                        editStudentName.setText(record.getRegisteredStudent().getStudentName());

                        Button confirm = (Button)dialogView.findViewById(R.id.confirm);
                        Button cancel = (Button)dialogView.findViewById(R.id.cancel);

                        confirm.setOnClickListener(new View.OnClickListener(){

                            @Override
                            public void onClick(View view) {
                                final String studentName = editStudentName.getText().toString();
                                final String studentID = editStudentID.getText().toString();
                                String oldId = record.getRegisteredStudent().getStudentID();
                                if (!TextUtils.isEmpty(studentName) && !TextUtils.isEmpty(studentID))
                                {
                                    ReturnMsg returnMsg = MainActivity.database.modifyRecord(studentID,oldId,String.valueOf(record.getRecordID()),studentName);
                                    if(!returnMsg.getSuccess())//student's id changed to an exist student
                                    {
                                        mess.setSuccess(false);
                                        boolean exist=false;
                                        //whether the new ID already in the class
                                        for(int i=0;i<mRecordList.size();i++)
                                        {
                                            if(mRecordList.get(i).getRegisteredStudent().getStudentID().equals(studentID))
                                            {
                                                exist = true;
                                                break;
                                            }
                                        }
                                        if(exist)
                                        {
                                            AlertDialog.Builder builder2 = new AlertDialog.Builder(view.getContext());//通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                                            builder2.setTitle("Modify failed");//设置Title的内容
                                            builder2.setMessage("This student"+" StudentID:"+studentID+" has already been in this class.");
                                            builder2.setPositiveButton("OK",null);
                                            builder2.create().show();
                                        }
                                        else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());//通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                                            builder.setTitle("WARNING");//设置Title的内容
                                            builder.setMessage("This student"+" StudentID:"+studentID+" already exist in the database. Do you still want to modify the information? ");
                                            builder.setPositiveButton("Modify", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    MainActivity.database.modifyRecordAnyway(studentID, String.valueOf(record.getRecordID()), studentName);
                                                    mess.setSuccess(true);
                                                    Student newStudent = new Student();
                                                    newStudent.setStudentName(studentName);
                                                    newStudent.setStudentID(studentID);
                                                    record.setRegisteredStudent(newStudent);
                                                    notifyDataSetChanged();
                                                    dialog.dismiss();
                                                }

                                            });
                                            builder.setNegativeButton("Cancel", null);
                                            builder.create().show();
                                        }
                                    }
                                    else {
                                        Student newStudent = new Student();
                                        newStudent.setStudentName(studentName);
                                        newStudent.setStudentID(studentID);
                                        record.setRegisteredStudent(newStudent);
                                        notifyDataSetChanged();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(view.getContext(), "Student name and id cannot be empty.", Toast.LENGTH_SHORT).show();

                                }
                                if (mess.getSuccess())
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
                }
        );
        return holder;

    }

    public void onBindViewHolder(RecordAdapter.ViewHolder holder, int position)
    {
        Record record = mRecordList.get(position);
        holder.studentName.setText(record.getRegisteredStudent().getStudentName());
        holder.studentID.setText("ID: " + record.getRegisteredStudent().getStudentID());
        if(record.getStatus().equals("Present"))
        {
            holder.present.setChecked(true);
            holder.absent.setChecked(false);
        }
        else
        {
            holder.present.setChecked(false);
            holder.absent.setChecked(true);
        }

    }

    @Override
    public int getItemCount()
    {
        return mRecordList.size();
    }
}