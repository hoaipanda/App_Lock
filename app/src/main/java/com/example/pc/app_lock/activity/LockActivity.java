package com.example.pc.app_lock.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.pc.app_lock.AppUtil;
import com.example.pc.app_lock.R;
import com.takwolf.android.lock9.Lock9View;

public class LockActivity extends AppCompatActivity {
    private Lock9View lock1;
    private String password;
    private SharedPreferences preferences;
    private TextView tvDraw;
    public static LockActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        instance = this;
        lock1 = findViewById(R.id.lock1);
        tvDraw = findViewById(R.id.tvDraw);

        preferences = getSharedPreferences(AppUtil.DATAPASSWORD, Context.MODE_PRIVATE);
        password = preferences.getString(AppUtil.PASSWORD, "");
        lock1.setGestureCallback(new Lock9View.GestureCallback() {

            @Override
            public void onNodeConnected(@NonNull int[] numbers) {
            }

            @Override
            public void onGestureFinished(@NonNull int[] numbers) {
                StringBuilder builder = new StringBuilder();
                for (int number : numbers) {
                    builder.append(number);
                }

                if (password.equals(builder.toString())) {
                    finish();
                } else {
                    tvDraw.setText("Password in correct!");
                }

            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        finish();
    }
}
