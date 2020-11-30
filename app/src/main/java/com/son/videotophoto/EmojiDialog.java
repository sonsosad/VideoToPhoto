package com.son.videotophoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.son.videotophoto.Adapter.EmojiAdapter;

import java.util.ArrayList;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class EmojiDialog extends Dialog implements EmojiAdapter.Callback {
    RecyclerView rvEmoji;
    ArrayList<String> emojiL = new ArrayList<>();
    EmojiAdapter emojiAdapter;
    PhotoEditor photoEditor;


    public EmojiDialog(@NonNull Context context, PhotoEditor photoEditor) {
        super(context);
        this.photoEditor = photoEditor;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emoji_dialog);
        rvEmoji = findViewById(R.id.rvEmoji);
        emojiL = PhotoEditor.getEmojis(getContext());
        emojiAdapter = new EmojiAdapter(emojiL, getContext(), this);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 5);
        rvEmoji.setAdapter(emojiAdapter);
        rvEmoji.setLayoutManager(layoutManager);

    }

    @Override
    public void onClickItem(String emoji) {
        Toast.makeText(getContext(), emoji + "", Toast.LENGTH_LONG).show();
        photoEditor.addEmoji(emoji);
        dismiss();
    }
}