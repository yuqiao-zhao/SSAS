package com.example.ssas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class IDInputActivity extends AppCompatActivity implements View.OnClickListener{
    private Button submitID;
    private Button back;
    private EditText inputID;
    public static boolean resetSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputid);
        resetSuccess = false;
        bind();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (resetSuccess)
            this.finish();
    }
    /**
     * 绑定控件
     */
    private void bind() {
        submitID = (Button) findViewById(R.id.submit_ID);
        inputID = (EditText) findViewById(R.id.inputID);
        back = (Button) findViewById(R.id.cancel_ID);

        inputID.setOnClickListener(this);
        back.setOnClickListener(this);
        submitID.setOnClickListener(this);
    }

    /**
     * 启动活动
     */
    public static void actionStart(Context context)
    {
        Intent intent = new Intent(context, IDInputActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.submit_ID:
                final String userInputId = inputID.getText().toString();
                Boolean isSignUp = MainActivity.database.isSignUp(userInputId);
                if(isSignUp)
                {
                    //network is not connected
                    if (!MainActivity.netWork.isMobileConnected(this) && !MainActivity.netWork.isWifiConnected(this))
                    {
                        Toast.makeText(this, "Connection failed. Please check your network connection.", Toast.LENGTH_SHORT).show();
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
                                String email = MainActivity.database.queryEmail(userInputId).get(0);

                                GMailSender sender = new GMailSender("jbddyyh2819@gmail.com",
                                        "www1234com");
                                sender.sendMail("A request of reseting password from SSAS", "The verification code is: " + InformationFragment.verifyCode,
                                        "jbddyyh2819@gmail.com", email);
                                //Toast.makeText(view.getContext(),"The verification code was sent!", Toast.LENGTH_SHORT).show();
                                VerifyActivity.actionStart(IDInputActivity.this, userInputId, email);
                            } catch (Exception e) {
                                Looper.prepare();
                                Toast.makeText(IDInputActivity.this,"Please enter a valid email address!", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        }

                    }).start();
                }
                else
                {
                    Toast.makeText(this, "You haven't signed up.",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.cancel_ID:
                finish();
                break;
        }
    }

}
