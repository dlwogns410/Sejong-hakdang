package com.pProject.ganada;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

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

public class CaptionService {

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
                    .baseUrl("http://203.234.62.199:8000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return RETROFIT;
    }

    public static CaptionRetrofitInterface getCaptionRetrofitInterface() {
        return getRetrofit().create(CaptionRetrofitInterface.class);
    }

    public void setCaptionView(CaptionView captionView) {
        this.captionView = captionView;
    }

    public void getPictureCaption(File photoFile) {
        if (!photoFile.exists()) {
            Log.e("CaptionService", "File does not exist: " + photoFile.getAbsolutePath());
            captionView.onCaptionError("File does not exist");
            return;
        }

        MultipartBody.Part file = getImageMultipartBody(photoFile);

        getCaptionRetrofitInterface().pictureCaption(file).enqueue(new Callback<Caption>() {
            @Override
            public void onResponse(Call<Caption> call, Response<Caption> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Caption caption = response.body();
                    Log.d("CaptionService", "Picture Caption Response: " + caption.toString());
                    captionView.onCaptionSuccess(Uri.fromFile(photoFile), caption);
                } else {
                    Log.e("CaptionService", "Picture Caption Error Response: " + response.message());
                    captionView.onCaptionError("Invalid picture response: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Caption> call, Throwable t) {
                Log.e("CaptionService", "getPictureCaption ERROR: " + t.toString());
                captionView.onCaptionError("Picture caption failed: " + t.getMessage());
            }
        });
    }

    public void getTextCaption(File photoFile) {
        if (!photoFile.exists()) {
            Log.e("CaptionService", "File does not exist: " + photoFile.getAbsolutePath());
            captionView.onCaptionError("File does not exist");
            return;
        }

        MultipartBody.Part file = getImageMultipartBody(photoFile);

        getCaptionRetrofitInterface().textRecognition(file).enqueue(new Callback<Caption>() {
            @Override
            public void onResponse(Call<Caption> call, Response<Caption> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Caption caption = response.body();
                    Log.d("CaptionService", "Text Caption Response: " + caption.toString());
                    captionView.onCaptionSuccess(Uri.fromFile(photoFile), caption);
                } else {
                    Log.e("CaptionService", "Text Caption Error Response: " + response.message());
                    captionView.onCaptionError("Invalid text response: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Caption> call, Throwable t) {
                Log.e("CaptionService", "getTextCaption ERROR: " + t.toString());
                captionView.onCaptionError("Text caption failed: " + t.getMessage());
            }
        });
    }

    public void getGalleryCaption(Uri uri) {
        String filePath = getRealPathFromURI(uri);
        if (filePath == null) {
            Log.e("CaptionService", "Invalid Uri: " + uri);
            captionView.onCaptionError("Invalid Uri");
            return;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            Log.e("CaptionService", "File does not exist: " + filePath);
            captionView.onCaptionError("File does not exist for the given Uri.");
            return;
        }

        MultipartBody.Part photoFile = getImageMultipartBody(file);

        getCaptionRetrofitInterface().galleryCaption(photoFile).enqueue(new Callback<Caption>() {
            @Override
            public void onResponse(Call<Caption> call, Response<Caption> response) {
                if (response.isSuccessful() && response.body() != null) {
                    captionView.onCaptionSuccess(uri, response.body());
                } else {
                    captionView.onCaptionError("Gallery Caption Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Caption> call, Throwable t) {
                captionView.onCaptionError("Gallery Caption Failed: " + t.getMessage());
            }
        });
    }

    private MultipartBody.Part getImageMultipartBody(File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        return MultipartBody.Part.createFormData("file", file.getName(), requestFile);
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Context context = ((Context) captionView);
        try (Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                return cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            Log.e("CaptionService", "Error getting real path from URI", e);
        }
        return null;
    }
}
