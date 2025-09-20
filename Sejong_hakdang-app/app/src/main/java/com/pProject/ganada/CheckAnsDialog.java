package com.pProject.ganada;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CheckAnsDialog extends Dialog {
    private TextView txt_contents, foreignText3, foreignText4, foreignText5, foreignText6, foreignText7, foreignText8;
    private Button shutdownClick;

    int Currentstate = 5;
    int Stagenum;

    ImageView resultimg;

    @SuppressLint("ResourceAsColor")
    public CheckAnsDialog(@NonNull Context context, String contents, int ans, int select, int Current, int stage, int score) {
        super(context);
        setContentView(R.layout.activity_custom_dialog);

        Stagenum = stage;
        Currentstate = Current;

        txt_contents = findViewById(R.id.txt_contents);
        foreignText3 = findViewById(R.id.txt_contents); // 추가된 외국어 텍스트뷰
        foreignText4 = findViewById(R.id.txt_contents); // 추가된 외국어 텍스트뷰
        foreignText5 = findViewById(R.id.foreign_text5);
        foreignText6 = findViewById(R.id.foreign_text5);
        foreignText7 = findViewById(R.id.foreign_text5);
        foreignText8 = findViewById(R.id.txt_contents);
        shutdownClick = findViewById(R.id.btn_shutdown);
        resultimg = findViewById(R.id.resultimgview);

        txt_contents.setText(contents);

        // 언어 설정
        SharedPreferences prefs = getContext().getSharedPreferences("Language", Context.MODE_PRIVATE);
        String language = prefs.getString("language", "english");
        setLanguageUI(language);
        setLanguageUI2(language);
        setLanguageUI3(language);
        setLanguageUI4(language);
        setLanguageUI5(language);
        setLanguageUI6(language);

        if (ans == select) {  // 정답인 경우
            setLanguageUI3(language);
            setLanguageUI(language);
        resultimg.setImageResource(R.drawable.great);  // 정답 이미지
        } else {  // 오답인 경우
            setLanguageUI2(language);
            resultimg.setImageResource(R.drawable.incorrect);  // 오답 이미지로 변경
            setLanguageUI4(language);
        }

        shutdownClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppValue appValue = (AppValue) getContext().getApplicationContext();
                int currentState = appValue.getCurrentstate(); // Currentstate 가져오기

                // 문제 제한: Currentstate >= 5인 경우
                if (currentState >= 5) {
                    setLanguageUI6(language);
                    resultimg.setImageResource(R.drawable.fanal);
                    setLanguageUI5(language);

                    // 기존 리스너 제거 후 새 리스너 설정
                    shutdownClick.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            getContext().startActivity(intent);
                            dismiss(); // 다이얼로그 닫기
                        }
                    });
                    return;
                }

                // 정답일 경우
                if (ans == select) {
                    int tmpTry = appValue.getTry();
                    int tmpLastScore = appValue.getLastscore();
                    tmpLastScore = tmpLastScore - tmpTry + 1;
                    appValue.setLastscore(tmpLastScore);
                    appValue.setTry(0);

                    appValue.setCurrentstate(currentState + 1);

                    Intent intent = new Intent(getContext().getApplicationContext(), Select4Q.class);
                    intent.putExtra("stageIndex", Stagenum);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    getContext().startActivity(intent);
                } else {  // 오답일 경우
                    // "다시 시도하기" 버튼을 눌렀을 때, 동일한 문제로 돌아가기
                    Intent intent = new Intent(getContext().getApplicationContext(), Select4Q.class);
                    intent.putExtra("Currentstate", Current);  // 같은 문제로 돌아가기 위해 Currentstate 그대로 전달
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // 액티비티 종료 후 새로 시작

                    dismiss();  // 다이얼로그 종료
                }
            }
        });
    }

    private void setLanguageUI(String language) {
        // 외국어 텍스트 업데이트
        switch (language) {
            case "english":
                foreignText3.setText(getContext().getString(R.string.Correct_answer_en));
                break;
            case "china":
                foreignText3.setText(getContext().getString(R.string.Correct_answer_cn));
                break;
            case "vietnam":
                foreignText3.setText(getContext().getString(R.string.Correct_answer_vn));
                break;
            case "japan":
                foreignText3.setText(getContext().getString(R.string.Correct_answer_jp));
                break;
            default:
                foreignText3.setText("");
                break;
        }
    }

    private void setLanguageUI2(String language) {
        // 외국어 텍스트 업데이트
        switch (language) {
            case "english":
                foreignText4.setText(getContext().getString(R.string.wrong_anwser_en));
                break;
            case "china":
                foreignText4.setText(getContext().getString(R.string.wrong_anwser_cn));
                break;
            case "vietnam":
                foreignText4.setText(getContext().getString(R.string.wrong_anwser_vn));
                break;
            case "japan":
                foreignText4.setText(getContext().getString(R.string.wrong_anwser_jp));
                break;
            default:
                foreignText4.setText("");
                break;
        }
    }

    private void setLanguageUI3(String language) {
        // 외국어 텍스트 업데이트
        switch (language) {
            case "english":
                foreignText5.setText(getContext().getString(R.string.next_en));
                break;
            case "china":
                foreignText5.setText(getContext().getString(R.string.next_cn));
                break;
            case "vietnam":
                foreignText5.setText(getContext().getString(R.string.next_vn));
                break;
            case "japan":
                foreignText5.setText(getContext().getString(R.string.next_jp));
                break;
            default:
                foreignText5.setText("");
                break;
        }
    }

    private void setLanguageUI4(String language) {
        // 외국어 텍스트 업데이트
        switch (language) {
            case "english":
                foreignText6.setText(getContext().getString(R.string.try_again_en));
                break;
            case "china":
                foreignText6.setText(getContext().getString(R.string.try_again_cn));
                break;
            case "vietnam":
                foreignText6.setText(getContext().getString(R.string.try_again_vn));
                break;
            case "japan":
                foreignText6.setText(getContext().getString(R.string.try_again_jp));
                break;
            default:
                foreignText6.setText("");
                break;
        }
    }

    private void setLanguageUI5(String language) {
        // 외국어 텍스트 업데이트
        switch (language) {
            case "english":
                foreignText7.setText(getContext().getString(R.string.finish_en));
                break;
            case "china":
                foreignText7.setText(getContext().getString(R.string.finish_cn));
                break;
            case "vietnam":
                foreignText7.setText(getContext().getString(R.string.finish_vn));
                break;
            case "japan":
                foreignText7.setText(getContext().getString(R.string.finish_jp));
                break;
            default:
                foreignText7.setText("");
                break;
        }
    }

    private void setLanguageUI6(String language) {
        // 외국어 텍스트 업데이트
        switch (language) {
            case "english":
                foreignText8.setText(getContext().getString(R.string.answer_finish_en));
                break;
            case "china":
                foreignText8.setText(getContext().getString(R.string.answer_finish_cn));
                break;
            case "vietnam":
                foreignText8.setText(getContext().getString(R.string.answer_finish_vn));
                break;
            case "japan":
                foreignText8.setText(getContext().getString(R.string.answer_finish_jp));
                break;
            default:
                foreignText8.setText("");
                break;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
