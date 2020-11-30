package com.son.videotophoto;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.son.videotophoto.Adapter.ColorPickerAdapter;

public class TextEditorDialog extends DialogFragment {
    public static final String TAG = TextEditorDialog.class.getSimpleName();
    View view;
    ;
    public static final String EXTRA_INPUT_TEXT = "extra_input_text";
    public static final String EXTRA_COLOR_CODE = "extra_color_code";
    private EditText addTextEditText;
    private ImageButton addTextDoneTextView;
    private InputMethodManager inputMethodManager;
    private int colorCode;
    RecyclerView addTextColorPickerRecyclerView;
    private TextEditor textEditor;
    String a;

    public interface TextEditor {
        void onDone(String inputText, int colorCode);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.transparent);
    }

    public static TextEditorDialog show(@NonNull AppCompatActivity appCompatActivity,
                                        @NonNull String inputText,
                                        @ColorInt int colorCode) {
        Bundle args = new Bundle();
        args.putString(EXTRA_INPUT_TEXT, inputText);
        args.putInt(EXTRA_COLOR_CODE, colorCode);
        TextEditorDialog fragment = new TextEditorDialog();
        fragment.setArguments(args);
        fragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return fragment;
    }

    //Show dialog with default text input as empty and text color white
    public static TextEditorDialog show(@NonNull AppCompatActivity appCompatActivity) {
        return show(appCompatActivity,
                "", ContextCompat.getColor(appCompatActivity, R.color.white));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_text_editor_dialog, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpView();
        addEvents();
    }

    public void setUpView() {
        addTextEditText = view.findViewById(R.id.addTextEditText);
        addTextDoneTextView = view.findViewById(R.id.btnOk);
        addTextColorPickerRecyclerView = view.findViewById(R.id.rvColors);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void addEvents() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        addTextColorPickerRecyclerView.setLayoutManager(layoutManager);
        addTextColorPickerRecyclerView.setHasFixedSize(true);
        ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter(getContext());
        colorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
            @Override
            public void onColorPickerClickListener(int colorCode1) {
                colorCode = colorCode1;
                addTextEditText.setTextColor(colorCode);

            }
        });
        addTextColorPickerRecyclerView.setAdapter(colorPickerAdapter);
        addTextEditText.setText(getArguments().getString(EXTRA_INPUT_TEXT));
        colorCode = getArguments().getInt(EXTRA_COLOR_CODE);
        addTextEditText.setTextColor(colorCode);
        inputMethodManager.toggleSoftInput(inputMethodManager.SHOW_FORCED, 0);

        addTextDoneTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(getContext(), "abc", Toast.LENGTH_LONG).show();
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                dismiss();
                String inputText = addTextEditText.getText().toString();
                if (!TextUtils.isEmpty(inputText) && textEditor != null) {
                    textEditor.onDone(inputText, colorCode);
                }
                return false;
            }
        });

    }

    public void setOnTextEditorListener(TextEditor textEditor1) {
        textEditor = textEditor1;
    }

}