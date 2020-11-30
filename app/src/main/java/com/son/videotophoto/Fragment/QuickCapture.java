package com.son.videotophoto.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EdgeEffect;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import static android.content.Context.MODE_PRIVATE;

public class QuickCapture extends Fragment implements QuickCaptureAdapter.Callback {
    private static final String TAG = "Video To Photo";
    VideoView videoView;
    SeekBar seekBar;
    TextView txtVideoName, txtCurrentTime, txtEndTime;
    ImageButton btnCut, imgControlsVideo;
    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
    String videoUrl;
    public List<Bitmap> cutImageList = new ArrayList<>();
    public List<File> imageList = new ArrayList<>();
    volatile boolean stopthread = false;
    RecyclerView recyclerView;
    QuickCaptureAdapter quickCaptureAdapter;
    String type = "";
    String quality = "";
    static String endWiths = ".jpg";
    View view;
    String getPath;
    long a;
    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPreferences", MODE_PRIVATE);
        type = sharedPreferences.getString("TYPE", "JPG");
        quality = sharedPreferences.getString("QUALITY", getString(R.string.High));
    }


    @Override
    public void onResume() {
        super.onResume();

        loadVideo();
        try {
            getData(videoUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadRecyclerView(cutImageList, imageList);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_quick_capture, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpView();
        if (!imageList.isEmpty()) {
            for (int i = 0; i < imageList.size(); i++) {
                if (imageList.get(i).getAbsolutePath().equals(getPath)) {
                    imageList.remove(i);
                    if (!cutImageList.isEmpty()) {
                    }
                    cutImageList.remove(i);
                    break;
                }
            }
        }
        btnCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cutImage();
            }
        });
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            if (!isVisibleToUser) {
                videoView.pause();
            }

            if (isVisibleToUser) {
                imgControlsVideo.setVisibility(View.VISIBLE);
                checkPlay(false);

            }
        }

    }

    private void loadVideo() {
        Intent intent = getActivity().getIntent();
        videoUrl = intent.getStringExtra("VIDEO PATH");
        videoView = view.findViewById(R.id.vdTimeCapture);
        Uri uri = Uri.parse(videoUrl);
        videoView.setVideoURI(uri);
//        videoView.requestFocus();
//        if (this.mediaController == null) {
//            this.mediaController = new MediaController(VideoToPhoto.this);
//
//            // Set the videoView that acts as the anchor for the MediaController.
//            this.mediaController.setAnchorView(videoView);
//
//            // Set MediaController for VideoView
//            this.videoView.setMediaController(mediaController);
//        }

    }

    private void cutImage() {
        checkPlay(true);
        videoView.pause();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imgControlsVideo.setVisibility(View.GONE);
            }
        }, 3000);
        float currentTime = videoView.getCurrentPosition() * 1000;
        Log.e("Tag",currentTime+"ctt");
        mmr = new MediaMetadataRetriever();
        mmr.setDataSource(videoUrl);
        Bitmap brFrame = mmr.getFrameAtTime(a=(long) currentTime, MediaMetadataRetriever.OPTION_CLOSEST);
        Log.e("Tag",a+"a");
        cutImageList.add(brFrame);
        try {
            createFileImage(brFrame);
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadRecyclerView(cutImageList, imageList);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                videoView.start();
            }
        }, 1000);
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


    void checkPlay(boolean isPlaying) {
        imgControlsVideo = view.findViewById(R.id.imgControlsVideo);

        if (isPlaying) {
            imgControlsVideo.setImageResource(R.drawable.ic_baseline_pause_24);

        } else {
            imgControlsVideo.setImageResource((R.drawable.ic_baseline_play_arrow_24));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopthread = true;
        videoView.pause();
        videoView.suspend();
    }

    private void setUpView() {
        txtVideoName = view.findViewById(R.id.txtNameVideo);
        txtCurrentTime = view.findViewById(R.id.txtCurrentTime);
        txtEndTime = view.findViewById(R.id.txtEndTime);
        seekBar = view.findViewById(R.id.seekBar);
        videoView = view.findViewById(R.id.vdTimeCapture);
        imgControlsVideo = view.findViewById(R.id.imgControlsVideo);
        recyclerView = view.findViewById(R.id.rvCutImageList);
        btnCut = view.findViewById(R.id.btnCut);
    }


    private void getData(final String videoPath) throws IOException {
        File videoFile = new File(videoPath);
        videoFile = new File(videoFile.getPath());
        Uri uri = Uri.parse(videoPath);
        mmr.setDataSource(videoPath);
        videoView.setVideoURI(uri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar.setMax(videoView.getDuration());
                String duration = MilisecondsToTimer(videoView.getDuration() / 1000);
                txtEndTime.setText(duration);
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.pause();
                mp.release();
            }
        });
        txtVideoName.setText(videoFile.getName());
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                imgControlsVideo.setVisibility(View.VISIBLE);
                return false;
            }

        });

        imgControlsVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!videoView.isPlaying()) {
                    videoView.start();
                    checkPlay(true);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imgControlsVideo.setVisibility(View.GONE);

                        }
                    }, 3000);


                } else {
                    videoView.pause();
                    checkPlay(false);
                    imgControlsVideo.setVisibility(View.VISIBLE);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoView.pause();
                    videoView.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (videoView != null) {
                    try {
                        if (videoView.isPlaying()) {
                            Message msg = new Message();
                            msg.arg1 = videoView.getCurrentPosition();
                            handler.sendMessage(msg);
                            Thread.sleep(1);
//                            Log.e("Tag",msg.arg1+" crT");
//                            Log.e("Tag",videoView.getDuration()+" eT");
                            if (msg.arg1 / 1000 == (videoView.getDuration() / 1000)) {
                                videoView.stopPlayback();
                                checkPlay(false);
                                videoView.resume();
                            }
                        } else {
                            Message msg = new Message();
                            msg.arg1 = videoView.getCurrentPosition();
                            handler.sendMessage(msg);
                            Thread.sleep(1);
                        }
                        if (stopthread) {
                            return;
                        }
                    } catch (InterruptedException ie) {
                        Log.w(TAG, "" + ie);
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
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
        Bitmap.CompressFormat bitmapImage;
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
        //MediaScannerConnection.scanFile(context, new String[]{file.toString()}, new String[]{file.getName()}, null);
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
    public void onPause() {
        super.onPause();
        stopthread=true;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            txtCurrentTime.setText(MilisecondsToTimer(msg.arg1 / 1000));
            seekBar.setProgress(msg.arg1);
        }
    };

    String MilisecondsToTimer(long milisec) {
        String finalTimerString = "";
        String secondString;
        String minuteString;
        long seconds = (long) milisec % 60;
        long minutes = (long) milisec / 60%60;
        long hours = (long) milisec / (60 * 60);
        if (hours > 0) {
            finalTimerString = hours + ":";
        }
        if (seconds < 10) {
            secondString = "0" + seconds;
        } else secondString = "" + seconds;
        if (minutes < 10) {
            minuteString = "0" + minutes;
        } else minuteString = "" + minutes;
        finalTimerString = finalTimerString + minuteString + ":" + secondString;
        return finalTimerString;
    }

    @Override
    public void onClickItem(String path) {
        getPath = path;
        Intent intent = new Intent(getContext(), EditPhoto.class);
        intent.putExtra("PATH IMG", path);
        startActivity(intent);
    }
}