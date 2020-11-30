package com.son.videotophoto;

import androidx.annotation.NonNull;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DialogAbout extends Dialog {
    TextView txtDK;
    Context context;

    public DialogAbout(@NonNull Context context) {
        super(context);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_about);
        AddEvents();
    }

    private void AddEvents() {
        txtDK = findViewById(R.id.txtDK);
        txtDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WebViewSetup.class);
                intent.putExtra("PUSHDATA", "https://codefresher.vn/");
                context.startActivity(intent);

            }
        });
    }
}