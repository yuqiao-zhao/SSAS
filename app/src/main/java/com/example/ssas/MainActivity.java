package com.example.ssas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static MainActivity mainActivity;
    private long exitTime=System.currentTimeMillis();
    public static Database database;

    private ManageFragment manageFragment = null;
    private StatisticFragment statisticFragment = null;
    private InformationFragment informationFragment = null;
    static public Teacher user = new Teacher();

    static public NetWork netWork = new NetWork();



    public String getDBPath(){
        return this.getDatabasePath("SSAS.db").getPath();
    }

    /**
     * 返回键退出应用
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "Press one times more to exit program.", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public MainActivity()
    {
        mainActivity = this;
    }

    public void refreshManagementFrag()
    {
        manageFragment = new ManageFragment();
        setFragment(manageFragment);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new Database(this, "SSAS", null, 1);
        SQLiteDatabase db = database.getWritableDatabase();
        database.setDb(db);

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
            if(user.getId().equals("-100"))
            {
                MainActivity.this.finish();
                System.exit(0);
            }

            return;
        }
        else
        {
            LoginActivity.actionStart(MainActivity.this);//打开登录界面
        }
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
        transaction.commitAllowingStateLoss();
    }
}
