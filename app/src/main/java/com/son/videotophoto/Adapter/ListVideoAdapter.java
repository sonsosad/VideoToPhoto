package com.son.videotophoto.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.son.videotophoto.Model.Video;
import com.son.videotophoto.R;

import java.io.File;
import java.util.List;

public class ListVideoAdapter extends RecyclerView.Adapter<ListVideoAdapter.ViewHolder> {
    private List<File> data;
    Context context;
    TextView txtVideoName;
    Callback callback;

    public ListVideoAdapter(List<File> data, Context context, Callback callback) {
        this.data = data;
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        View view = holder.getView();
        inflaterDataToView(view, position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClickItem(data.get(position).getAbsolutePath());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void inflaterDataToView(View view, int positon) {
        txtVideoName = view.findViewById(R.id.txtvideoNameListVideo);
        VideoView thunbVideo = view.findViewById(R.id.vdThumbnail);
        //File file = new File(String.valueOf(data.get(positon).getAbsoluteFile()));
        txtVideoName.setText(data.get(positon).getName());

        Uri uri = Uri.parse(data.get(positon).getAbsolutePath());
        thunbVideo.setVideoURI(uri);
        thunbVideo.seekTo(10000);

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
