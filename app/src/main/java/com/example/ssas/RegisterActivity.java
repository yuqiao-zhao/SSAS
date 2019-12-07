package com.example.ssas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText id;//用户的id
    private EditText name;//用户的真实姓名
    private EditText password;//用户的密码
    private EditText password_repeat;//用户的密码
    private Button register;//登录按钮
    private EditText email;//email

    private String userId, userName, userPassword, userPasswordRepeat, userEmail;//用户输入的id、姓名、密码、确认密码

    private List<Map<String, Object>> list = new ArrayList<>();//从服务器端返回的结果

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        bind();
    }

    /**
     * 绑定控件
     */
    private void bind()
    {
        id = (EditText) findViewById(R.id.id);
        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        password_repeat = (EditText) findViewById(R.id.password_repeat);

        register = (Button) findViewById(R.id.register);
        email = (EditText)findViewById(R.id.email);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.register:
                register();
                break;
        }
    }

    /**
     * 启动活动
     */
    public static void actionStart(Context context)
    {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    public void finishRegister()
    {
        Teacher teacher =new Teacher();
        teacher.setId(userId);
        teacher.setEmail(userEmail);
        teacher.setName(userName);
        teacher.setPassword(userPassword);

        MainActivity.user.setId(userId);
        Toast.makeText(RegisterActivity.this, "Register success!", Toast.LENGTH_SHORT).show();
        RegisterActivity.this.finish();//结束当前login的activity，直接回到main activity
        ReturnMsg returnMsg = MainActivity.database.insertNewTeacher(userId,userPassword,userName,userEmail);
    }

    /**
     * 注册
     */
    private void register()
    {
        userId = id.getText().toString();//获取用户输入的id
        userName = name.getText().toString();//获取用户输入的姓名
        userPassword = password.getText().toString();//获取用户输入的密码
        userPasswordRepeat = password_repeat.getText().toString();////获取用户输入的确认密码
        userEmail = email.getText().toString();

        if (userId.length() == 0 || userPassword.length() == 0)//如果用户没有输入学号、姓名、密码
        {
            Toast.makeText(RegisterActivity.this, "Please enter your id and password", Toast.LENGTH_SHORT).show();
            return;
        }

        Boolean isSignUp = MainActivity.database.isSignUp(userId);
        if(isSignUp)//user has already signed up
        {
            Toast.makeText(RegisterActivity.this, "You have already signed up. Please sign in.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userPasswordRepeat.length() == 0)//如果用户没有输入确认密码
        {
            Toast.makeText(RegisterActivity.this, "Please confirm your password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!userPassword.equals(userPasswordRepeat))//如果用户两次输入的密码不一致
        {
            Toast.makeText(RegisterActivity.this, "The password are not equal. Please enter again.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userEmail.length() == 0)//如果用户没有输入email
        {
            Toast.makeText(RegisterActivity.this, "Please enter your email.", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    int max=999999;
                    int min=100000;
                    Random random = new Random();

                    InformationFragment.verifyCode = random.nextInt(max)%(max-min+1) + min;

                    GMailSender sender = new GMailSender("jbddyyh2819@gmail.com",
                            "www1234com");
                    sender.sendMail("A request of reseting password from SSAS", "The verification code is: " + InformationFragment.verifyCode,
                            "jbddyyh2819@gmail.com", userEmail);
                    //Toast.makeText(view.getContext(),"The verification code was sent!", Toast.LENGTH_SHORT).show();
                    VerifyEmailActivity.actionStart(RegisterActivity.this, RegisterActivity.this);
                } catch (Exception e) {
                    Looper.prepare();
                    Toast.makeText(RegisterActivity.this,"Please enter a valid email address!", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }

        }).start();
    }
}
