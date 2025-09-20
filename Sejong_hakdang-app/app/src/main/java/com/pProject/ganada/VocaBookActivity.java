package com.pProject.ganada;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class VocaBookActivity extends AppCompatActivity {

    private List<Voca> vocaList;
    private VocaDB vocaDB = null;
    private Context mContext = null;
    private VocaAdapter vocaAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mysrl;

    private TextView voca_note_foreign;
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_book);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setNestedScrollingEnabled(false);
        mContext = getApplicationContext();
        vocaAdapter = new VocaAdapter(VocaBookActivity.this, vocaList);

        vocaList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        mysrl = findViewById(R.id.refresh_layout);

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

        //DB 생성
        vocaDB = VocaDB.getInstance(this);

        //DB getAll();
        class InsertRunnable implements Runnable {
            @Override
            public void run() {
                try {
                    vocaList = VocaDB.getInstance(mContext).vocaDao().getAll();
                    vocaAdapter = new VocaAdapter(VocaBookActivity.this, vocaList);
                    vocaAdapter.notifyDataSetChanged();

                    mRecyclerView.setAdapter(vocaAdapter);
                    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                    mRecyclerView.setLayoutManager(mLinearLayoutManager);
                } catch (Exception e) {

                }
            }
        }
        InsertRunnable insertRunnable = new InsertRunnable();
        Thread t = new Thread(insertRunnable);
        t.start();

        mysrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                InsertRunnable insertRunnable = new InsertRunnable();
                Thread t = new Thread(insertRunnable);
                t.start();

                mysrl.setRefreshing(false);
            }
        });
    }

    //선택된 언어에 맞춰 TextView 의 텍스트를 설정하는 함수
    private void setLanguageUI(String language) {

        voca_note_foreign = (TextView) findViewById(R.id.voca_note_foreign);

        switch (language) {
            case "english":
                voca_note_foreign.setText(R.string.vocabulary_en);
                break;
            case "china":
                voca_note_foreign.setText(R.string.vocabulary_cn);
                break;
            case "vietnam":
                voca_note_foreign.setText(R.string.vocabulary_vn);
                break;
            default:
                voca_note_foreign.setText(R.string.vocabulary_jp);
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