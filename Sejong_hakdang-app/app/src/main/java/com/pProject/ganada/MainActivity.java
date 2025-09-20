package com.pProject.ganada;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Dialog;  // 추가된 import
import android.view.Window;  // 추가된 import

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView takePictureTv, vocaTv, settingTv;
    private View settingView;
    private View vocabularyView;
    private View quiz;
    private Dialog recognizeSelectDialog;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 이미지/텍스트 촬영 클릭 리스너
        View takePictureView = findViewById(R.id.take_picture_view);
        takePictureView.setOnClickListener(view -> startDialog());

        // 설정 클릭 리스너
        settingView = findViewById(R.id.setting_view);
        settingView.setOnClickListener(view -> startSettingActivity());

        // 단어장 클릭 리스너
        vocabularyView = findViewById(R.id.vocabulary_view);
        vocabularyView.setOnClickListener(view -> startVocaBookActivity());

        // 한글 퀴즈 클릭 리스너
        quiz = findViewById(R.id.hangeul_quiz);
        quiz.setOnClickListener(view -> startQuizActivity());
    }

    @Override
    protected void onResume() {
        super.onResume();
        language = getSharedPreferences("Language", MODE_PRIVATE).getString("language", null);
        setLanguageUI(language);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private void setLanguageUI(String language) {
        takePictureTv = findViewById(R.id.take_picture_tv_other_language);
        vocaTv = findViewById(R.id.voca_other_language_tv);
        settingTv = findViewById(R.id.quiz_other_language_tv);

        switch (language) {
            case "english":
                takePictureTv.setText(R.string.take_picture_en);
                vocaTv.setText(R.string.vocabulary_en);
                settingTv.setText(R.string.hangeulquiz_en);
                break;
            case "china":
                takePictureTv.setText(R.string.take_picture_cn);
                vocaTv.setText(R.string.vocabulary_cn);
                settingTv.setText(R.string.hanguelquiz_cn);
                break;
            case "vietnam":
                takePictureTv.setText(R.string.take_picture_vn);
                vocaTv.setText(R.string.vocabulary_vn);
                settingTv.setText(R.string.hanguelquiz_vn);
                break;
            default:
                takePictureTv.setText(R.string.take_picture_jp);
                vocaTv.setText(R.string.vocabulary_jp);
                settingTv.setText(R.string.hanguelquiz_jp);
                break;
        }
    }

    private void startDialog() {
        recognizeSelectDialog = new Dialog(this);
        recognizeSelectDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        recognizeSelectDialog.setContentView(R.layout.recognize_select_dialog);
        recognizeSelectDialog.show();

        setDialogLanguageUI(language);

        View objectRecognitionView = recognizeSelectDialog.findViewById(R.id.object_recognition_view);
        objectRecognitionView.setOnClickListener(view -> {
            startCameraActivity("object");
        });

        View textRecognitionView = recognizeSelectDialog.findViewById(R.id.text_recognition_view);
        textRecognitionView.setOnClickListener(view -> {
            startCameraActivity("text");
        });

        ImageView exitIv = recognizeSelectDialog.findViewById(R.id.exit_iv);
        exitIv.setOnClickListener(view -> recognizeSelectDialog.dismiss());
    }

    private void setDialogLanguageUI(String language) {
        TextView alertTv = recognizeSelectDialog.findViewById(R.id.alert_other_language_tv);
        TextView objectRecognitionTv = recognizeSelectDialog.findViewById(R.id.object_recognition_other_language_tv);
        TextView textRecognitionTv = recognizeSelectDialog.findViewById(R.id.text_recognition_other_language_tv);

        switch (language) {
            case "english":
                alertTv.setText(R.string.take_photo_alert_en);
                objectRecognitionTv.setText(R.string.object_recognition_en);
                textRecognitionTv.setText(R.string.text_recognition_en);
                break;
            case "china":
                alertTv.setText(R.string.take_photo_alert_cn);
                objectRecognitionTv.setText(R.string.object_recognition_cn);
                textRecognitionTv.setText(R.string.text_recognition_cn);
                break;
            case "vietnam":
                alertTv.setText(R.string.take_photo_alert_vn);
                objectRecognitionTv.setText(R.string.object_recognition_vn);
                textRecognitionTv.setText(R.string.text_recognition_vn);
                break;
            default:
                alertTv.setText(R.string.take_photo_alert_jp);
                objectRecognitionTv.setText(R.string.object_recognition_jp);
                textRecognitionTv.setText(R.string.text_recognition_jp);
                break;
        }
    }

    private void startCameraActivity(String objectType) {
        Intent intent = new Intent(MainActivity.this, CameraActivity.class);
        intent.putExtra("objectType", objectType);
        startActivity(intent);
        recognizeSelectDialog.dismiss();
    }

    private void startSettingActivity() {
        startActivity(new Intent(this, SettingActivity.class));
    }

    private void startVocaBookActivity() {
        startActivity(new Intent(this, VocaBookActivity.class));
    }

    private void startQuizActivity() {
        AppValue appValue = (AppValue) getApplicationContext();
        appValue.setCurrentstate(0);
        startActivity(new Intent(this, Select4Q.class));
    }

    void startLearnWord(String type, Uri uri, Caption caption) {
        Intent intent = new Intent(this, LearnWordActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("uri", uri.toString());

        // 텍스트 인식과 사물 인식 결과 구분 처리
        if ("text".equals(type)) {
            String recognizedText = caption.getWord() != null ? caption.getWord() : "알 수 없음";
            String example = caption.getExample() != null ? caption.getExample() : "예문 없음";
            intent.putExtra("recognizedText", recognizedText);
            intent.putExtra("exam", example);
        } else if ("object".equals(type)) {
            String recognizedText = caption.getWord() != null ? caption.getWord() : "알 수 없음";
            String example = caption.getExample() != null ? caption.getExample() : "이미지 캡셔닝 생성 오류";
            intent.putExtra("recognizedText", recognizedText);
            intent.putExtra("exam", example);
        }

        startActivity(intent);
    }
}
