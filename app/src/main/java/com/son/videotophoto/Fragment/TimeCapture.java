package com.son.videotophoto.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.son.videotophoto.Adapter.QuickCaptureAdapter;
import com.son.videotophoto.EditPhoto;
import com.son.videotophoto.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.sephiroth.android.library.rangeseekbar.RangeSeekBar;

import static android.content.Context.MODE_PRIVATE;

public class TimeCapture extends Fragment implements QuickCaptureAdapter.Callback {
    public float duration = 2000;
    TextView txtSetTime, txtVideoName, txtCurrentTime, txtEndTime, txtProcess;
    View view;
    String videoUrl, time;
    VideoView vdTimeCapture;
    RangeSeekBar rangeSeekBar;
    ImageButton imgControlsVideo, imgPauseCut;
    volatile boolean stopThread = false;
    ImageButton btnTimeCut;
    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
    ArrayList<Bitmap> cutTimeList = new ArrayList<>();
    List<File> imageList = new ArrayList<>();
    QuickCaptureAdapter quickCaptureAdapter;
    RecyclerView recyclerView;
    int minTime, durationMax, currentTime, endTime;
    long is, maxTime, a, startTime, imageSize;
    String type = "";
    String quality = "";
    static String endWiths = ".jpg";
    SeekBar sbProcess;
    int visibleItemCount;

    boolean isChoose = false;
    TimeCaptureAsynTask timeCaptureAsynTask;


    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPreferences", MODE_PRIVATE);
        type = sharedPreferences.getString("TYPE", "JPG");
        quality = sharedPreferences.getString("QUALITY", getString(R.string.High));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_time_capture, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpView();
        loadVideo();
        getData();


    }

    @Override
    public void onStop() {
        super.onStop();
        stopThread = true;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (!isVisibleToUser) {
                vdTimeCapture.pause();
            }

            if (isVisibleToUser) {
                imgControlsVideo.setVisibility(View.VISIBLE);
                checkPlay(false);

            }
        }

    }

    private void setUpView() {
        txtSetTime = view.findViewById(R.id.txtSetTime);
        txtVideoName = view.findViewById(R.id.txtNameVideo);
        txtCurrentTime = view.findViewById(R.id.txtCurrentTime);
        txtEndTime = view.findViewById(R.id.txtEndTime);
        rangeSeekBar = view.findViewById(R.id.rangerSeekBarTime);
        imgControlsVideo = view.findViewById(R.id.imgControlsVideo);
        btnTimeCut = view.findViewById(R.id.btnTimeCut);
        recyclerView = view.findViewById(R.id.rvCutImageList);
        sbProcess = view.findViewById(R.id.sbProcess);
        txtProcess = view.findViewById(R.id.txtProcess);
        imgPauseCut = view.findViewById(R.id.imgPauseCut);
        btnTimeCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sbProcess.setVisibility(View.VISIBLE);
                txtProcess.setVisibility(View.VISIBLE);
                imgPauseCut.setVisibility(View.VISIBLE);
                timeCaptureAsynTask = new TimeCaptureAsynTask();
                timeCaptureAsynTask.execute();
            }
        });
        imgPauseCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPause();
            }
        });
    }

    private void loadRecyclerView(List<Bitmap> bitmaps, List<File> files) {
        quickCaptureAdapter = new QuickCaptureAdapter(bitmaps, getContext(), this, files);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(quickCaptureAdapter);
        quickCaptureAdapter.notifyDataSetChanged();

    }

    private void loadVideo() {

        Intent intent = getActivity().getIntent();
        videoUrl = intent.getStringExtra("VIDEO PATH");
        vdTimeCapture = view.findViewById(R.id.vdTimeCapture);
        Uri uri = Uri.parse(videoUrl);
        vdTimeCapture.setVideoURI(uri);
        File videoFile = new File(videoUrl);
        videoFile = new File(videoFile.getPath());
        txtVideoName.setText(videoFile.getName());
    }

    private void getData() {
        vdTimeCapture.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mmr.setDataSource(videoUrl);
                time = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                durationMax = Integer.parseInt(time);
                txtCurrentTime.setText(MilisecondsToTimer(vdTimeCapture.getCurrentPosition() / 1000));
                txtEndTime.setText(MilisecondsToTimer(vdTimeCapture.getDuration() / 1000));
                currentTime = vdTimeCapture.getCurrentPosition();
                rangeSeekBar.setMax(vdTimeCapture.getDuration() / 1000);
                rangeSeekBar.setProgress(currentTime, durationMax);
                rangeSeekBar.setEnabled(true);
                startTime = 0;
                maxTime = durationMax/1000;
                txtSetTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDialog(0, vdTimeCapture.getDuration() / 1000);
                    }
                });
                rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(RangeSeekBar rangeSeekBar, int minValue, int maxValue, boolean b) {
                        if (b) {
                            maxTime = maxValue;
                            minTime = minValue;
                            vdTimeCapture.seekTo(minValue * 1000);
                            txtEndTime.setText(MilisecondsToTimer(maxValue));
                            txtCurrentTime.setText(MilisecondsToTimer(minValue));
                            startTime = (long) ((minValue * 1000) + duration);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(RangeSeekBar rangeSeekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(RangeSeekBar rangeSeekBar) {

                    }
                });
                vdTimeCapture.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        imgControlsVideo.setVisibility(View.VISIBLE);
                        return false;
                    }
                });
                imgControlsVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!vdTimeCapture.isPlaying()) {
                            vdTimeCapture.start();
                            checkPlay(true);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    imgControlsVideo.setVisibility(View.GONE);

                                }
                            }, 3000);

                        } else {
                            vdTimeCapture.pause();
                            checkPlay(false);
                            imgControlsVideo.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (vdTimeCapture != null) {
                    if (vdTimeCapture.isPlaying()) {
                        Message msg = new Message();
                        msg.what = vdTimeCapture.getCurrentPosition();
                        handler.sendMessage(msg);
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Message msg = new Message();
                        msg.what = vdTimeCapture.getCurrentPosition();
                        handler.sendMessage(msg);
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (stopThread) {
                        vdTimeCapture.pause();
                        return;
                    }

                }
            }
        }).start();
    }

    void checkPlay(boolean isPlaying) {
        imgControlsVideo = view.findViewById(R.id.imgControlsVideo);

        if (isPlaying) {
            imgControlsVideo.setImageResource(R.drawable.ic_baseline_pause_24);

        } else {
            imgControlsVideo.setImageResource((R.drawable.ic_baseline_play_arrow_24));
        }
    }

    private void openDialog(final int min, final int max) {
        final EditText txtDuration;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_set_time_dialog, null);
        txtDuration = view.findViewById(R.id.txtDuration);
        builder.setView(view)
                .setTitle(R.string.EnterDuration)
                .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!txtDuration.getText().toString().isEmpty()) {
                            duration = (int) (Float.parseFloat(txtDuration.getText().toString()) * 1000);
                            if ((duration / 1000) > (max - min)) {
                                AlertDialog.Builder errorDialog = new AlertDialog.Builder(getContext());
                                errorDialog.setTitle(R.string.Error)
                                        .setMessage(R.string.SnapError)
                                        .setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                errorDialog.create();
                                errorDialog.show();
                            } else
                                txtSetTime.setText(getContext().getString(R.string.SnapEvery) + " " + (float) duration / 1000 + " " + getContext().getString(R.string.Sec));
                        } else {
                            android.app.AlertDialog.Builder errorDialog = new android.app.AlertDialog.Builder(getContext());
                            errorDialog.setTitle(R.string.Error)
                                    .setMessage(R.string.NullTimeSnap)
                                    .setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            errorDialog.create();
                            errorDialog.show();
                        }
                    }
                }).create();
        builder.show();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            txtCurrentTime.setText(MilisecondsToTimer(msg.what / 1000));


        }
    };

    String MilisecondsToTimer(long milisec) {
        String finalTimerString = "";
        String hoursString = "";
        String secondString;
        String minuteString;
        long seconds = (long) milisec % 60;
        long minutes = (long) milisec / 60%60;
        long hours = (long) milisec / (60 * 60);
        if (hours > 0) {
            hoursString = hours + ":";
        }
        if (seconds < 10) {
            secondString = "0" + seconds;
        } else secondString = "" + seconds;
        if (minutes < 10) {
            minuteString = "0" + minutes;
        } else minuteString = "" + minutes;
        finalTimerString = hoursString + minuteString + ":" + secondString;
        return finalTimerString;
    }

    private void createFileImage(Bitmap bitmap) throws IOException {
        File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/FrameCaptured");
        if (!file.exists()) {
            file.mkdirs();
        }
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fileName = "Image-" + n + endWiths;
        Bitmap.CompressFormat bitmapImage = Bitmap.CompressFormat.JPEG;
        if (type.equals("JPG")) {
            endWiths = ".jpg";
            bitmapImage = Bitmap.CompressFormat.JPEG;
        } else {
            endWiths = ".png";
            bitmapImage = Bitmap.CompressFormat.PNG;
        }
        File outFile = new File(file, fileName);
        FileOutputStream fos = new FileOutputStream(outFile);
        bitmap.compress(bitmapImage, setQuality(quality), fos);
        fos.flush();
        fos.close();
        imageList.add(outFile);

    }

    int setQuality(String quality) {
        if (quality.equals(getResources().getString(R.string.Best))) {
            return 100;
        } else if (quality.equals(getResources().getString(R.string.VeryHigh))) {
            return 85;
        } else if (quality.equals(getResources().getString(R.string.High))) {
            return 75;
        } else if (quality.equals(getResources().getString(R.string.Medium))) {
            return 65;
        } else if (quality.equals(getResources().getString(R.string.Low))) {
            return 50;
        } else return 75;
    }

    @Override
    public void onClickItem(String path) {
        Intent intent = new Intent(getContext(), EditPhoto.class);
        intent.putExtra("PATH IMG", path);
        startActivity(intent);
    }

    void checkPause() {
        if (isChoose) {
            imgPauseCut.setImageResource(R.drawable.pause);
            timeCaptureAsynTask = new TimeCaptureAsynTask();
            startTime = a;
            timeCaptureAsynTask.execute();
            isChoose = false;
        } else {
            imgPauseCut.setImageResource(R.drawable.play);
            timeCaptureAsynTask.cancel(true);
            isChoose = true;
        }
    }

    private class TimeCaptureAsynTask extends AsyncTask<Void, ArrayList<Bitmap>, ArrayList<Bitmap>> {
        @SuppressLint("WrongThread")
        @Override
        protected ArrayList<Bitmap> doInBackground(Void... voids) {
            for (is = startTime;
                 is <= maxTime * 1000;
                 is += duration) {
                Log.e("Tag", is + "iii");
                mmr.setDataSource(videoUrl);
                Bitmap bmFrame = mmr.getFrameAtTime(is * 1000, MediaMetadataRetriever.OPTION_CLOSEST);
                try {
                    createFileImage(bmFrame);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                publishProgress();
                cutTimeList.add(bmFrame);
                visibleItemCount = cutTimeList.size();
                if (isCancelled()) {
                    a = (long) (is + duration);
                    break;
                }


            }

            Log.e("Tag", a + "ibbb");
            return cutTimeList;
        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> bitmaps) {
            loadRecyclerView(cutTimeList, imageList);
            super.onPostExecute(bitmaps);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imageSize = (long) ((maxTime - minTime) / (duration / 1000));
            Log.e("Tag", maxTime + "mt");
            Log.e("Tag", minTime + "mint");
            sbProcess.setMax(Integer.parseInt(String.valueOf(imageSize)));
            txtProcess.setText(visibleItemCount + "/" + imageSize);
        }

        @Override
        protected void onProgressUpdate(ArrayList<Bitmap>... values) {
            super.onProgressUpdate(values);
            loadRecyclerView(cutTimeList, imageList);
            txtProcess.setText(visibleItemCount + "/" + imageSize);
            sbProcess.setProgress(visibleItemCount);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();


        }

    }

}