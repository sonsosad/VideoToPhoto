package com.son.videotophoto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.Toolbar;

import java.security.Permission;

public class MainActivity extends AppCompatActivity {
    public static final int PERMISSION_CODE_GALLLERY = 1;
    public static final int REQUEST_CODE_PICK_CAPTURE = 2;
    private Toolbar toolbar;
    private CardView cvShare, cvAbout, cvRate;
    Button btnGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        btnGallery = findViewById(R.id.btnGallery);
        Button btnSelect = findViewById(R.id.btnSelect);
        Button btnSetting = findViewById(R.id.btnSetting);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingDiaglog settingDiaglog = new SettingDiaglog(MainActivity.this);
                settingDiaglog.show();
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Gallery.class);
                startActivity(intent);
            }
        });

        showBottom();

    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISSION_CODE_GALLLERY);
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(MainActivity.this, ReadFolder.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE_GALLLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(MainActivity.this, "Permission denied...", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_CAPTURE && resultCode == RESULT_OK && data != null) {
            Intent intent = new Intent(MainActivity.this, ReadFolder.class);
            startActivity(intent);

        }
    }

    private void showBottom() {
        cvAbout = findViewById(R.id.cvAbout);
        cvRate = findViewById(R.id.cvRate);
        cvShare = findViewById(R.id.cvShare);
        cvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "");
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });

        cvRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.asquaremobileapps.videotoimageconverter")));
            }
        });

        cvAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAbout dialogAbout = new DialogAbout(MainActivity.this);
                dialogAbout.show();
            }
        });


    }
}