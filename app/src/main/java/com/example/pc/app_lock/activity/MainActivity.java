package com.example.pc.app_lock.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.pc.app_lock.AppUtil;
import com.example.pc.app_lock.R;
import com.takwolf.android.lock9.Lock9View;

public class MainActivity extends AppCompatActivity {
    private ImageView imReset;
    private Lock9View lock1;
    private Lock9View lock2;
    private String password, passEnter = "";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private TextView tvDraw;
    private Intent startIntent;
    public static MainActivity instance = null;
    public static final String[] PERMISSIONS = {
            Manifest.permission.VIBRATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.GET_TASKS,

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        startIntent = getIntent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : PERMISSIONS) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(PERMISSIONS, 0);
                    return;
                }
            }
            lock1 = findViewById(R.id.lock1);
            lock2 = findViewById(R.id.lock2);
            tvDraw = findViewById(R.id.tvDraw);
            imReset = findViewById(R.id.imReset);

        } else {
            lock1 = findViewById(R.id.lock1);
            lock2 = findViewById(R.id.lock2);
            tvDraw = findViewById(R.id.tvDraw);
            imReset = findViewById(R.id.imReset);
        }
        preferences = getSharedPreferences(AppUtil.DATAPASSWORD, Context.MODE_PRIVATE);
        editor = preferences.edit();
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

                if (password.length() > 0) {
                    if (password.equals(builder.toString())) {
                        Intent intent = new Intent(MainActivity.this, ControlActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        tvDraw.setText("Password in correct!");
                    }
                } else {
                    passEnter = builder.toString();
                    tvDraw.setText("Draw password again!");
                    lock1.setVisibility(View.GONE);
                    lock2.setVisibility(View.VISIBLE);
                }
            }

        });

        lock2.setGestureCallback(new Lock9View.GestureCallback() {
            @Override
            public void onNodeConnected(@NonNull int[] numbers) {
            }

            @Override
            public void onGestureFinished(@NonNull int[] numbers) {
                StringBuilder builder = new StringBuilder();
                for (int number : numbers) {
                    builder.append(number);
                }

                if (builder.toString().equals(passEnter)) {
                    password = builder.toString();
                    editor.putString(AppUtil.PASSWORD, password);
                    editor.commit();
                    Intent intent = new Intent(MainActivity.this, ControlActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    finish();
                }else {
                    tvDraw.setText("Draw password fail!");
                }
            }
        });

        imReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passEnter ="";
                lock1.setVisibility(View.VISIBLE);
                lock2.setVisibility(View.GONE);
                tvDraw.setText("Draw your password");
                Toast.makeText(MainActivity.this,"Reset",Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void finish() {
        super.finish();
        instance = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                finish();
                return;
            }
        }
        finish();
        startActivity(startIntent);
    }
}

