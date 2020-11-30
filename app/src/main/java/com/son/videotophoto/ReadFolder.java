package com.son.videotophoto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.son.videotophoto.Adapter.ReadFolderAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReadFolder extends AppCompatActivity implements ReadFolderAdapter.Callback {
    RecyclerView recyclerView;
    ReadFolderAdapter readFolderAdapter;

    List<File> dir = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        assert getSupportActionBar() != null;

        setContentView(R.layout.activity_read_folder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.rvReadFolder);
        ArrayList<File> myVideo = ListDir(Environment.getExternalStorageDirectory());
        for (int i = 0; i < dir.size() - 1; i++) {
            for (int j = 1; j < dir.size(); j++) {
                if (dir.get(i).getName().equals(dir.get(j).getName())) {
                    dir.remove(j);
                }
            }
        }
        readFolderAdapter = new ReadFolderAdapter(dir, this, this);
        //Log.e("Tag",dir.size()+"");

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(readFolderAdapter);
    }

    public ArrayList<File> ListDir(File f) {

        ArrayList<File> temp = new ArrayList<>();
        File[] files = f.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                temp.addAll(ListDir(file));
            }
            if (file.getName().endsWith(".mp4")) {
                dir.add(new File(file.getParent()));
                temp.add(file);
            }
        }
        return temp;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClickItem(String path) {
        //Toast.makeText(this,path+"", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ListVideo.class);
        intent.putExtra("PATH", path);
        startActivity(intent);
    }
}