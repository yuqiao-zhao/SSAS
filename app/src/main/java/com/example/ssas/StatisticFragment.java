package com.example.ssas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

public class StatisticFragment extends Fragment implements View.OnClickListener{

    private View view;
    private View statisticSearch;
    private EditText studentId;
    private EditText courseName;
    private List<StatisticResult> resultsList;



    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_statistic, container, false);
        bind();

        return view;
    }

    /**
     * 绑定控件
     */
    private void bind() {
        statisticSearch = view.findViewById(R.id.Statistic_search);
        studentId = view.findViewById(R.id.studentId_search);
        courseName = view.findViewById(R.id.courseName_search);

        statisticSearch.setOnClickListener(this);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Statistic_search:
                searchStudentStatus();
                break;

        }
    }

    private void searchStudentStatus() {
        String cName, sId;
        cName = courseName.getText().toString();
        sId = studentId.getText().toString();

        if (cName.length() == 0 || sId.length() == 0)//如果用户没有输入密码或者用户名，则不做任何反馈
        {
            Toast.makeText(view.getContext(), "Please enter student ID and course ID for search.", Toast.LENGTH_SHORT).show();
            return;
        }

        resultsList = MainActivity.database.queryStatisticResult(cName, sId);

        if(!resultsList.isEmpty())
        {
            StatisticSearchActivity.actionStart(view.getContext(), cName, sId);
        }else{
            Toast.makeText(view.getContext(), "No result under such student ID and course ID", Toast.LENGTH_SHORT).show();
            return;
        }

    }
}

