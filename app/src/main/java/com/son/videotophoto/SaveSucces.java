package com.son.videotophoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class SaveSucces extends AppCompatActivity {
    Uri uri;
    public static final String FILE_PROVIDER_AUTHORITY = "com.son.videotophoto.fileprovider";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_succes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView imgV = findViewById(R.id.imgSucces);

        Intent intent = getIntent();
        String pathI = intent.getStringExtra("PATH SUCCES");
        uri = Uri.parse(pathI);
        Bitmap bitmap = BitmapFactory.decodeFile(pathI);
        imgV.setImageBitmap(bitmap);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnShareSave:

                Intent intent1 = new Intent(Intent.ACTION_SEND);
                intent1.setType("image/*");

                intent1.putExtra(Intent.EXTRA_STREAM, buidFileProviderUri(uri));
                startActivity(Intent.createChooser(intent1, getString(R.string.Share)));
                return true;
            case R.id.btnHome:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private Uri buidFileProviderUri(Uri uri) {
        return FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, new File(uri.getPath()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}