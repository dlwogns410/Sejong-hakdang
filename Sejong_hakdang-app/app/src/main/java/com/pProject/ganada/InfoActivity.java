package com.pProject.ganada;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {

    private ImageButton btn_back;
    private TextView info_foreign, app_explanation_foreign;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //상태바 설정
        View view = getWindow().getDecorView();
        if (view != null) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.parseColor("#80FFF2CC"));
        }

        btn_back = (ImageButton) findViewById(R.id.btn_back);

        //sharedPreferences 에서 선택된 언어 가져오기
        language = getSharedPreferences("Language", MODE_PRIVATE).getString("language", null);
        setLanguageUI(language);    //선택된 언어에 맞춰 TextView 의 텍스트를 설정하는 함수 호출

        //뒤로가기 버튼
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //선택된 언어에 맞춰 TextView 의 텍스트를 설정하는 함수
    private void setLanguageUI(String language) {

        info_foreign = (TextView) findViewById(R.id.info_foreign);
        app_explanation_foreign = (TextView) findViewById(R.id.app_explanation_foreign);

        switch (language) {
            case "english":
                info_foreign.setText(R.string.information_en);
                app_explanation_foreign.setText(R.string.information_detail_en);
                break;
            case "china":
                info_foreign.setText(R.string.information_cn);
                app_explanation_foreign.setText(R.string.information_detail_cn);
                break;
            case "vietnam":
                info_foreign.setText(R.string.information_vn);
                app_explanation_foreign.setText(R.string.information_detail_vn);
                break;
            default:
                info_foreign.setText(R.string.information_jp);
                app_explanation_foreign.setText(R.string.information_detail_jp);
                break;
        }
    }
}