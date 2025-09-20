package com.pProject.ganada;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface CaptionRetrofitInterface {

    @Multipart
    @POST("/picture-caption")
    Call<Caption> pictureCaption(@Part MultipartBody.Part file);

    @Multipart
    @POST("/gallery-caption")
    Call<Caption> galleryCaption(@Part MultipartBody.Part file);

    @Multipart
    @POST("/text-recognition") // 경로를 수정했습니다.
    Call<Caption> textRecognition(@Part MultipartBody.Part file);

    @GET("/test-api")
    Call<JsonObject> test();
}
