package com.pProject.ganada;

import android.net.Uri;

public interface CaptionView {
    /**
     * 캡션 로딩 중 상태 알림
     */
    void onCaptionLoading();

    /**
     * 캡션 처리 성공 시 호출
     *
     * @param uri     - 처리된 이미지의 URI
     * @param caption - 캡션 데이터 객체
     */
    void onCaptionSuccess(Uri uri, Caption caption);

    /**
     * 캡션 처리 중 오류 발생 시 호출
     *
     * @param errorMessage - 오류 메시지
     */
    void onCaptionError(String errorMessage);
}
