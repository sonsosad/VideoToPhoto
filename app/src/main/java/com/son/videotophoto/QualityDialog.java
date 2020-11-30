package com.son.videotophoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

public class QualityDialog extends Dialog {
    RadioButton rdBest, rdVeryHigh, rdHigh, rdLow, rdMedium;
    String defQuality = "High";
    String quality = "";
    ImageButton btnClose;
    TextView txtQuality;
    Context context;

    public QualityDialog(@NonNull Context context, TextView txtQuality) {
        super(context);
        this.context = context;
        this.txtQuality = txtQuality;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quality_dialog);
        setUpView();
        addEvets();

    }

    private void setUpView() {
        rdBest = findViewById(R.id.rdBest);
        rdVeryHigh = findViewById(R.id.rdVrHigh);
        rdMedium = findViewById(R.id.rdMedium);
        rdHigh = findViewById(R.id.rdHigh);
        rdLow = findViewById(R.id.rdLow);
        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        getDataSharePreference();
    }

    private void getDataSharePreference() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        quality = sharedPreferences.getString("QUALITY", defQuality);
        checkQuality(quality);
    }

    private void checkQuality(String quality) {
        if (quality.equals(context.getResources().getString(R.string.Best))) {
            rdBest.setChecked(true);
        }
        if (quality.equals(context.getResources().getString(R.string.VeryHigh))) {
            rdVeryHigh.setChecked(true);
        }
        if (quality.equals(context.getResources().getString(R.string.High))) {
            rdHigh.setChecked(true);
        }
        if (quality.equals(context.getResources().getString(R.string.Medium))) {
            rdMedium.setChecked(true);
        }
        if (quality.equals(context.getResources().getString(R.string.Low))) {
            rdLow.setChecked(true);
        }
    }
    private void addEvets(){
        rdVeryHigh.setOnCheckedChangeListener(listener);
        rdBest.setOnCheckedChangeListener(listener);
        rdLow.setOnCheckedChangeListener(listener);
        rdHigh.setOnCheckedChangeListener(listener);
        rdMedium.setOnCheckedChangeListener(listener);
    }

    CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                String quality = buttonView.getText().toString();
                buttonView.setChecked(true);
                saveQuality(quality);
                txtQuality.setText(quality);
            }
        }
    };

    private void saveQuality(String quality) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("QUALITY",quality);
        editor.commit();
    }

}