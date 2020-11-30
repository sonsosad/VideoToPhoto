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

public class SizeDiaglog extends Dialog {
    RadioButton rd05x, rd1x, rd15x, rd2x, rd3x;
    ImageButton btnClose;
    TextView txtSize;
    Context context;
    String size = "";
    String defSize = "1x";

    public SizeDiaglog(@NonNull Context context,TextView txtSize) {
        super(context);
        this.context = context;
        this.txtSize = txtSize;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_size_diaglog);
        setUpView();
        addEvents();


    }

    private void addEvents() {
        rd05x.setOnCheckedChangeListener(listener);
        rd1x.setOnCheckedChangeListener(listener);
        rd2x.setOnCheckedChangeListener(listener);
        rd3x.setOnCheckedChangeListener(listener);
        rd15x.setOnCheckedChangeListener(listener);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void setUpView() {
        rd1x = findViewById(R.id.rd1x);
        rd2x = findViewById(R.id.rd2x);
        rd3x = findViewById(R.id.rd3x);
        rd05x = findViewById(R.id.rd05x);
        rd15x = findViewById(R.id.rd15x);
        btnClose = findViewById(R.id.btnClose);
        getDataSharePreference();
    }
    private  void getDataSharePreference(){
        SharedPreferences  sharedPreferences = context.getSharedPreferences("MyPreferences",Context.MODE_PRIVATE);
        size = sharedPreferences.getString("SIZE",defSize);
        checkSize(size);
    }

    private void checkSize(String Size) {
        if (size.equals("0.5x")){
            rd05x.setChecked(true);
        }
        if(size.equals("1x")){
            rd1x.setChecked(true);
        }
        if(size.equals("1.5x")){
            rd15x.setChecked(true);
        }
        if(size.equals("2x")){
            rd2x.setChecked(true);
        }
        if(size.equals("3x")){
            rd3x.setChecked(true);
        }

    }
    CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                String size = buttonView.getText().toString();
                saveSize(size);
                txtSize.setText(size);
            }
        }
    };

    private void saveSize(String size) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPreferences",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("SIZE", size);
        editor.commit();
    }
}