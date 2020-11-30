package com.son.videotophoto.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.son.videotophoto.Fragment.ImagesFragment;
import com.son.videotophoto.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryImageAdapter extends RecyclerView.Adapter<GalleryImageAdapter.ViewHolder> {
    Context context;
    List<File> list;
    List<File> selectedList = new ArrayList<>();
    int id = 0;
    boolean selectMode = false;
    boolean isChoose = false;
    boolean delMode = false;
    ImagesFragment fragment;
    Callback callback;

    public GalleryImageAdapter(Context context, List<File> list, int id, boolean selectMode, boolean delMode, ImagesFragment fragment, Callback callback) {
        this.context = context;
        this.list = list;
        this.id = id;
        this.selectMode = selectMode;
        this.delMode = delMode;
        this.fragment = fragment;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.images_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        View view = holder.getView();
        inflatDataToView(view, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public View inflatDataToView(View view, int possition) {
        ImageButton btnDel = view.findViewById(R.id.btnDel);
        ImageView imageView = view.findViewById(R.id.imgImage);
        String path = list.get(possition).getAbsolutePath();
        File file = new File(path);
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id != 0 && !selectMode) {
                    callback.onClickItem(list.get(possition) + "");
                    Toast.makeText(context, "sondz", Toast.LENGTH_LONG).show();
                } else {
                    if (!isChoose) {
                        isChoose = true;
                        selectedList.add(list.get(possition));
                    } else {
                        isChoose = false;
                        if (!selectedList.isEmpty()) {
                            selectedList.remove(list.get(possition));
                        }
                    }
                    fragment.txtNumSelected.setText("Selected " + selectedList.size() + " images");
                }
                fragment.btnDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setTitle(R.string.Delete).setMessage(R.string.DeleteQuestion).setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (File file : selectedList) {
                                    if (file.exists()) {
                                        file.delete();
                                    }
                                    list.remove(file);
                                }
                                for (int i = selectedList.size() - 1; i >= 0; i--) {
                                    selectedList.remove(i);
                                }
                                fragment.txtNumSelected.setText("Selected " + selectedList.size() + " images");
                                notifyDataSetChanged();
                                dialog.dismiss();
                                FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
                                if (Build.VERSION.SDK_INT >= 26) {
                                    ft.setReorderingAllowed(false);
                                }
                                ft.detach(fragment).attach(fragment).commit();
                            }
                        }).create();
                        dialog.show();
                    }
                });
                fragment.btnReload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isChoose = false;
                        if (!selectedList.isEmpty()) {
                            for (int i = selectedList.size() - 1; i >= 0; i--) {
                                selectedList.remove(i);
                            }
                        }
                        fragment.txtNumSelected.setText("Selected " + selectedList.size() + " images");
                        notifyDataSetChanged();
                    }
                });
                if (isChoose) {
                    btnDel.setImageResource(R.drawable.ic_baseline_check_24);
                } else
                    btnDel.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24);
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectMode = true;
                notifyDataSetChanged();
                if (fragment != null) {
                    fragment.SelectedZone.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });
        btnDel.setVisibility(View.INVISIBLE);
        if (selectMode) {
            btnDel.setVisibility(View.VISIBLE);
            btnDel.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24);
        }
        if (delMode) {
            btnDel.setVisibility(View.VISIBLE);
            btnDel.setImageResource(R.drawable.ic_baseline_cancel_24);
            btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(possition);
                    notifyDataSetChanged();
                }
            });

        }
        return view;
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
