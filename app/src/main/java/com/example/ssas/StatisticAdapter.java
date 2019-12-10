package com.example.ssas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticAdapter extends RecyclerView.Adapter<StatisticAdapter.ViewHolder>{

    ArrayList<ArrayList<StatisticResult>> groupedList = new ArrayList<ArrayList<StatisticResult>>();


    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView studentName;
        TextView studentID;
        TextView courseName;
        TextView semester;
        TextView presence;
        TextView totalClass;

        public ViewHolder(View view)
        {
            super(view);
            studentName = (TextView) view.findViewById(R.id.studentName_Statistic);
            studentID = (TextView) view.findViewById(R.id.studentID_Statistic);
            courseName = (TextView) view.findViewById(R.id.courseName_Statistic);
            semester = (TextView) view.findViewById(R.id.semester_Statistic);
            presence = (TextView) view.findViewById(R.id.presence_Statistic);
            totalClass = (TextView) view.findViewById(R.id.totalClass_Statistic);
        }

        public TextView getSemester(){
            return semester;
        }

    }

    public StatisticAdapter(List<StatisticResult> resultList)
    {
        Map<String, ArrayList<StatisticResult>> map = new HashMap<String, ArrayList<StatisticResult>>();
        for (StatisticResult res: resultList) {
            String key  = res.getSemester();
            if(map.containsKey(key)){
                List<StatisticResult> list = map.get(key);
                list.add(res);

            }else{
                List<StatisticResult> list = new ArrayList<StatisticResult>();
                list.add(res);
                map.put(key, (ArrayList<StatisticResult>) list);
            }

        }

        for (String key : map.keySet()){
            groupedList.add(map.get(key));
        }
    }

    @NonNull
    @Override
    public StatisticAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.statistic_item, parent, false);
        final StatisticAdapter.ViewHolder holder = new StatisticAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StatisticAdapter.ViewHolder holder, int position) {
        List<StatisticResult> res = groupedList.get(position);
        int attendanceCount = 0;
        int classCount;
        holder.studentName.setText(res.get(0).getStudentName());
        holder.studentID.setText("ID: " + res.get(0).getStudentId());
        holder.courseName.setText(res.get(0).getCourseName());
        holder.semester.setText(res.get(0).getSemester());
        classCount = res.size();
        for(StatisticResult sr : res){
            if(sr.getStatus().equals("Present"))
                attendanceCount += 1;
        }
        holder.presence.setText("attendance : " + Integer.toString(attendanceCount));
        holder.totalClass.setText("total class : " + Integer.toString(classCount));


    }


    @Override
    public int getItemCount() {
        return groupedList.size();
    }
}
