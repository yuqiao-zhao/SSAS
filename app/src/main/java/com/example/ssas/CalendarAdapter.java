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

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder>{

    List<StatisticResult> calendarList = new ArrayList<StatisticResult>();

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView Date;
        TextView Status;

        public ViewHolder(View view)
        {
            super(view);
            Date = (TextView) view.findViewById(R.id.classDate_Statistic);
            Status = (TextView) view.findViewById(R.id.presenceStatus_Statistic);

        }
    }

    public CalendarAdapter(List<StatisticResult> resultList, String semester)
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

        calendarList = map.get(semester);
    }

    @NonNull
    @Override
    public CalendarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.presence_status, parent, false);
        final CalendarAdapter.ViewHolder holder = new CalendarAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarAdapter.ViewHolder holder, int position) {
        StatisticResult res = calendarList.get(position);
        holder.Date.setText("course Date : " + res.getSignInTime());
        holder.Status.setText("status : " + res.getStatus());

    }



    @Override
    public int getItemCount() {
        return calendarList.size();
    }
}
