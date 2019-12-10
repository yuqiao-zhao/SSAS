package com.example.ssas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StatisticResultAdapter extends RecyclerView.Adapter<StatisticResultAdapter.ViewHolder>{
    List<StatisticResultBrief> results = new ArrayList<>();
    private AttendanceTableAdapter adapter;


    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView studentName;
        TextView studentID;
        TextView courseName;
        TextView semester;
        TextView presence;
        TextView totalClass;
        RecyclerView recyclerView;

        public ViewHolder(View view)
        {
            super(view);
            studentName = (TextView) view.findViewById(R.id.studentName_Statistic);
            studentID = (TextView) view.findViewById(R.id.studentID_Statistic);
            courseName = (TextView) view.findViewById(R.id.courseName_Statistic);
            semester = (TextView) view.findViewById(R.id.semester_Statistic);
            presence = (TextView) view.findViewById(R.id.presence_Statistic);
            totalClass = (TextView) view.findViewById(R.id.totalClass_Statistic);
            recyclerView = (RecyclerView) view.findViewById(R.id.presenceStatus_recycler_view);
        }

    }

    public StatisticResultAdapter(List<StatisticResultBrief> r)
    {
        results = r;
    }

    @NonNull
    @Override
    public StatisticResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.statistic_item, parent, false);
        final StatisticResultAdapter.ViewHolder holder = new StatisticResultAdapter.ViewHolder(view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        holder.recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(holder.recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        holder.recyclerView.addItemDecoration(dividerItemDecoration);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StatisticResultAdapter.ViewHolder holder, int position) {

        StatisticResultBrief res = results.get(position);

        adapter = new AttendanceTableAdapter(res.getAttendanceTables());
        holder.recyclerView.setAdapter(adapter);

        int attendanceCount = 0;
        int classCount;
        holder.studentName.setText(res.getStudentName());
        holder.studentID.setText("ID: " + res.getStudentId());
        holder.courseName.setText(res.getCourseName());
        holder.semester.setText(res.getSemester());
        classCount = res.getAttendanceTables().size();
        for(AttendanceTable a:res.getAttendanceTables())
        {
            if(a.getStatus().equals("Present"))
                attendanceCount += 1;
        }
        holder.presence.setText("attendance : " + Integer.toString(attendanceCount));
        holder.totalClass.setText("total class : " + Integer.toString(classCount));

    }


    @Override
    public int getItemCount() {
        return results.size();
    }
}
