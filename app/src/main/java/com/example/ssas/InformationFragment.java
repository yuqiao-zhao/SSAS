package com.example.ssas;

import android.os.Bundle;
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

public class InformationFragment extends Fragment implements View.OnClickListener{
    private View view;
    private EditText teacherName;
    private EditText email;
    private View saveProfile;
    private View resetPwd;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_personal_information, container, false);
        bind();
        //initInfo();
        return view;
    }

    @Override
    public void onResume()
    {
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
        saveProfile.setOnClickListener(this);
        resetPwd.setOnClickListener(this);
        teacherName.setText(MainActivity.user.getName());
        email.setText(MainActivity.user.getEmail());
    }

    //init info for the name and email
    private void initInfo()
    {
        //TODO: consult the database
        Log.d("username", MainActivity.user.getName());
        teacherName.setText("jjj");



        teacherName.setText("Jeff");
        email.setText("xxxx@xxx.xxx");
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
                //TODO: save profile to the database
                MainActivity.user.setName(teacherName.getText().toString());
                MainActivity.user.setEmail(email.getText().toString());
                AlertDialog.Builder builder2 = new AlertDialog.Builder(view.getContext());//通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                builder2.setTitle("Save success!");//设置Title的内容

                builder2.setPositiveButton("OK",null);
                builder2.create().show();
                break;

            case R.id.changePassword:
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());//通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                final AlertDialog dialog = builder.create();
                View dialogView = View.inflate(view.getContext(), R.layout.change_password, null);
                dialog.setView(dialogView);
                dialog.setTitle("Reset your password");
                dialog.show();

                final EditText oldPwdText = (EditText)dialogView.findViewById(R.id.old_password);
                final EditText newPwdText = (EditText)dialogView.findViewById(R.id.new_password);
                final EditText confirmPwdText = (EditText)dialogView.findViewById(R.id.confirm_new_password);
                Button confirm = (Button)dialogView.findViewById(R.id.submit_password);
                Button cancel = (Button)dialogView.findViewById(R.id.cancel_changePwd);

                confirm.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        String oldPwd = oldPwdText.getText().toString();
                        String newPwd = newPwdText.getText().toString();
                        String confirmPwd = confirmPwdText.getText().toString();
                        //TODO:check if old password equals to the current password
                        if (!oldPwd.equals(MainActivity.user.getPassword()))
                        {
                            Toast.makeText(view.getContext(), "Old password is wrong.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(!newPwd.equals(confirmPwd))
                        {
                            Toast.makeText(view.getContext(), "Your new passwords are not equal.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //TODO: change the database
                        MainActivity.user.setPassword(newPwd);

                        AlertDialog.Builder builder3 = new AlertDialog.Builder(view.getContext());//通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                        builder3.setTitle("Save success!");//设置Title的内容

                        builder3.setPositiveButton("OK",null);
                        builder3.create().show();

                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                break;
        }
    }

}
