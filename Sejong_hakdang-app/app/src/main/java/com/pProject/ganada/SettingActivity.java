package com.pProject.ganada;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

public class SettingActivity extends AppCompatActivity {

    private TextView setting_foreign, lang_change_foreign, info_foreign;
    private ConstraintLayout change_language, information;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //툴바 설정
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        //상태바 설정
        View view = getWindow().getDecorView();
        if (view != null) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.parseColor("#FFF2CC"));
        }

        change_language = (ConstraintLayout) findViewById(R.id.change_language);
        information = (ConstraintLayout) findViewById(R.id.information);

        //언어 변경 페이지로 이동
        change_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ChangeLanguageActivity.class));
            }
        });

        //정보 페이지로 이동
        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), InfoActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //sharedPreferences 에서 선택된 언어 가져오기
        language = getSharedPreferences("Language", MODE_PRIVATE).getString("language", null);
        setLanguageUI(language);    //선택된 언어에 맞춰 TextView 의 텍스트를 설정하는 함수 호출
    }

    //선택된 언어에 맞춰 TextView 의 텍스트를 설정하는 함수
    private void setLanguageUI(String language) {

        setting_foreign = (TextView) findViewById(R.id.setting_foreign);
        lang_change_foreign = (TextView) findViewById(R.id.lang_change_foreign);
        info_foreign = (TextView) findViewById(R.id.info_foreign);

        switch (language) {
            case "english":
                setting_foreign.setText(R.string.setting_en);
                lang_change_foreign.setText(R.string.change_language_en);
                info_foreign.setText(R.string.information_en);
                break;
            case "china":
                setting_foreign.setText(R.string.setting_cn);
                lang_change_foreign.setText(R.string.change_language_cn);
                info_foreign.setText(R.string.information_cn);
                break;
            case "vietnam":
                setting_foreign.setText(R.string.setting_vn);
                lang_change_foreign.setText(R.string.change_language_vn);
                info_foreign.setText(R.string.information_vn);
                break;
            default:
                setting_foreign.setText(R.string.setting_jp);
                lang_change_foreign.setText(R.string.change_language_jp);
                info_foreign.setText(R.string.information_jp);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}