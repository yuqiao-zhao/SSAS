package com.example.ssas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText id;//用户的密码
    private EditText password;//用户的密码
    private Button login;//登录按钮
    private TextView register;//注册按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bind();
    }
    /**
     * 绑定控件
     */
    private void bind()
    {
        id = (EditText) findViewById(R.id.id);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register = (TextView) findViewById(R.id.register);

        //绑定监听器
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.login:
                login();//调用login的函数，让用户进行登陆
                break;
            case R.id.register:
                LoginActivity.this.finish();//结束login activity，打开register activity
                RegisterActivity.actionStart(getApplicationContext());
                break;
        }
    }

    /**
     * 启动活动
     */
    public static void actionStart(Context context)
    {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    /**
     * 登录
     */
    private void login()
    {
//        String userId, userPassword;//用户输入的id和密码
//        userId = id.getText().toString();//获取用户输入的id
//        userPassword = password.getText().toString();//获取用户输入的密码
//        if (userId.length() == 0 || userPassword.length() == 0)//如果用户没有输入密码或者用户名，则不做任何反馈
//        {
//            Toast.makeText(LoginActivity.this, "Please enter username and password.", Toast.LENGTH_SHORT).show();
//            return;
//        }
        Toast.makeText(LoginActivity.this, "Login Success.", Toast.LENGTH_SHORT).show();
        MainActivity.user.setId("11223344");
        LoginActivity.this.finish();//结束当前login的activity，直接回到main activity
    }
}
