package com.example.pc.app_lock.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.app_lock.AppUtil;
import com.example.pc.app_lock.R;
import com.takwolf.android.lock9.Lock9View;

public class ChangePasswordActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String password, passEnter;
    private Lock9View lock1, lock2, lock3;
    private TextView tvDraw;
    private ImageView imReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        preferences = getSharedPreferences(AppUtil.DATAPASSWORD, Context.MODE_PRIVATE);
        editor = preferences.edit();
        password = preferences.getString(AppUtil.PASSWORD, "");
        lock1 = findViewById(R.id.lock1);
        lock2 = findViewById(R.id.lock2);
        lock3 = findViewById(R.id.lock3);
        tvDraw = findViewById(R.id.tvDraw);
        imReset = findViewById(R.id.imReset);


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
                    lock1.setVisibility(View.GONE);
                    lock2.setVisibility(View.VISIBLE);
                    tvDraw.setText("Draw new password");
                } else {
                    tvDraw.setText("Password in correct!");
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
                passEnter = builder.toString();
                lock2.setVisibility(View.GONE);
                lock3.setVisibility(View.VISIBLE);
                tvDraw.setText("Draw new password again");
            }

        });

        lock3.setGestureCallback(new Lock9View.GestureCallback() {

            @Override
            public void onNodeConnected(@NonNull int[] numbers) {
            }

            @Override
            public void onGestureFinished(@NonNull int[] numbers) {
                StringBuilder builder = new StringBuilder();
                for (int number : numbers) {
                    builder.append(number);
                }

                if (passEnter.equals(builder.toString())){
                    password = passEnter;
                    editor.putString(AppUtil.PASSWORD,password);
                    editor.commit();
                    finish();
                    Toast.makeText(ChangePasswordActivity.this,"Change password done",Toast.LENGTH_LONG).show();
                }else {
                    tvDraw.setText("Draw new password again");
                }


            }

        });

        imReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passEnter= "";
                tvDraw.setText("Draw new password");
                lock1.setVisibility(View.VISIBLE);
                lock2.setVisibility(View.GONE);
                lock3.setVisibility(View.GONE);
            }
        });

    }

}
