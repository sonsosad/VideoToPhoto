package com.son.videotophoto.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.son.videotophoto.R;

import java.util.ArrayList;


import ja.burhanrashid52.photoeditor.PhotoEditor;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.ViewHolder> {
    ArrayList<String> emojiList;
    Context context;
    Callback callback;

    public EmojiAdapter(ArrayList<String> emojiList, Context context, Callback callback) {
        this.emojiList = emojiList;
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emoji, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtEmoji.setText(emojiList.get(position));


    }

    @Override
    public int getItemCount() {
        return emojiList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtEmoji;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtEmoji = itemView.findViewById(R.id.txtEmoji);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onClickItem(emojiList.get(getLayoutPosition()));
                    }

                }


            });
        }
    }

    public interface Callback {
        void onClickItem(String emoji);
    }
}
