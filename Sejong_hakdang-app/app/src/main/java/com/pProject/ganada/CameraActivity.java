package com.pProject.ganada;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class CameraActivity extends AppCompatActivity implements CaptionView {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    private String objectType;
    private PreviewView previewView;
    private ImageButton captureBtn;
    private ImageCapture imageCapture;
    private ProgressDialog progressDialog;
    private File outputFile;
    private CaptionService captionService;
    private ExampleParsingService exampleParsingService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        if (!hasCameraPermission()) {
            requestCameraPermission();
            return;
        }

        initializeCameraActivity();
    }

    private void initializeCameraActivity() {
        Intent intent = getIntent();
        objectType = intent.getStringExtra("objectType");

        captionService = new CaptionService();
        captionService.setCaptionView(this);

        exampleParsingService = new ExampleParsingService();
        exampleParsingService.setCaptionView(this);

        previewView = findViewById(R.id.viewFinder);

        outputFile = getOutputDirectory();
        startCamera();

        captureBtn = findViewById(R.id.camera_capture_btn);
        captureBtn.setOnClickListener(view -> takePhoto());
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeCameraActivity();
            } else {
                Toast.makeText(this, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture = ProcessCameraProvider.getInstance(this);
        imageCapture = new ImageCapture.Builder().build();

        cameraProviderListenableFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderListenableFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
    }

    private void takePhoto() {
        File photoFile = new File(
                outputFile,
                new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.KOREA).format(System.currentTimeMillis()) + ".jpg"
        );

        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                        onCaptionLoading();

                        if ("text".equals(objectType)) {
                            captionService.getTextCaption(photoFile);
                        } else {
                            captionService.getPictureCaption(photoFile);
                        }
                    }

                    @Override
                    public void onError(ImageCaptureException error) {
                        Log.e("CameraActivity", "Image capture error: " + error.getMessage());
                        onCaptionError("Image capture error: " + error.getMessage());
                    }
                });
    }

    private File getOutputDirectory() {
        File mediaDir = this.getFilesDir();
        if (mediaDir != null && mediaDir.exists()) {
            return mediaDir;
        } else {
            return getFilesDir();
        }
    }

    @Override
    public void onCaptionLoading() {
        progressDialog = new ProgressDialog(this);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();
    }

    @Override
    public void onCaptionSuccess(Uri uri, Caption caption) {
        Intent intent = new Intent(this, LearnWordActivity.class);
        intent.putExtra("type", objectType);
        intent.putExtra("uri", uri.toString());

        if ("text".equals(objectType)) {
            intent.putExtra("recognizedText", caption.getWord());
            intent.putExtra("exam", caption.getExample());
        } else if ("object".equals(objectType)) {
            String recognizedKind = caption.getKind() != null ? caption.getKind() : "알 수 없음";
            String message = caption.getMessage() != null ? caption.getMessage() : "예문 없음";
            intent.putExtra("recognizedText", recognizedKind);
            intent.putExtra("exam", message);
        }

        startActivity(intent);
        progressDialog.dismiss();
    }

    @Override
    public void onCaptionError(String errorMessage) {
        Toast.makeText(this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
        Log.e("CameraActivity", "onCaptionError: " + errorMessage);
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
