package com.example.ssas;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.teprinciple.mailsender.Mail;
import com.teprinciple.mailsender.MailSender;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InformationFragment extends Fragment implements View.OnClickListener{
    private View view;
    private EditText teacherName;
    private EditText email;
    private View saveProfile;
    private View resetPwd;
    private View logout;
    public static int verifyCode;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_personal_information, container, false);
        bind();
        initInfo();
        return view;
    }

    @Override
    public void onResume()
    {
        initInfo();
        super.onResume();
    }

    /**
     * 绑定控件
     */
    private void bind()
    {
        teacherName = (EditText)view.findViewById(R.id.name_display);
        email = (EditText)view.findViewById(R.id.email_display);
        saveProfile = view.findViewById(R.id.saveProfile);
        resetPwd = view.findViewById(R.id.changePassword);
        logout = view.findViewById(R.id.logout);
        saveProfile.setOnClickListener(this);
        resetPwd.setOnClickListener(this);
        logout.setOnClickListener(this);
//        teacherName.setText(MainActivity.user.getName());
//        email.setText(MainActivity.user.getEmail());
    }

    //init info for the name and email
    private void initInfo()
    {
        if (MainActivity.user.getId() == null)
        {
            return;
        }
        List<Teacher> teacher = MainActivity.database.queryProfile(MainActivity.user.getId());
        if(teacher != null)
        {
            teacherName.setText(teacher.get(0).getName());
            email.setText(teacher.get(0).getEmail());
            MainActivity.user.setName(teacher.get(0).getName());
            MainActivity.user.setEmail(teacher.get(0).getEmail());
        }
    }


    @Override
    /**
     * 注册监听事件
     * */
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.saveProfile:
                String newTeacherName = teacherName.getText().toString();
                String newEmail = email.getText().toString();
                MainActivity.database.changeProfile(newTeacherName, MainActivity.user.getId(), newEmail);
                MainActivity.user.setName(newTeacherName);
                MainActivity.user.setEmail(newEmail);
                AlertDialog.Builder builder2 = new AlertDialog.Builder(view.getContext());//通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                builder2.setTitle("Save success!");//设置Title的内容

                builder2.setPositiveButton("OK",null);
                builder2.create().show();
                break;
            case R.id.logout:
                MainActivity.user = new Teacher();
                LoginActivity.actionStart(v.getContext());//打开登录界面
                MainActivity.mainActivity.refreshManagementFrag();
                break;
            case R.id.changePassword:
                //network is not connected
                if (!MainActivity.netWork.isMobileConnected(view.getContext()) && !MainActivity.netWork.isWifiConnected(view.getContext()))
                {
                    Toast.makeText(view.getContext(), "Connection failed. Please check your network connection.", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            int max=999999;
                            int min=100000;
                            Random random = new Random();

                            verifyCode = random.nextInt(max)%(max-min+1) + min;

                            GMailSender sender = new GMailSender("jbddyyh2819@gmail.com",
                                    "www1234com");
                            sender.sendMail("A request of resetting password from SSAS", "The verification code is: " + verifyCode,
                                    "jbddyyh2819@gmail.com", MainActivity.user.getEmail());

                            VerifyActivity.actionStart(view.getContext(), MainActivity.user.getId(), MainActivity.user.getEmail());
                        } catch (Exception e) {
                            Looper.prepare();
                            Toast.makeText(view.getContext(),"Please enter a valid email address!", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                            e.printStackTrace();
                            Log.e("SendMail", e.getMessage(), e);
                        }
                    }

                }).start();

                break;

        }
    }

}
