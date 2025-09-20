package com.pProject.ganada;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import android.util.Log;

public class LearnWordActivity extends AppCompatActivity {

    private VocaDB vocaDB = null;
    private Context mContext;

    private TextToSpeech tts;
    private TextView word, example_sentence, practice_foreign, exampleDescKrTv, exampleDescOtherLangTv;
    private ImageButton btn_back, btn_word_pronunciation, btn_sentence_pronunciation;
    private CheckBox btn_bookmark;
    private ImageView word_pic;
    private String language, type;
    private View practice_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_word);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                tts.setLanguage(Locale.KOREAN);
                tts.setSpeechRate(0.75f);
            }
        });

        //intent 를 통해 image Uri, 인식된 텍스트, 예문을 전달 받음.
        Intent intent = getIntent();
        type = intent.getStringExtra("type");    //사물인식인지 텍스트인식인지 판단용(object/text)
        Uri uri = Uri.parse(intent.getStringExtra("uri"));
        String text = intent.getStringExtra("recognizedText");
        String exam = intent.getStringExtra("exam");

        //sharedPreferences 에서 선택된 언어 가져오기
        language = getSharedPreferences("Language", MODE_PRIVATE).getString("language", null);
        setLanguageUI();    //선택된 언어에 맞춰 TextView 의 텍스트를 설정하는 함수 호출

        //전달 받은 텍스트로 UI 바인딩
        word = (TextView) findViewById(R.id.word);
        word.setText(text);

        //전달 받은 uri 로 UI 바인딩
        word_pic = (ImageView) findViewById(R.id.word_pic);
        word_pic.setImageURI(uri);

        //예문 설명 텍스트뷰 UI 바인딩
        exampleDescKrTv = (TextView) findViewById(R.id.ex_sentence_desc_kr_tv);
        if (type.equals("text"))
            exampleDescKrTv.setText(R.string.explain_recog_sentence_kr);
        else
            exampleDescKrTv.setText(R.string.explain_ex_sentence_kr);

        //전달 받은 exam 로 UI 바인딩
        example_sentence = (TextView) findViewById(R.id.ex_sentence);
        example_sentence.setText(emphasizeWord(exam, text));

        //단어 스피커 이미지버튼 클릭 리스너
        btn_word_pronunciation = (ImageButton) findViewById(R.id.btn_word_pronunciation);
        btn_word_pronunciation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speakOut(word.getText().toString());
            }
        });

        //예문 스피커 이미지버튼 클릭 리스너
        btn_sentence_pronunciation = (ImageButton) findViewById(R.id.btn_sentence_pronunciation);
        btn_sentence_pronunciation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speakOut(example_sentence.getText().toString());
            }
        });

        btn_bookmark = (CheckBox) findViewById(R.id.btn_bookmark);
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        practice_view = (View) findViewById(R.id.practice_view);

        //DB 생성
        vocaDB = VocaDB.getInstance(this);
        mContext = getApplicationContext();

        //단어 추가
        class InsertRunnable implements Runnable {
            @Override
            public void run() {
                Voca voca = new Voca();
                voca.type = type;   //type 추가
                voca.word = word.getText().toString();
                voca.ex_sentence = example_sentence.getText().toString();
                voca.picture_uri = uri.toString();
                VocaDB.getInstance(mContext).vocaDao().insert(voca);
            }
        }

        //단어 삭제
        class DeleteRunnable implements Runnable {
            @Override
            public void run() {
                Voca voca = new Voca();
                VocaDB.getInstance(mContext).vocaDao().delete(VocaDB.getInstance(mContext).vocaDao().findByUri(uri.toString()));
            }
        }

        //북마크 버튼(단어 추가, 단어 삭제기능 연결)
        btn_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_bookmark.isChecked()) {
                    //insert DB
                    InsertRunnable insertRunnable = new InsertRunnable();
                    Thread addThread = new Thread(insertRunnable);
                    addThread.start();

                    switch (language) {
                        case "english":
                            Toast.makeText(getApplicationContext(), getString(R.string.save_word_en), Toast.LENGTH_SHORT).show();
                            break;
                        case "china":
                            Toast.makeText(getApplicationContext(), getString(R.string.save_word_cn), Toast.LENGTH_SHORT).show();
                            break;
                        case "vietnam":
                            Toast.makeText(getApplicationContext(), getString(R.string.save_word_vn), Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), getString(R.string.save_word_jp), Toast.LENGTH_SHORT).show();
                            break;
                    }
                    /*TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    if(v != null) v.setGravity(Gravity.CENTER);*/
                } else {
                    //delete DB
                    DeleteRunnable deleteRunnable = new DeleteRunnable();
                    Thread deleteThread = new Thread(deleteRunnable);
                    deleteThread.start();

                    switch (language) {
                        case "english":
                            Toast.makeText(getApplicationContext(), getString(R.string.delete_word_en), Toast.LENGTH_SHORT).show();
                            break;
                        case "china":
                            Toast.makeText(getApplicationContext(), getString(R.string.delete_word_cn), Toast.LENGTH_SHORT).show();
                            break;
                        case "vietnam":
                            Toast.makeText(getApplicationContext(), getString(R.string.delete_word_vn), Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), getString(R.string.delete_word_jp), Toast.LENGTH_SHORT).show();
                            break;
                    }

                    /*TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    if(v != null) v.setGravity(Gravity.CENTER);*/
                }
            }
        });

        //따라쓰기 페이지로 이동
        practice_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PracticeWordActivity.class);
                intent.putExtra("word", word.getText());
                startActivity(intent);
            }
        });

        //뒤로가기 버튼
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    //선택된 언어에 맞춰 TextView 의 텍스트를 설정하는 함수
    private void setLanguageUI() {
        practice_foreign = (TextView) findViewById(R.id.practice_foreign);
        exampleDescOtherLangTv = (TextView) findViewById(R.id.ex_sentence_desc_other_tv);

        if (type.equals("text"))
            setTextExDescTv();
        else
            setObjExDescTv();

        switch (language) {
            case "english":
                practice_foreign.setText(R.string.practice_en);
                break;
            case "china":
                practice_foreign.setText(R.string.practice_cn);
                break;
            case "vietnam":
                practice_foreign.setText(R.string.practice_vn);
                break;
            default:
                practice_foreign.setText(R.string.practice_jp);
                break;
        }
    }

    //사물 인식일 때 다국어 지원 텍스트뷰 UI 바인딩 함수
    private void setObjExDescTv() {
        switch (language) {
            case "english":
                exampleDescOtherLangTv.setText(R.string.explain_ex_sentence_en);
                break;
            case "china":
                exampleDescOtherLangTv.setText(R.string.explain_ex_sentence_cn);
                break;
            case "vietnam":
                exampleDescOtherLangTv.setText(R.string.explain_ex_sentence_vn);
                break;
            default:
                exampleDescOtherLangTv.setText(R.string.explain_ex_sentence_jp);
                break;
        }
    }

    //텍스트 인식일 때 다국어 지원 텍스트뷰 UI 바인딩 함수
    private void setTextExDescTv() {
        switch (language) {
            case "english":
                exampleDescOtherLangTv.setText(R.string.explain_recog_sentence_en);
                break;
            case "china":
                exampleDescOtherLangTv.setText(R.string.explain_recog_sentence_cn);
                break;
            case "vietnam":
                exampleDescOtherLangTv.setText(R.string.explain_recog_sentence_vn);
                break;
            default:
                exampleDescOtherLangTv.setText(R.string.explain_recog_sentence_jp);
                break;
        }
    }

    //예문에서 단어에 강조표시 하는 함수
    private String emphasizeWord(String example, String word) {
        Log.d("LearnWordActivity", example + word);
        int idx = example.indexOf(word);
        String temp;

        try {
            temp = example.substring(0, idx) + "\"" + word + "\"" + example.substring(idx + word.length());
        } catch (Exception e) {
            temp = example;
        }


        return temp;
    }

    //TTS 함수
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void speakOut(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

}