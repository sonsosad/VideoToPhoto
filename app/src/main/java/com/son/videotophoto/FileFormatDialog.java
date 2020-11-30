package com.son.videotophoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

public class FileFormatDialog extends Dialog {
    RadioButton rdJPG, rdPNG;
    ImageButton btnClose;
    String fileFormat = "";
    String defType = "JPG";
    Context context;
    TextView txtFileFormat;

    public FileFormatDialog(@NonNull Context context, TextView txtFileFormat) {
        super(context);
        this.context = context;
        this.txtFileFormat = txtFileFormat;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_format_dialog);
        setUpView();
        addEvents();
    }

    private void getDataSharePreference() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        fileFormat = sharedPreferences.getString("TYPE", defType);
        checkQuality(fileFormat);

    }

    private void checkQuality(String quality) {
        if (quality.equals("JPG")) {
            rdJPG.setChecked(true);
        }if (quality.equals("PNG")){
            rdPNG.setChecked(true);
        }
    }
    private void addEvents(){
        rdJPG.setOnCheckedChangeListener(listener);
        rdPNG.setOnCheckedChangeListener(listener);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                String type = buttonView.getText().toString();
                buttonView.setChecked(true);
                saveType(type);
                txtFileFormat.setText(type);

            }
        }
    };
    private void saveType(String type){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPreferences",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TYPE",type);
        editor.commit();
    }
    private void setUpView(){
        rdPNG = findViewById(R.id.rdPNG);
        rdJPG = findViewById(R.id.rdJPG);
        btnClose = findViewById(R.id.btnClose);
        getDataSharePreference();
    }
}