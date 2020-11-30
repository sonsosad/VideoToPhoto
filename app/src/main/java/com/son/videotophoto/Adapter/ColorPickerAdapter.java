package com.son.videotophoto.Adapter;

import android.app.WallpaperManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.son.videotophoto.R;

import java.util.ArrayList;
import java.util.List;

public class ColorPickerAdapter extends RecyclerView.Adapter<ColorPickerAdapter.ViewHolder> {
    Context context;
    List<Integer> listPickerColors;
    private OnColorPickerClickListener onColorPickerClickListener;


    public ColorPickerAdapter(Context context, List<Integer> listPickerColors) {
        this.context = context;
        this.listPickerColors = listPickerColors;
    }

    public ColorPickerAdapter(@NonNull Context context) {
        this(context, getDefaultColors(context));
        this.context = context;

    }

    @NonNull
    @Override
    public ColorPickerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_color_picker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorPickerAdapter.ViewHolder holder, int position) {
        holder.colorPickerView.setBackgroundColor(listPickerColors.get(position));

    }

    @Override
    public int getItemCount() {
        return listPickerColors.size();
    }

    public void setOnColorPickerClickListener(OnColorPickerClickListener onColorPickerClickListener) {
        this.onColorPickerClickListener = onColorPickerClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View colorPickerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            colorPickerView = itemView.findViewById(R.id.colorPickerView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onColorPickerClickListener != null)
                        onColorPickerClickListener.onColorPickerClickListener(listPickerColors.get(getAdapterPosition()));

                }
            });
        }
    }

    public interface OnColorPickerClickListener {
        void onColorPickerClickListener(int colorCode);
    }

    public static List<Integer> getDefaultColors(Context context) {
        ArrayList<Integer> listPickerColors = new ArrayList<>();
        listPickerColors.add(ContextCompat.getColor(context, R.color.blue_color_picker));
        listPickerColors.add(ContextCompat.getColor(context, R.color.brown_color_picker));
        listPickerColors.add(ContextCompat.getColor(context, R.color.green_color_picker));
        listPickerColors.add(ContextCompat.getColor(context, R.color.orange_color_picker));
        listPickerColors.add(ContextCompat.getColor(context, R.color.red_color_picker));
        listPickerColors.add(ContextCompat.getColor(context, R.color.black));
        listPickerColors.add(ContextCompat.getColor(context, R.color.red_orange_color_picker));
        listPickerColors.add(ContextCompat.getColor(context, R.color.sky_blue_color_picker));
        listPickerColors.add(ContextCompat.getColor(context, R.color.violet_color_picker));
        listPickerColors.add(ContextCompat.getColor(context, R.color.white));
        listPickerColors.add(ContextCompat.getColor(context, R.color.yellow_color_picker));
        listPickerColors.add(ContextCompat.getColor(context, R.color.yellow_green_color_picker));
        return listPickerColors;
    }
}
