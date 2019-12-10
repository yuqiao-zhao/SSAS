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

public class AttendanceTableAdapter extends RecyclerView.Adapter<AttendanceTableAdapter.ViewHolder>{
    List<AttendanceTable> attendanceTables = new ArrayList<AttendanceTable>();

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

    public AttendanceTableAdapter(List<AttendanceTable> resultList)
    {
        attendanceTables = resultList;
    }

    @NonNull
    @Override
    public AttendanceTableAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.presence_status, parent, false);
        final AttendanceTableAdapter.ViewHolder holder = new AttendanceTableAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceTableAdapter.ViewHolder holder, int position) {
        AttendanceTable res = attendanceTables.get(position);
        holder.Date.setText("course Date : " + res.getStartTimeInString());
        holder.Status.setText("status : " + res.getStatus());

    }



    @Override
    public int getItemCount() {
        return attendanceTables.size();
    }
}
