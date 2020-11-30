package com.son.videotophoto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.son.videotophoto.Adapter.ListVideoAdapter;
import com.son.videotophoto.Adapter.ReadFolderAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListVideo extends AppCompatActivity implements ListVideoAdapter.Callback {
    ListVideoAdapter listVideoAdapter;
    RecyclerView recyclerView;
    List<File> videoFile = new ArrayList<>();
    TextView txtVideoName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_video);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.rvListVideo);


        Intent intent =getIntent();
        String pathFolder = intent.getStringExtra("PATH");
        File file = new File(pathFolder);
        file = new File(file.getPath());
        videoFile = getVideoByPath(file);

        listVideoAdapter =  new ListVideoAdapter(videoFile,this,this);
        GridLayoutManager layoutManager = new  GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listVideoAdapter);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        listVideoAdapter =  new ListVideoAdapter(videoFile,this,this);
        GridLayoutManager layoutManager = new  GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listVideoAdapter);
    }

    ArrayList<File> getVideoByPath(File file){
        ArrayList<File> temp =new ArrayList<>();
        File[]  files = file.listFiles();
        for(File f :files){
            if(f.getName().endsWith(".mp4")){
                temp.add(f);
            }
        }
        return temp;
    }

    @Override
    public void onClickItem(String path) {
        //Toast.makeText(this,path+"",Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this,VideoToPhoto.class);
        Intent intent = new Intent(this,Capture.class);
        intent.putExtra("VIDEO PATH",path);
        startActivity(intent);
    }
}