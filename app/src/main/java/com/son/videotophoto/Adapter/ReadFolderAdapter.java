package com.son.videotophoto.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.son.videotophoto.ListVideo;
import com.son.videotophoto.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReadFolderAdapter extends RecyclerView.Adapter<ReadFolderAdapter.ViewHolder> {
    private List<File> data;
    Context context;
    Callback callback;

    public ReadFolderAdapter(List<File> data, Context context, Callback callback) {
        this.data = data;
        this.context = context;
        this.callback = callback;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_read_folder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadFolderAdapter.ViewHolder holder, final int position) {
        View view = holder.getView();
        inflaterDataToView(view, position);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, position+"", Toast.LENGTH_SHORT).show();

                callback.onClickItem(data.get(position).getAbsolutePath());
                //context.startActivity(new Intent(context, ListVideo.class));


            }
        });

    }


    public void inflaterDataToView(View view, int position) {
        TextView textView = view.findViewById(R.id.txtVideoName);
        File file = new File(String.valueOf(data.get(position))).getAbsoluteFile();
        textView.setText(file.getName() + "(" + countFiles(file) + ")");

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    private int countFiles(File file) {
        ArrayList<File> list = new ArrayList<>();
        File[] f = file.listFiles();
        for (File files : f) {
            if (files.getName().endsWith(".mp4")) {
                list.add(files);
            }
        }
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }

        public View getView() {
            return view;
        }
    }

    public interface Callback {
        void onClickItem(String path);
    }
}
