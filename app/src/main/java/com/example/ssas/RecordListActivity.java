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
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecordListActivity extends AppCompatActivity implements View.OnClickListener{
    private List<Record> recordList = new ArrayList<>();
    private RecyclerView recyclerView = null;
    private RecordAdapter adapter=null;
    private static Class currentClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getRecordList();
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
        Intent intent = new Intent(context, ClassListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 绑定控件
     */
    private void bind()
    {
        recyclerView = (RecyclerView) findViewById(R.id.record_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecordAdapter(recordList);
        recyclerView.setAdapter(adapter);

        View addRecord = (Button)findViewById(R.id.add_new_record);
        addRecord.setOnClickListener(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    /**
     * 注册监听事件
     * */
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.add_new_record:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);//通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                final AlertDialog dialog = builder.create();
                View dialogView = View.inflate(this, R.layout.add_record, null);
                dialog.setView(dialogView);
                dialog.setTitle("Please enter a new record");
                dialog.show();

                final EditText editStudentName = (EditText)dialogView.findViewById(R.id.studentName_edit);
                final EditText editStudentID = (EditText)dialogView.findViewById(R.id.studentID_edit);
                Button confirm = (Button)dialogView.findViewById(R.id.confirm);
                Button cancel = (Button)dialogView.findViewById(R.id.cancel);

                confirm.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        String studentName = editStudentName.getText().toString();
                        String studentID = editStudentID.getText().toString();
                        if (!TextUtils.isEmpty(studentName) && !TextUtils.isEmpty(studentID))
                        {
                            Record newRecord = new Record();
                            Student student = new Student();
                            student.setStudentName(studentName);
                            student.setStudentID(studentID);
                            newRecord.setRegisteredStudent(student);
                            newRecord.setSignInTime(currentClass.getStartTime());
                            recordList.add(newRecord);
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(RecordListActivity.this, "Student name and id cannot be empty.", Toast.LENGTH_SHORT).show();

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
            case R.id.save_record:
                //TODO: save record list to database
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);//通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                builder2.setTitle("Save success!");//设置Title的内容

                builder2.setPositiveButton("OK",null);
                builder2.create().show();
        }
    }

    /**
     * 启动activity
     */
    public static void actionStart(Context context, Class c)
    {
        currentClass = c;
        Intent intent = new Intent(context, RecordListActivity.class);
        context.startActivity(intent);
    }

    //TODO: get info from datebase
    private void getRecordList() {
        recordList = new ArrayList<>();
    }
}
