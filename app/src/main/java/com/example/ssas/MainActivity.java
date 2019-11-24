package com.example.ssas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.icu.text.IDNA;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ManageFragment manageFragment = null;
    private StatisticFragment statisticFragment = null;
    private InformationFragment informationFragment = null;
    static public Teacher user = new Teacher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();

        manageFragment = new ManageFragment();
        setFragment(manageFragment);
    }

    /**
     * 启动app时，弹出登录界面
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        if (user.getId() != null)//如果当前用户信息不为空，则跳过登录，直接打开main activity
        {
            return;
        }
        else
            LoginActivity.actionStart(MainActivity.this);//打开登录界面
    }

    /**
     * 绑定控件
     */
    private void bindView()
    {
        //为三个fragment的按钮绑定监听器
        TextView management = (TextView) findViewById(R.id.Management),
                statistic = (TextView) findViewById(R.id.Statistic),
                personalInformation = (TextView) findViewById(R.id.PersonalInformation);

        management.setOnClickListener(this);
        statistic.setOnClickListener(this);
        personalInformation.setOnClickListener(this);
    }

    @Override
    /**
     * 注册监听事件
     * */
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.Management:
                if (manageFragment == null)
                    manageFragment = new ManageFragment();
                setFragment(manageFragment);
                break;
            case R.id.Statistic:
                if (statisticFragment == null)
                    statisticFragment = new StatisticFragment();
                setFragment(statisticFragment);
                break;
            case R.id.PersonalInformation:
                if (informationFragment == null)
                    informationFragment = new InformationFragment();
                setFragment(informationFragment);
                break;
        }
    }

    /**
     * 改变当前活动的fragment
     *
     * @param fragment 新的活动fragment
     */
    public void setFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.activeFragment_layout, fragment);
        transaction.commit();
    }
}
