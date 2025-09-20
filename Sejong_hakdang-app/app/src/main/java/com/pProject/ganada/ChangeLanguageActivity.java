package com.pProject.ganada;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ChangeLanguageActivity extends AppCompatActivity {

    private Button settingUpBtn;
    private ImageView unitedStatesIv, chinaIv, vietnamIv, japanIv;
    private TextView englishTv, chineseTv, vietnamTv, japanTv, lang_change_foreign;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);

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

        //sharedPreferences 에서 선택된 언어 가져오기
        language = getSharedPreferences("Language", MODE_PRIVATE).getString("language", null);
        setLanguageUI(language);    //선택된 언어에 맞춰 TextView 의 텍스트를 설정하는 함수 호출

        //설정하기 버튼 클릭 리스너 -> MainActivity로 이동
        settingUpBtn = (Button) findViewById(R.id.setting_up_btn);
        settingUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveLanguage(language);
            }
        });

        unitedStatesIv = (ImageView) findViewById(R.id.unite_states_iv);
        chinaIv = (ImageView) findViewById(R.id.china_iv);
        vietnamIv = (ImageView) findViewById(R.id.vietnam_iv);
        japanIv = (ImageView) findViewById(R.id.japan_iv);

        englishTv = (TextView) findViewById(R.id.english_tv);
        chineseTv = (TextView) findViewById(R.id.chinese_tv);
        vietnamTv = (TextView) findViewById(R.id.vietnam_tv);
        japanTv = (TextView) findViewById(R.id.japan_tv);

        unitedStatesIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initUI();
                language = "english";
                changeTvStyle("english");
            }
        });

        englishTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initUI();
                language = "english";
                changeTvStyle("english");
            }
        });

        chinaIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initUI();
                language = "china";
                changeTvStyle("china");
            }
        });
        chineseTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initUI();
                language = "china";
                changeTvStyle("china");
            }
        });

        vietnamIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initUI();
                language = "vietnam";
                changeTvStyle("vietnam");
            }
        });
        vietnamTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initUI();
                language = "vietnam";
                changeTvStyle("vietnam");
            }
        });

        japanIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initUI();
                language = "japan";
                changeTvStyle("japan");
            }
        });
        japanTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initUI();
                language = "japan";
                changeTvStyle("japanTv");
            }
        });
    }

    //선택된 언어에 맞춰 TextView 의 텍스트를 설정하는 함수
    private void setLanguageUI(String language) {

        lang_change_foreign = (TextView) findViewById(R.id.lang_change_foreign);

        switch (language) {
            case "english":
                lang_change_foreign.setText(R.string.change_language_en);
                break;
            case "china":
                lang_change_foreign.setText(R.string.change_language_cn);
                break;
            case "vietnam":
                lang_change_foreign.setText(R.string.change_language_vn);
                break;
            default:
                lang_change_foreign.setText(R.string.change_language_jp);
                break;
        }
    }

    //MainActivity 화면으로 이동하는 함수
    public void goMainActivity() {
        startActivity(new Intent(getApplicationContext(), SettingActivity.class));
    }

    //SharedPreferences 에 선택한 언어 저장하는 함수
    private void saveLanguage(String language) {
        if (language == null) {
            Toast.makeText(this, "언어를 선택해주세요.", Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences sp = getSharedPreferences("Language", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("language", language);
            editor.commit();

            finish();
        }
    }

    //언어가 선택될 때마다 텍스트뷰 검정, 디폴트 스타일, 사이트 20sp로 초기화하는 함수
    private void initUI() {
        englishTv.setTextColor(getColor(R.color.black));
        chineseTv.setTextColor(getColor(R.color.black));
        vietnamTv.setTextColor(getColor(R.color.black));
        japanTv.setTextColor(getColor(R.color.black));

        englishTv.setTypeface(Typeface.DEFAULT);
        chineseTv.setTypeface(Typeface.DEFAULT);
        vietnamTv.setTypeface(Typeface.DEFAULT);
        japanTv.setTypeface(Typeface.DEFAULT);

        englishTv.setTextSize(Dimension.SP, 20);
        chineseTv.setTextSize(Dimension.SP, 20);
        vietnamTv.setTextSize(Dimension.SP, 20);
        japanTv.setTextSize(Dimension.SP, 20);
    }

    //선택된 언어의 텍스트뷰를 노란색, Bold Style, 24sp 로 변경하는 함수
    private void changeTvStyle(String language) {
        switch (language) {
            case "english":
                englishTv.setTextColor(getColor(R.color.yellow));
                englishTv.setTypeface(Typeface.DEFAULT_BOLD);
                englishTv.setTextSize(Dimension.SP, 24);
                break;
            case "china":
                chineseTv.setTextColor(getColor(R.color.yellow));
                chineseTv.setTypeface(Typeface.DEFAULT_BOLD);
                chineseTv.setTextSize(Dimension.SP, 24);
                break;
            case "vietnam":
                vietnamTv.setTextColor(getColor(R.color.yellow));
                vietnamTv.setTypeface(Typeface.DEFAULT_BOLD);
                vietnamTv.setTextSize(Dimension.SP, 24);
                break;
            default:
                japanTv.setTextColor(getColor(R.color.yellow));
                japanTv.setTypeface(Typeface.DEFAULT_BOLD);
                japanTv.setTextSize(Dimension.SP, 24);
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