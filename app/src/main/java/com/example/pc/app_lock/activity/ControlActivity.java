package com.example.pc.app_lock.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;

import com.example.pc.app_lock.AppUtil;
import com.example.pc.app_lock.service.GroupService;
import com.example.pc.app_lock.R;
import com.example.pc.app_lock.adapter.ViewPagerAdapter;
import com.example.pc.app_lock.fragment.GroupFragment;
import com.example.pc.app_lock.fragment.HomeFragment;
import com.example.pc.app_lock.fragment.TimeFragment;

import company.librate.RateDialog;

public class ControlActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private NavigationView nav;
    private ViewPager pager;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle mToggle;
    private RateDialog rateDialog, rateDialog1;
    private BottomNavigationView bottomNavigationView;
    private MenuItem prevMenuItem;
    private boolean isFisrt = true;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        preferences = getSharedPreferences(AppUtil.DATAPERMISSION, Context.MODE_PRIVATE);
        editor = preferences.edit();
        isFisrt = preferences.getBoolean(AppUtil.ISFIRST, true);
        if (isFisrt) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AlertDialog alertDialog = new AlertDialog.Builder(ControlActivity.this).create();
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setMessage("Alllow Applock read your infomation apps!");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

            }
            isFisrt = false;
            editor.putBoolean(AppUtil.ISFIRST, false);
            editor.commit();

        }

        initView();
        setupBottomNavigation();
        setDrawerLayout();
        Intent intent1 = new Intent(this, GroupService.class);
        startService(intent1);
    }

    private void initView() {
        pager = findViewById(R.id.pager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        rateDialog = new RateDialog(this, "", true);
        rateDialog1 = new RateDialog(this, "", false);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        drawer = findViewById(R.id.drawer);
        nav = findViewById(R.id.nav);

    }


    private void setupBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.imHome:
                                pager.setCurrentItem(0);
                                return true;
                            case R.id.imGroup:
                                pager.setCurrentItem(1);
                                return true;
                            case R.id.imTime:
                                pager.setCurrentItem(2);
                                return true;

                        }
                        return false;
                    }
                });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(pager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new GroupFragment());
        adapter.addFragment(new TimeFragment());
        viewPager.setCurrentItem(0);
        viewPager.setAdapter(adapter);
    }


    private void setDrawerLayout() {
        mToggle = new ActionBarDrawerToggle(this, drawer, toolbar, 0, 0);
        drawer.addDrawerListener(mToggle);
        mToggle.syncState();
        nav.setNavigationItemSelectedListener(this);
//        nav.setItemIconTintList(null);
        mToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.imSetting:
                Intent intent = new Intent(ControlActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.imShare:
                Intent intent1 = new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse("https://play.google.com/store/apps/developer?id="+this.getPackageName()));
                startActivity(intent1);
                break;
            case R.id.imRate:
                if (!rateDialog1.isRate()) {
                    rateDialog1.show();
                }
                break;
            case R.id.imPolicy:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!rateDialog.isRate()) {
            rateDialog.show();
        } else {
            finish();
        }
    }
}
