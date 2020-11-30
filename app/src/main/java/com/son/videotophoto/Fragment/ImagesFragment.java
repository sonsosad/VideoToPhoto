package com.son.videotophoto.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.son.videotophoto.Adapter.GalleryImageAdapter;
import com.son.videotophoto.EditPhoto;
import com.son.videotophoto.Model.Video;
import com.son.videotophoto.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ImagesFragment extends Fragment implements GalleryImageAdapter.Callback {
    View view;
    List<File> imageList = new ArrayList<>();
    public ImageButton btnDel, btnReload, btnShare;
    public LinearLayout SelectedZone;
    public TextView txtNumSelected;
    static int index;
    RecyclerView recyclerView;
    GalleryImageAdapter galleryImageAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_images_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rvImages);


    }

    @Override
    public void onResume() {
        super.onResume();
        readImageFromExternalStorage();
        galleryImageAdapter = new GalleryImageAdapter(getContext(), imageList, 1, false, false, this, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(galleryImageAdapter);
        setUpView();
    }

    public void setUpView() {
        btnDel = view.findViewById(R.id.btnDel);
        btnReload = view.findViewById(R.id.btnReload);
        btnShare = view.findViewById(R.id.btnShare);
        SelectedZone = view.findViewById(R.id.SelectedZone);
        txtNumSelected = view.findViewById(R.id.txtNumSelected);
    }

    private void readImageFromExternalStorage() {
        imageList.clear();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/FrameCaptured");
        if (!file.exists()) {
            file.mkdirs();
        }
        File[] files = file.listFiles();
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return Long.compare(o2.lastModified(), o1.lastModified());
            }
        });
        for (File f : files) {
            if (f.getName().endsWith(".jpg") || f.getName().endsWith(".png")) {
                imageList.add(f);
            }
        }
    }

    @Override
    public void onClickItem(String path) {
        Intent intent = new Intent(getContext(), EditPhoto.class);
        intent.putExtra("PATH IMG", path);
        startActivity(intent);
    }
}