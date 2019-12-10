package com.example.ssas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import java.util.List;

public class StatisticSearchActivity extends AppCompatActivity implements View.OnClickListener{

    public StatisticSearchActivity statisticSearchActivity;
    //private List<StatisticResult> resultList = new ArrayList<>();
    private List<StatisticResultBrief> resultBriefList = new ArrayList<>();
    private static String studentId;
    private static String courseName;
    private TextView semester;
    private RecyclerView recyclerView;
    private RecyclerView childeRecyclerView;
    private StatisticResultAdapter adapter;
    private CalendarAdapter childAdapter;


    public StatisticSearchActivity() {
        statisticSearchActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //searchStudentStatus();
        searchResult();
        setContentView(R.layout.statistic_result);
        bind();
    }


    private void bind() {

//        recyclerView = (RecyclerView) findViewById(R.id.Statistic_recycler_view);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        adapter = new StatisticAdapter(resultList);
//        recyclerView.setAdapter(adapter);

            recyclerView = (RecyclerView) findViewById(R.id.Statistic_recycler_view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new StatisticResultAdapter(resultBriefList);
            recyclerView.setAdapter(adapter);


//        childeRecyclerView = (RecyclerView) findViewById(R.id.presenceStatus_recycler_view);
//        childAdapter = new CalendarAdapter(resultList, semester.toString());
//        recyclerView.setAdapter(adapter);
//        childeRecyclerView.setAdapter(childAdapter);

    }

    /**
     * 启动活动
     */
    public static void actionStart(Context context, String cName, String sId) {
        courseName = cName;
        studentId = sId;
        Intent intent = new Intent(context, StatisticSearchActivity.class);
        context.startActivity(intent);

    }

    @Override
    public void onResume()
    {
        super.onResume();
    }


    private void searchStudentStatus() {

        //resultList = MainActivity.database.queryStatisticResult(courseName, studentId);


    }

    private void searchResult() {

        resultBriefList = MainActivity.database.queryStatisticResultBrief(courseName, studentId);

    }

    @Override
    public void onClick(View view) {

    }
}
