package com.example.ssas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Random;

public class VerifyEmailActivity extends AppCompatActivity implements View.OnClickListener{
    private Button submitVerification;
    private Button back;
    private EditText verification;
    private TextView resend;
    private static RegisterActivity registerActivity;
    private static  String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyemail);
        Toast.makeText(this, "A verification code was sent to your email.", Toast.LENGTH_LONG).show();
        bind();
        CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(resend, 60000, 1000); //倒计时1分钟
        mCountDownTimerUtils.start();
    }

    /**
     * 绑定控件
     */
    private void bind() {
        submitVerification = (Button) findViewById(R.id.submit_verification_emailverify);
        verification = (EditText) findViewById(R.id.verificationCode_emailverify);
        back = (Button) findViewById(R.id.cancel_verification_emailverify);
        resend = (TextView)findViewById(R.id.countDown_email);

        submitVerification.setOnClickListener(this);
        back.setOnClickListener(this);
        resend.setOnClickListener(this);
    }

    /**
     * 启动活动
     */
    public static void actionStart(Context context, RegisterActivity activity, String e)
    {
        email = e;
        registerActivity = activity;
        Intent intent = new Intent(context, VerifyEmailActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.submit_verification_emailverify:
                String userInput = verification.getText().toString();
                if(InformationFragment.verifyCode != 0 && userInput.equals(String.valueOf(InformationFragment.verifyCode)))
                {
                    registerActivity.finishRegister();
                    this.finish();
                }
                else
                {
                    Toast.makeText(this, "The verification code is wrong.",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.cancel_verification_emailverify:
                finish();
                break;
            case R.id.countDown_email:
                //network is not connected
                if (!MainActivity.netWork.isMobileConnected(this) && !MainActivity.netWork.isWifiConnected(this))
                {
                    Toast.makeText(VerifyEmailActivity.this, "Connection failed. Please check your network connection.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(this, "A verification code was sent to your email.", Toast.LENGTH_LONG).show();
                CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(resend, 60000, 1000); //倒计时1分钟
                mCountDownTimerUtils.start();
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
                                    "jbddyyh2819@gmail.com", email);
                            //Toast.makeText(view.getContext(),"The verification code was sent!", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            Looper.prepare();
                            Toast.makeText(VerifyEmailActivity.this,"Please enter a valid email address!", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }

                }).start();

                break;
        }
    }

}
