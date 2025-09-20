package com.pProject.ganada;

import android.net.Uri;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExampleParsingService {

    private static Retrofit RETROFIT;

    private CaptionView captionView;

    public static Retrofit getRetrofit() {
        if (RETROFIT == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES)
                    .build();

            RETROFIT = new Retrofit.Builder()
                    .baseUrl("https://opendict.korean.go.kr/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return RETROFIT;
    }

    public static ExampleParsingRetrofitInterface getExampleRetrofitInterface() {
        return getRetrofit().create(ExampleParsingRetrofitInterface.class);
    }

    public void setCaptionView(CaptionView captionView) {
        this.captionView = captionView;
    }

    public void getExample(Uri uri, Caption caption) {
        String word = caption.getKind();

        getExampleRetrofitInterface().getExample(word).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    JsonObject jsonObject = response.body();
                    JsonObject wordInfoResult = jsonObject.getAsJsonObject("channel");
                    JsonArray wordInfo = wordInfoResult.getAsJsonArray("item");
                    String exam = wordInfo.get(0).getAsJsonObject().get("example").toString();
                    exam = exam.replaceAll("\\\"", "");
                    caption.setMessage(exam);
                    captionView.onCaptionSuccess(uri, caption);
                } else {
                    Log.e("ExampleParsingService", "getExample fail code: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("ExampleParsingService", "getExample error: " + t.toString());
            }
        });
    }

    private MultipartBody.Part getImageMultipartBody(Uri uri) {
        File file = new File(uri.getPath());
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        return MultipartBody.Part.createFormData("file", file.getName(), requestFile);
    }
}
