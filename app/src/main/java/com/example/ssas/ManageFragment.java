package com.example.ssas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ManageFragment extends Fragment implements View.OnClickListener{
    private View view;
    private List<String> universityList = new ArrayList<>();
    private RecyclerView recyclerView = null;
    private CollegeAdapter adapter = null;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_management, container, false);
        getUniversityList();


        bind();
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    public void refreshAdapter()
    {
        CollegeAdapter adapter = new CollegeAdapter(universityList);
        recyclerView.setAdapter(adapter);
    }
    /**
     * 绑定控件
     */
    private void bind()
    {
        recyclerView = view.findViewById(R.id.college_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CollegeAdapter(universityList);
        recyclerView.setAdapter(adapter);

        View addUniversity = (Button)view.findViewById(R.id.join_new_university);
        addUniversity.setOnClickListener(this);
    }


    @Override
    /**
     * 注册监听事件
     * */
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.join_new_university:
                final EditText editText = new EditText(this.getContext());
                AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());//通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                builder.setTitle("Please enter the University's name");//设置Title的内容
                builder.setView(editText);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //TODO: change the database and refresh the page
                        universityList.add(editText.getText().toString());
                        //refreshAdapter();//刷新当前活动
                        adapter.notifyDataSetChanged();

                    }
                });
                builder.setNegativeButton("Cancel",null);
                builder.create().show();
                break;

        }
    }


    //TODO: get info from datebase
    private void getUniversityList() {
        universityList = new ArrayList<>();
        universityList.add("Tufts Unviersity");
        universityList.add("Boston University");
    }
}
