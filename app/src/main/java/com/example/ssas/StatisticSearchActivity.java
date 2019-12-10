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
    private List<StatisticResultBrief> resultBriefList = new ArrayList<>();
    private static String studentId;
    private static String courseName;
    private RecyclerView recyclerView;
    private StatisticResultAdapter adapter;


    public StatisticSearchActivity() {
        statisticSearchActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchResult();
        setContentView(R.layout.statistic_result);
        bind();
    }


    private void bind() {

            recyclerView = (RecyclerView) findViewById(R.id.Statistic_recycler_view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new StatisticResultAdapter(resultBriefList);
            recyclerView.setAdapter(adapter);

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



    private void searchResult() {

        resultBriefList = MainActivity.database.queryStatisticResultBrief(courseName, studentId);

    }

    @Override
    public void onClick(View view) {

    }
}
