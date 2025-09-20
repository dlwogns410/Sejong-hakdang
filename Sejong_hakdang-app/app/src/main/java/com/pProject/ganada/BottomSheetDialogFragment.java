package com.pProject.ganada;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import gun0912.tedimagepicker.builder.TedImagePicker;

public class BottomSheetDialogFragment extends com.google.android.material.bottomsheet.BottomSheetDialogFragment implements CaptionView {

    private String language, objectType;
    private TextView usingCameraOtherLanguageTv, usingGalleryOtherLanguageTv;
    private View usingCameraView, usingGalleryView;
    private CaptionService captionService;
    private ProgressDialog progressDialog;

    public BottomSheetDialogFragment(String objectType) {
        this.objectType = objectType;
    }

    // 카메라 권한 확인
    private final ActivityResultLauncher<String> cameraPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        startCameraActivity();
                    } else {
                        Toast.makeText(requireContext(), "카메라 사용 권한을 허용해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    dismiss();
                }
            }
    );

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        captionService = new CaptionService();
        captionService.setCaptionView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);

        usingCameraOtherLanguageTv = view.findViewById(R.id.camera_tv_other_language);
        usingGalleryOtherLanguageTv = view.findViewById(R.id.gallery_tv_other_language);
        usingCameraView = view.findViewById(R.id.camera_view);
        usingGalleryView = view.findViewById(R.id.gallery_view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usingCameraView.setOnClickListener(v -> cameraPermissionLauncher.launch(Manifest.permission.CAMERA));
        usingGalleryView.setOnClickListener(v -> getImage());
    }

    @Override
    public void onResume() {
        super.onResume();
        language = requireActivity().getSharedPreferences("Language", MODE_PRIVATE).getString("language", "default");
        setLanguageUI(language);
    }

    private void setLanguageUI(String language) {
        switch (language) {
            case "english":
                usingCameraOtherLanguageTv.setText(R.string.using_camera_en);
                usingGalleryOtherLanguageTv.setText(R.string.using_gallery_en);
                break;
            case "china":
                usingCameraOtherLanguageTv.setText(R.string.using_camera_cn);
                usingGalleryOtherLanguageTv.setText(R.string.using_gallery_cn);
                break;
            case "vietnam":
                usingCameraOtherLanguageTv.setText(R.string.using_camera_vn);
                usingGalleryOtherLanguageTv.setText(R.string.using_gallery_vn);
                break;
            default:
                usingCameraOtherLanguageTv.setText(R.string.using_camera_jp);
                usingGalleryOtherLanguageTv.setText(R.string.using_gallery_jp);
                break;
        }
    }

    private void startCameraActivity() {
        Intent intent = new Intent(requireActivity(), CameraActivity.class);
        intent.putExtra("objectType", objectType);
        startActivity(intent);
    }

    private void getImage() {
        TedImagePicker.with(requireContext()).start(uri -> {
            onCaptionLoading();

            File file = new File(getRealPathFromURI(uri));
            if (file.exists()) {
                if ("text".equals(objectType)) {
                    captionService.getTextCaption(file);
                } else {
                    captionService.getGalleryCaption(uri);
                }
            } else {
                onCaptionError("파일 경로를 찾을 수 없습니다.");
            }
        });
    }

    @Override
    public void onCaptionLoading() {
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.show();
    }

    @Override
    public void onCaptionSuccess(Uri uri, Caption caption) {
        Log.d("BottomSheetDialogFragment", "Caption: " + caption.toString());
        ((MainActivity) requireActivity()).startLearnWord(objectType, uri, caption);
        progressDialog.dismiss();
        dismiss();
    }

    @Override
    public void onCaptionError(String errorMessage) {
        Toast.makeText(requireContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
        Log.e("BottomSheetDialogFragment", "Error: " + errorMessage);
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        try (Cursor cursor = requireContext().getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                return cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            Log.e("BottomSheetDialogFragment", "Error getting real path from URI", e);
        }
        return null;
    }
}
