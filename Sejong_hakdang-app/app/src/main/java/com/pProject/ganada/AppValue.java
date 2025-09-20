package com.pProject.ganada;

import android.app.Application;

public class AppValue extends Application {
    private int trynum = 0;        // 시도 횟수 초기화
    private int Lastscore = 0;     // 마지막 점수 초기화
    private int Currentstate = 0;  // 현재 문제 번호 초기화

    public int getTry() {
        return trynum;
    }

    public void setTry(int trynum) {
        this.trynum = trynum;
    }

    public int getLastscore() {
        return Lastscore;
    }

    public void setLastscore(int Lastscore) {
        this.Lastscore = Lastscore;
    }

    public int getCurrentstate() {
        return Currentstate;
    }

    public void setCurrentstate(int Currentstate) {
        this.Currentstate = Currentstate;
    }

    // Currentstate 초기화 메서드 추가
    public void resetCurrentState() {
        this.Currentstate = 0;  // Currentstate를 0으로 초기화
    }
}
