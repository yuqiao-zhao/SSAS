package com.example.ssas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ResetpwdActivity extends AppCompatActivity implements View.OnClickListener{
    private static  VerifyActivity activity;
    private EditText newPwd;
    private EditText confirmPwd;
    private Button submitPwd;
    private Button cancelChangePwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        bind();
    }


    public void bind()
    {
        newPwd = findViewById(R.id.new_password);
        confirmPwd = findViewById(R.id.confirm_new_password);
        submitPwd = findViewById(R.id.submit_password);
        cancelChangePwd = findViewById(R.id.cancel_changePwd);

        submitPwd.setOnClickListener(this);
        cancelChangePwd.setOnClickListener(this);
    }


    /**
     * 启动活动
     */
    public static void actionStart(Context context, VerifyActivity a)
    {
        activity = a;
        Intent intent = new Intent(context, ResetpwdActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.submit_password:
                String newPassword = newPwd.getText().toString();
                String confirmPassword = confirmPwd.getText().toString();
                if(!newPassword.equals(confirmPassword))
                {
                    Toast.makeText(this, "Your new passwords are not equal.", Toast.LENGTH_SHORT).show();
                    return;
                }
                MainActivity.database.changePwd(MainActivity.user.getId(), newPassword);
                InformationFragment.verifyCode = 0;
                Toast.makeText(this, "Password reset Success! Please sign in again.", Toast.LENGTH_SHORT).show();
                MainActivity.user = new Teacher();
                activity.finishAll(this);
                break;
            case R.id.cancel_changePwd:
                InformationFragment.verifyCode = 0;
                activity.finishAll(this);
                break;
        }
    }
}
