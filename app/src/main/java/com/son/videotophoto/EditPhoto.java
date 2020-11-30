package com.son.videotophoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.son.videotophoto.Adapter.QuickCaptureAdapter;
import com.son.videotophoto.Fragment.QuickCapture;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.DecimalFormat;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.TextStyleBuilder;
import ja.burhanrashid52.photoeditor.ViewType;
import yuku.ambilwarna.AmbilWarnaDialog;

public class EditPhoto extends AppCompatActivity implements OnPhotoEditorListener {
    CardView btnShare, btnEdit, btnDelete, btnCrop, btnText, btnSticker, btnPaitn;
    ImageButton imgSave, infoImage;
    ImageView imgColors, imgEaser,imgUndo,imgRedo;
    TextView txtCrop;
    CropImageView cropImage;
    PhotoEditorView photoEditorView;
    PhotoEditor photoEditor;
    static String path = "";
    LinearLayout linearLayoutShow, lnPaint,undoAndRedo;
    FrameLayout frameLayoutTool;
    int defaultColor;
    SeekBar sbSize, sbOpacity, sbEaser;
    boolean isChoose = false;
    public static final String FILE_PROVIDER_AUTHORITY = "com.son.videotophoto.fileprovider";

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpView();
        showPhoto();
        textEditor();
        sticker();
        pickerColor();
        paint();
        saveFile();
        undoAndRedo();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditPhoto.this);
        builder.setTitle(R.string.Exit).setMessage(R.string.ExitQuestion).setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditPhoto.super.onBackPressed();
                dialog.dismiss();
                finish();
            }
        });
        builder.show();

    }

    private void setUpView() {
        btnShare = findViewById(R.id.btnShare);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnCrop = findViewById(R.id.btnCrop);
        btnText = findViewById(R.id.btnText);
        btnSticker = findViewById(R.id.btnSticker);
        btnPaitn = findViewById(R.id.btnPaint);
        txtCrop = findViewById(R.id.txtCrop);
        photoEditorView = findViewById(R.id.photoEditor);
        cropImage = findViewById(R.id.cropImage);
        imgSave = findViewById(R.id.imgSave);
        infoImage = findViewById(R.id.infoImage);
        linearLayoutShow = findViewById(R.id.bottomNavigation);
        frameLayoutTool = findViewById(R.id.frameLayoutTool);
        lnPaint = findViewById(R.id.lnPaint);
        imgColors = findViewById(R.id.imgColors);
        sbSize = findViewById(R.id.sbSize);
        sbOpacity = findViewById(R.id.sbOpacity);
        imgEaser = findViewById(R.id.imgEraser);
        sbEaser = findViewById(R.id.sbEaser);
        undoAndRedo = findViewById(R.id.undoAndRedo);
        imgUndo = findViewById(R.id.imgUndo);
        imgRedo =findViewById(R.id.imgRedo);
        defaultColor = ContextCompat.getColor(this, R.color.colorPrimary);


    }
    public void  undoAndRedo(){
        imgUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEditor.undo();
            }
        });
        imgRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEditor.redo();
            }
        });
    }

    private void saveFile() {
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(EditPhoto.this, "abc", Toast.LENGTH_LONG).show();
                }
                photoEditor.saveAsFile(path, new PhotoEditor.OnSaveListener() {
                    @Override
                    public void onSuccess(@NonNull String imagePath) {
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        photoEditorView.getSource().setImageBitmap(bitmap);
                        Toast.makeText(EditPhoto.this, "Save Success", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(EditPhoto.this,SaveSucces.class);
                        intent.putExtra("PATH SUCCES",path);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(EditPhoto.this, "fail", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    private void showPhoto() {
        Intent intent = getIntent();
        path = intent.getStringExtra("PATH IMG");
        Uri uri = Uri.parse(path);
        File file = new File(path);
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        photoEditorView.getSource().setImageBitmap(bitmap);
        photoEditor = new PhotoEditor.Builder(this, photoEditorView)
                .setPinchTextScalable(true)
                .build();
        photoEditor.setOnPhotoEditorListener(this);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutShow.setVisibility(View.GONE);
                frameLayoutTool.setVisibility(View.VISIBLE);
                undoAndRedo.setVisibility(View.VISIBLE);
            }
        });
        btnCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImage.setVisibility(View.VISIBLE);
                txtCrop.setVisibility(View.VISIBLE);
                photoEditorView.setVisibility(View.GONE);

                cropImage(cropImage, photoEditorView, photoEditor);
            }
        });
        infoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDetails();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditPhoto.this);
                builder.setTitle(R.string.Delete).setMessage(R.string.DeleteQuestion).setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File file1 = new File(path);
                        file1.delete();
                        dialog.dismiss();
//                        finish();
                        EditPhoto.super.onBackPressed();
                    }
                });
                builder.show();
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_SEND);
                intent1.setType("image/*");

                intent1.putExtra(Intent.EXTRA_STREAM, buidFileProviderUri(uri));
                startActivity(Intent.createChooser(intent1, getString(R.string.Share)));
            }
        });

    }

    private Uri buidFileProviderUri(Uri uri) {
        return FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, new File(uri.getPath()));
    }

    private void textEditor() {
        btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImage.setVisibility(View.GONE);
                photoEditorView.setVisibility(View.VISIBLE);
                TextEditorDialog textEditorDialog = TextEditorDialog.show(EditPhoto.this);
                textEditorDialog.setOnTextEditorListener(new TextEditorDialog.TextEditor() {
                    @Override
                    public void onDone(String inputText, int colorCode) {
                        TextStyleBuilder styleBuilder = new TextStyleBuilder();
                        styleBuilder.withTextColor(colorCode);
                        photoEditor.addText(inputText, styleBuilder);
                    }
                });
            }
        });
    }

    private void sticker() {
        btnSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImage.setVisibility(View.GONE);
                photoEditorView.setVisibility(View.VISIBLE);
                EmojiDialog emojiDialog = new EmojiDialog(EditPhoto.this, photoEditor);
                emojiDialog.show();
                emojiDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
            }
        });
    }

    private void paint() {
        btnPaitn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImage.setVisibility(View.GONE);
                photoEditorView.setVisibility(View.VISIBLE);
                frameLayoutTool.setVisibility(View.GONE);
                lnPaint.setVisibility(View.VISIBLE);
                undoAndRedo.setVisibility(View.GONE);
                imgEaser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkBrushOption();
                    }
                });

                sbOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        photoEditor.setOpacity(progress);
                        seekBar.setProgress(progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

            }
        });
        photoEditor.setOnPhotoEditorListener(new OnPhotoEditorListener() {
            @Override
            public void onEditTextChangeListener(View rootView, String text, int colorCode) {

            }

            @Override
            public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {

            }

            @Override
            public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {

            }

            @Override
            public void onStartViewChangeListener(ViewType viewType) {
                lnPaint.setVisibility(View.GONE);
                frameLayoutTool.setVisibility(View.VISIBLE);
                undoAndRedo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopViewChangeListener(ViewType viewType) {

            }
        });
        sbSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    seekBar.setProgress(progress);
                    photoEditor.setBrushSize(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbEaser.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    seekBar.setProgress(progress);
                    photoEditor.setBrushDrawingMode(false);
                    photoEditor.brushEraser();
                    photoEditor.setBrushEraserSize(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    void checkBrushOption() {
        if (isChoose) {
            imgEaser.setImageResource(R.drawable.eraser);
            sbEaser.setVisibility(View.GONE);
            sbSize.setVisibility(View.VISIBLE);
            photoEditor.setBrushDrawingMode(true);
            isChoose = false;
        } else {
            imgEaser.setImageResource(R.drawable.paint);
            sbEaser.setVisibility(View.VISIBLE);
            sbSize.setVisibility(View.GONE);
            photoEditor.setBrushDrawingMode(false);
            photoEditor.brushEraser();
            isChoose = true;
        }
    }

    private void pickerColor() {

        imgColors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmbilWarnaDialog colorPickers = new AmbilWarnaDialog(EditPhoto.this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {

                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        defaultColor = color;
                        photoEditor.setBrushColor(defaultColor);
                    }
                });
                colorPickers.show();
            }
        });
    }

    void cropImage(final CropImageView cropImage, final PhotoEditorView view, final PhotoEditor editor) {
        File file = new File(path);
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        cropImage.setImageBitmap(bitmap);
        Rect rect = new Rect(3, 1, 1, 3);
        cropImage.setCropRect(rect);
        txtCrop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(EditPhoto.this, "Cuted", Toast.LENGTH_LONG).show();
                Bitmap croped = cropImage.getCroppedImage();
                cropImage.setImageBitmap(croped);
                view.getSource().setImageBitmap(croped);
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(EditPhoto.this, "abc", Toast.LENGTH_LONG).show();
                }
                editor.saveAsFile(path, new PhotoEditor.OnSaveListener() {
                    @Override
                    public void onSuccess(@NonNull String imagePath) {
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        photoEditorView.getSource().setImageBitmap(bitmap);
                        Toast.makeText(EditPhoto.this, "Success", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(EditPhoto.this, "fail", Toast.LENGTH_LONG).show();
                    }
                });
                return false;
            }

        });

    }

    private void showDialogDetails() {
        try {
            File file = new File(path);
            double size = file.length() / (1024 * 1024);
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            String endwith = "";
            String filesize = "";
            BasicFileAttributes attr = null;
            FileTime creationTime = null;
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            if (size > 1024) {
                decimalFormat = new DecimalFormat("#.00");
                filesize = decimalFormat.format(size);
                endwith = " MB";
            } else {
                filesize = String.valueOf(size);
                endwith = " Kb";
            }
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                attr = Files.readAttributes(Paths.get(path), BasicFileAttributes.class);
                creationTime = attr.creationTime();
                dialog.setTitle(R.string.Details).setMessage("" +
                        this.getResources().getString(R.string.FileName) + ":\n" + file.getName() +
                        "\n" + this.getResources().getString(R.string.FileSize) + ":\n" + filesize + endwith +
                        "\n" + R.string.Resolution + ":\n" + bitmap.getHeight() + " x " + bitmap.getWidth() +
                        "\nDate:\n" + attr.creationTime() +
                        "\n" + this.getResources().getString(+R.string.Path) + ":\n" + path)
                        .setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
            } else {
                dialog.setTitle(R.string.Details).setMessage("" +
                        this.getResources().getString(R.string.FileName) + ":\n" + file.getName() +
                        "\n" + this.getResources().getString(R.string.FileSize) + ":\n" + filesize + endwith +
                        "\n" + R.string.Resolution + ":\n" + bitmap.getWidth() + " x " + bitmap.getHeight() +
                        "\n" + this.getResources().getString(R.string.Path) + ":\n" + path)
                        .setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
            }
            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEditTextChangeListener(View rootView, String text, int colorCode) {
        TextEditorDialog textEditorDialog = TextEditorDialog.show(this, text, colorCode);
        textEditorDialog.setOnTextEditorListener(new TextEditorDialog.TextEditor() {
            @Override
            public void onDone(String inputText, int colorCode) {
                TextStyleBuilder styleBuilder = new TextStyleBuilder();
                styleBuilder.withTextColor(colorCode);
                photoEditor.editText(rootView, inputText, styleBuilder);
            }
        });
    }

    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {

    }

    @Override
    public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {

    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {

    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {

    }
}