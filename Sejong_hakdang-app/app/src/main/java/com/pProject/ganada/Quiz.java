package com.pProject.ganada;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;


public class Quiz extends AppCompatActivity {
    ImageButton Stage1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        ((AppValue)getApplication()).setTry(0);     //전역변수 설정
        ((AppValue)getApplication()).setLastscore(0);       //전역변수 설정

        Stage1 = findViewById(R.id.button);


        Stage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Select4Q.class);
                //데이터 송신부
                intent.putExtra("Currentstate",1);
                intent.putExtra("stageIndex",0);
                startActivity(intent);	//문제 액티비티로 이동
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();	//액티비티 종료
            }
        });


    }
}