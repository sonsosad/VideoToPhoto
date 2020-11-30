package com.son.videotophoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SettingDiaglog extends Dialog {
    TextView txtSize, txtQuality, txtFileFormat;
    String defVal = "JPG";
    String defQuality = getContext().getResources().getString(R.string.High);
    String defSize = "1x";
    Context context;

    public SettingDiaglog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_diaglog);
        setUpView();
        addEvents();
    }

    private void getSharePreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String type = sharedPreferences.getString("TYPE", defVal);
        String quality = sharedPreferences.getString("QUALITY", defQuality);
        String size = sharedPreferences.getString("SIZE", defSize);
        txtFileFormat.setText(type);
        txtQuality.setText(quality);
        txtSize.setText(size);
    }

    private  void addEvents(){
        txtFileFormat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileFormatDialog fileFormatDialog = new FileFormatDialog(context,txtFileFormat);
                fileFormatDialog.show();
            }
        });

        txtQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QualityDialog qualityDialog = new QualityDialog(context,txtQuality);
                qualityDialog.show();
            }
        });
        txtSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SizeDiaglog sizeDiaglog = new SizeDiaglog(context,txtSize);
                sizeDiaglog.show();

            }
        });
    }
    private  void  setUpView(){
        txtFileFormat = findViewById(R.id.txtFileFormat);
        txtSize = findViewById(R.id.txtSize);
        txtQuality = findViewById(R.id.txtQuality);
        getSharePreferences();
    }
}
