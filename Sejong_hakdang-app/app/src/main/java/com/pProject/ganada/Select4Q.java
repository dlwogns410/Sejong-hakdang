package com.pProject.ganada;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.res.ColorStateList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Random;

public class Select4Q extends AppCompatActivity {

    private CheckAnsDialog customDialog;

    String[] wordlist = {"아이", "화가", "뿌리", "가수", "오리", "파도", "부모", "까치", "의자"};

    int answer;
    int currenchk = 99;
    Button A1, A2, A3, A4, btn_check;
    ImageButton imgA1, imgA2, imgA3, imgA4;
    ImageView ans_Img;
    TextView select4_text, foreignText, ans_word, foreignText2; // 한국어와 외국어 텍스트뷰

    int count = 4;
    int[] a = new int[count];

    int Currentstate = 1;
    int Stagenum;
    int QuestionType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select4_q);

        // 툴바 설정
        setupToolbar();

        // 텍스트뷰 초기화
        select4_text = findViewById(R.id.select4_text);
        foreignText = findViewById(R.id.foreign_text);
        foreignText2 = findViewById(R.id.foreign_text2);
        ans_word = findViewById(R.id.ans_word);

        A1 = findViewById(R.id.Q4_ans1);
        A2 = findViewById(R.id.Q4_ans2);
        A3 = findViewById(R.id.Q4_ans3);
        A4 = findViewById(R.id.Q4_ans4);
        ans_Img = findViewById(R.id.ans_img);

        imgA1 = findViewById(R.id.Img_Q4_ans1);
        imgA2 = findViewById(R.id.Img_Q4_ans2);
        imgA3 = findViewById(R.id.Img_Q4_ans3);
        imgA4 = findViewById(R.id.Img_Q4_ans4);

        // QuestionType 설정 (0: 단어 선택, 1: 그림 선택)
        Random rnd = new Random();
        QuestionType = rnd.nextInt(2);

        // 문제 생성
        makeQuestion();
        resetButtonColors();
        makeImg();

        if (QuestionType == 0) {
            ans_word.setText(wordlist[answer]);

        } else {
            A1.setVisibility(View.VISIBLE);
            A2.setVisibility(View.VISIBLE);
            A3.setVisibility(View.VISIBLE);
            A4.setVisibility(View.VISIBLE);
            ans_Img.setVisibility(View.VISIBLE);

            imgA1.setVisibility(View.INVISIBLE);
            imgA2.setVisibility(View.INVISIBLE);
            imgA3.setVisibility(View.INVISIBLE);
            imgA4.setVisibility(View.INVISIBLE);
            ans_word.setVisibility(View.INVISIBLE);
        }

        // 언어 설정
        String language = getSharedPreferences("Language", MODE_PRIVATE).getString("language", "english");
        setLanguageUI("korea", language); // 기본 언어는 한국어, 선택된 언어 추가 표시
        setLanguagesubmit("korea", language);

        // 창의 투명도 설정
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        btn_check = findViewById(R.id.btn_check);
        btn_check.setOnClickListener(view -> {
            if (currenchk == 99) {
                Toast.makeText(getApplicationContext(), "정답을 선택하세요.", Toast.LENGTH_SHORT).show();
            } else {
                customDialog = new CheckAnsDialog(view.getContext(),
                        "[ " + wordlist[a[currenchk]] + " ]가 아닌 다른 것을 생각해봅시다.",
                        answer, a[currenchk], Currentstate, Stagenum, -99);
                customDialog.show();
            }
        });

        setupAnswerButtons();
        setupImageButtons();
    }

    // 툴바 설정 메서드
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar); // activity_select4_q.xml에 툴바 ID가 존재해야 함
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back); // 뒤로가기 아이콘
        }

        // 상태바 설정
        View view = getWindow().getDecorView();
        if (view != null) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); // 상태바 아이콘 밝게
            getWindow().setStatusBarColor(Color.parseColor("#FFF2CC")); // 상태바 색상 변경
        }
    }

    // 뒤로가기 버튼 클릭 처리
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(Select4Q.this, MainActivity.class);
            startActivity(intent);
            finish(); // 현재 액티비티 종료
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetButtonColors() {
        A1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
        A2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
        A3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
        A4.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));

        imgA1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
        imgA2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
        imgA3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));
        imgA4.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn_nomal)));

        currenchk = 99;
    }

    private void setupAnswerButtons() {
        A1.setOnClickListener(view -> handleButtonClick(A1, 0));
        A2.setOnClickListener(view -> handleButtonClick(A2, 1));
        A3.setOnClickListener(view -> handleButtonClick(A3, 2));
        A4.setOnClickListener(view -> handleButtonClick(A4, 3));
    }

    private void handleButtonClick(Button button, int index) {
        resetButtonColors();
        button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn)));
        currenchk = index;
    }

    private void setupImageButtons() {
        imgA1.setOnClickListener(view -> handleImageButtonClick(imgA1, 0));
        imgA2.setOnClickListener(view -> handleImageButtonClick(imgA2, 1));
        imgA3.setOnClickListener(view -> handleImageButtonClick(imgA3, 2));
        imgA4.setOnClickListener(view -> handleImageButtonClick(imgA4, 3));
    }

    private void handleImageButtonClick(ImageButton imageButton, int index) {
        resetButtonColors();
        imageButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.btn)));
        currenchk = index;
    }

    public void makeQuestion() {
        Random r = new Random();
        for (int i = 0; i < count; i++) {
            a[i] = r.nextInt(9);
            for (int j = 0; j < i; j++) {
                if (a[i] == a[j]) {
                    i--;
                    break;
                }
            }
        }
        answer = a[0];
        int k = r.nextInt(4);
        int temp = a[k];
        a[k] = a[0];
        a[0] = temp;

        A1.setText(wordlist[a[0]]);
        A2.setText(wordlist[a[1]]);
        A3.setText(wordlist[a[2]]);
        A4.setText(wordlist[a[3]]);

        makeImg_select(imgA1, a[0]);
        makeImg_select(imgA2, a[1]);
        makeImg_select(imgA3, a[2]);
        makeImg_select(imgA4, a[3]);


    }

    public void makeImg() {
        switch (answer) {
            case 0:
                ans_Img.setImageResource(R.drawable.kid);
                break;
            case 1:
                ans_Img.setImageResource(R.drawable.painter);
                break;
            case 2:
                ans_Img.setImageResource(R.drawable.root);
                break;
            case 3:
                ans_Img.setImageResource(R.drawable.singer);
                break;
            case 4:
                ans_Img.setImageResource(R.drawable.duck);
                break;
            case 5:
                ans_Img.setImageResource(R.drawable.wave);
                break;
            case 6:
                ans_Img.setImageResource(R.drawable.parents);
                break;
            case 7:
                ans_Img.setImageResource(R.drawable.magpie);
                break;
            case 8:
                ans_Img.setImageResource(R.drawable.chair);
                break;
        }
    }

    public void makeImg_select(ImageButton imgB, int here) {
        switch (here) {
            case 0:
                imgB.setImageResource(R.drawable.kid);
                break;
            case 1:
                imgB.setImageResource(R.drawable.painter);
                break;
            case 2:
                imgB.setImageResource(R.drawable.root);
                break;
            case 3:
                imgB.setImageResource(R.drawable.singer);
                break;
            case 4:
                imgB.setImageResource(R.drawable.duck);
                break;
            case 5:
                imgB.setImageResource(R.drawable.wave);
                break;
            case 6:
                imgB.setImageResource(R.drawable.parents);
                break;
            case 7:
                imgB.setImageResource(R.drawable.magpie);
                break;
            case 8:
                imgB.setImageResource(R.drawable.chair);
                break;
        }
    }

    private void setLanguageUI(String korean, String language) {
        // 한국어 텍스트
        select4_text.setText(getString(R.string.explain_quiz_kr));

        // 외국어 텍스트
        switch (language) {
            case "english":
                foreignText.setText(getString(R.string.explain_quiz_en));
                break;
            case "china":
                foreignText.setText(getString(R.string.explain_quiz_cn));
                break;
            case "vietnam":
                foreignText.setText(getString(R.string.explain_quiz_vn));
                break;
            case "japan":
                foreignText.setText(getString(R.string.explain_quiz_jp));
                break;
            default:
                foreignText.setText("");
                break;
        }
    }

    private void setLanguagesubmit(String korean, String language) {
        // 한국어 텍스트

        // 외국어 텍스트
        switch (language) {
            case "english":
                foreignText2.setText(getString(R.string.submit_answer_en));
                break;
            case "china":
                foreignText2.setText(getString(R.string.submit_answer_cn));
                break;
            case "vietnam":
                foreignText2.setText(getString(R.string.submit_answer_vn));
                break;
            case "japan":
                foreignText2.setText(getString(R.string.submit_answer_jp));
                break;
            default:
                foreignText2.setText("");
                break;
        }
    }
}
