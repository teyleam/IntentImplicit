package com.example.intentimplicit;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.os.BuildCompat;

import android.Manifest;
import android.adservices.appsetid.AppSetIdManager;
import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.util.ULocale;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.PixelCopy;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.internal.StaticLayoutBuilderConfigurer;

import java.net.URI;

public class MainActivity extends AppCompatActivity {
    Button mBtnGallery, mBtnCamera;
    ImageView mImg;

    int REQUEST_CODE_CAMERA = 123;
    int REQUEST_CODE_GALLERY = 456;
    ActivityResultLauncher<Intent> mResultCamera;
    ActivityResultLauncher<Intent> mResultGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnCamera = findViewById(R.id.buttonCamera);
        mBtnGallery = findViewById(R.id.buttonGallery);
        mImg = findViewById(R.id.imageView);


        mBtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLaucherCamera.launch(Manifest.permission.CAMERA);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {

            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
        private ActivityResultLauncher<String> requestLaucherCamera = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if (result) {
                            requestLauncherOpenCamera.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));

                        } else {
                            if (ActivityCompat.checkSelfPermission(MainActivity.this,
                                    android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(
                                        MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA
                                );
                            } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Camera permission go to setting enable");
                                builder.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", null, null);
                                        intent.setData(uri);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestLaucherCamera.launch(Manifest.permission.CAMERA);
                                    }
                                }).show();
                            } else {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", null, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    }
                });


    private ActivityResultLauncher<Intent> requestLauncherOpenCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK && result.getData() != null){
             Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
             mImg.setImageBitmap(bitmap);
            }

        }
    });

        }