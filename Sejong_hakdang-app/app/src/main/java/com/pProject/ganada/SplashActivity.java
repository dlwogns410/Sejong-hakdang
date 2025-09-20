package com.pProject.ganada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class SplashActivity extends AppCompatActivity {

    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //상태바 설정
        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().setStatusBarColor(Color.parseColor("#FFF2CC"));
            }
        }

        language = getSharedPreferences("Language", MODE_PRIVATE).getString("language", null);

        startLoading();
    }

    //2초 대기하다 언어 선택 화면으로 넘어간다.
    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (language == null)   //선택된 언어가 없으면 SelectLanguageActivity 화면으로 이동
                    startActivity(new Intent(getApplicationContext(), SelectLanguageActivity.class));
                else    //선택된 언어가 있으면 MainActivity 화면으로 이동
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }, 2000);
    }
}