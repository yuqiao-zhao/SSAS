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

public class VerifyActivity extends AppCompatActivity implements View.OnClickListener{
    private Button submitVerification;
    private Button back;
    private EditText verification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        Toast.makeText(this, "A verification code was sent to your email.", Toast.LENGTH_LONG).show();
        bind();
    }

    /**
     * 绑定控件
     */
    private void bind() {
        submitVerification = (Button) findViewById(R.id.submit_verification);
        verification = (EditText) findViewById(R.id.verificationCode);
        back = (Button) findViewById(R.id.cancel_verification);

        submitVerification.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    /**
     * 启动活动
     */
    public static void actionStart(Context context)
    {
        Intent intent = new Intent(context, VerifyActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.submit_verification:
                String userInput = verification.getText().toString();
                if(InformationFragment.verifyCode != 0 && userInput.equals(String.valueOf(InformationFragment.verifyCode)))
                {
                    ResetpwdActivity.actionStart(this, this);
                }
                else
                {
                    Toast.makeText(this, "The verification code is wrong.",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.cancel_verification:
                finish();
                break;
        }
    }

    public void finishAll(Activity activity)
    {
        activity.finish();
        this.finish();
    }
}
