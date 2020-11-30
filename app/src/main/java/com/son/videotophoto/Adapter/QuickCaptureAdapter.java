package com.son.videotophoto.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.son.videotophoto.EditPhoto;
import com.son.videotophoto.R;

import java.io.File;
import java.util.List;

public class QuickCaptureAdapter extends RecyclerView.Adapter<QuickCaptureAdapter.ViewHolder> {
    List<Bitmap> listImageList;
    ImageView imgCut;
    Context context;
    Callback callback;
    List<File> imageListCut;

    public QuickCaptureAdapter(List<Bitmap> listImageList, Context context, Callback callback, List<File> imageListCut) {
        this.listImageList = listImageList;
        this.context = context;
        this.callback = callback;
        this.imageListCut = imageListCut;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_quick_capture, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        View view = holder.getView();
        inflaterDataToView(view, position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClickItem(imageListCut.get(position).getAbsolutePath());

            }
        });
    }

    @Override
    public int getItemCount() {
        return listImageList.size();
    }

    public void inflaterDataToView(View view, int possition) {
        imgCut = view.findViewById(R.id.imgCut);
        imgCut.setImageBitmap(listImageList.get(possition));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;

        public View getView() {
            return view;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }
    }

    public interface Callback {
        void onClickItem(String path);
    }
}
