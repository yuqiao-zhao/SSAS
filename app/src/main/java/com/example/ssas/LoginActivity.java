package com.example.ssas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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

    private long exitTime=System.currentTimeMillis();

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
            case R.id.forget_password:

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
     * 返回键退出应用
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            MainActivity.user.setId("-100");
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 登录
     */
    private void login()
    {
        String userId, userPassword;//用户输入的id和密码
        userId = id.getText().toString();//获取用户输入的id
        userPassword = password.getText().toString();//获取用户输入的密码
        if (userId.length() == 0 || userPassword.length() == 0)//如果用户没有输入密码或者用户名，则不做任何反馈
        {
            Toast.makeText(LoginActivity.this, "Please enter username and password.", Toast.LENGTH_SHORT).show();
            return;
        }
        ReturnMsg returnMsg = MainActivity.database.queryLogin(userId, userPassword);
        if(returnMsg.getSuccess())
        {
            Toast.makeText(LoginActivity.this, returnMsg.getMessage(), Toast.LENGTH_SHORT).show();
            MainActivity.user.setId(userId);
            MainActivity.mainActivity.refreshManagementFrag();

            LoginActivity.this.finish();//结束当前login的activity，直接回到main activity
        }
        else
        {
            Toast.makeText(LoginActivity.this, returnMsg.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
}
