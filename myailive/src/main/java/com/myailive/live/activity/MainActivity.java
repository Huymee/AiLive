package com.myailive.live.activity;

import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.myailive.live.MyApplication;
import com.myailive.live.R;
import com.myailive.live.fragment.FavFragment;
import com.myailive.live.fragment.HomeFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.activity_main)
public class MainActivity extends Activity implements RadioGroup.OnCheckedChangeListener {

    @ViewById(R.id.fl_main)
    FrameLayout flMain;
    @ViewById(R.id.rb_01)
    RadioButton rb01;
    @ViewById(R.id.rb_02)
    RadioButton rb02;
    @ViewById(R.id.rg_main_bottom)
    RadioGroup rgMainBottom;
    @ViewById(R.id.ll_left_menu)
    LinearLayout llLeftMenu;
    @ViewById(R.id.dl_main)
    DrawerLayout dlMain;
    @ViewById(android.R.id.home)
    ImageView vHome;
    @ViewById(R.id.iv_user_icon)
    ImageView ivUserIcon;
    @ViewById(R.id.iv_user_level)
    ImageView ivUserLevel;
    @ViewById(R.id.tv_user_nick)
    TextView tvUserNick;
    @ViewById(R.id.iv_user_vip_level)
    ImageView ivUserVipLevel;
    @ViewById(R.id.ll_user_info)
    LinearLayout llUserInfo;
    @ViewById(R.id.tv_my_gold)
    TextView tvMyGold;
    @ViewById(R.id.tv_my_income)
    TextView tvMyIncome;
    @ViewById(R.id.tv_my_experience)
    TextView tvMyExperience;
    @ViewById(R.id.tv_my_charging)
    TextView tvMyCharging;
    @ViewById(R.id.tv_setting)
    TextView tvSetting;
    @ViewById(R.id.tv_help)
    TextView tvHelp;
    @ViewById(R.id.rl_user)
    RelativeLayout user_ll;
    @ViewById(R.id.ll_gold)
    LinearLayout gold_ll;
    @ViewById(R.id.ll_income)
    LinearLayout income_ll;
    @ViewById(R.id.ll_experience)
    LinearLayout experience_ll;
    @ViewById(R.id.ll_charging)
    LinearLayout charging_ll;
    @ViewById(R.id.ll_setting)
    LinearLayout setting_ll;
    @ViewById(R.id.ll_help)
    LinearLayout help_ll;
    private HomeFragment homeFragment;
    private FavFragment favFragment;

    @AfterViews
    void initViews() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) actionBar.setHomeButtonEnabled(true);
        vHome.setImageResource(R.mipmap.ic_record_pressed);
        initFragment();
        rgMainBottom.setOnCheckedChangeListener(this);
        rb01.setChecked(true);
        initDrawLayout();

  	}

    @Click({R.id.rl_user, R.id.ll_gold, R.id.ll_income, R.id.ll_experience, R.id.ll_charging, R.id.ll_setting, R.id.ll_help})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_user:
                if (MyApplication.isLogin) {
                    // TODO 打开用户界面信息
                } else {
                    // TODO 打开登录界面
                }
                break;
            case R.id.ll_gold:
                //TODO 我的金币

                break;
            case R.id.ll_income:
                //TODO 我的收入
                break;
            case R.id.ll_experience:
                //TODO 我的经验
                break;
            case R.id.ll_charging:
                //TODO 充值界面
                break;
            case R.id.ll_setting:
                //TODO 设置界面
                break;
            case R.id.ll_help:
                //TODO 帮助或反馈界面
                break;
        }
        dlMain.closeDrawer(llLeftMenu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * 初始化侧滑菜单
     */
    void initDrawLayout() {
        dlMain.addDrawerListener(new DrawerLayout.DrawerListener() {
            boolean isOpened = false;
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                isOpened = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                isOpened = false;
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == 2) {
                    if (isOpened) {
                        ObjectAnimator.ofFloat(vHome, "rotation", -45, 0).setDuration(300).start();
                    } else {
                        ObjectAnimator.ofFloat(vHome, "rotation", 0, -45).setDuration(300).start();
                    }
                }
            }
        });
    }

    /**
     * 初始化Fragment
     */
    void initFragment() {
        homeFragment = new HomeFragment();
        favFragment = new FavFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fl_main, homeFragment, "index")
                .add(R.id.fl_main, favFragment, "bill");
        transaction.commit();
    }

    /**
     * ActionBar等菜单监听
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == android.R.id.home) {
            boolean isOpen = dlMain.isDrawerOpen(llLeftMenu);
            if (isOpen) {
                dlMain.closeDrawer(llLeftMenu);
            } else {
                dlMain.openDrawer(llLeftMenu);
            }
        } else if (id == R.id.start_live) {
            new AlertDialog.Builder(MainActivity.this).setTitle("开始直播")
                    .setMessage("根据相关法规\n开启直播前需要完成实名认证")
                    .setPositiveButton("立即认证", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //TODO 跳转到实名认证界面
                        }
                    }).setNegativeButton("暂不认证,先开播", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent=new Intent(MainActivity.this,LiveActivity.class);
                    intent.putExtra("roomId","17027");// TODO: 17/07/10 获取房间号
                    startActivity(intent);
                }
            }).create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * RadioGroup点击事件监听
     */
    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        switch (checkedId) {
            case R.id.rb_01:
                transaction.show(homeFragment);
                transaction.hide(favFragment);
                break;
            case R.id.rb_02:
                transaction.show(favFragment);
                transaction.hide(homeFragment);
                break;
        }
        transaction.commit();
    }

    private boolean isClose = false;

    @Override
    public void onBackPressed() {
        if (isClose) {
            super.onBackPressed();
        } else {
            isClose = true;
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            changeCloseState();
        }
    }

    @Background
    void changeCloseState() {
        try {
            Thread.sleep(3000);
            isClose = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
