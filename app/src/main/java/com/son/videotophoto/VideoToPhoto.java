//package com.son.videotophoto;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.media.MediaMetadataRetriever;
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.View;
//import android.widget.CompoundButton;
//import android.widget.ImageButton;
//import android.widget.MediaController;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//
//import androidx.appcompat.widget.Toolbar;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.widget.SeekBar;
//import android.widget.TextView;
//import android.widget.VideoView;
//
//import com.son.videotophoto.Adapter.QuickCaptureAdapter;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//public class VideoToPhoto extends AppCompatActivity {
//    private static final String TAG = "Video To Photo";
//    VideoView videoView;
//    SeekBar seekBar;
//    MediaController mediaController;
//    RadioButton rdCut, rdCutS;
//    RadioGroup radioGroup;
//    TextView txtTime, txtVideoName, txtCurrentTime, txtEndTime;
//    ImageButton btnCut, imgControlsVideo;
//    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
//    String videoUrl;
//    List<Bitmap> cutImageList = new ArrayList<>();
//    volatile boolean stopthread = false;
//    RecyclerView recyclerView;
//    QuickCaptureAdapter quickCaptureAdapter;
//    String type = "";
//    String quality = "";
//    Context context;
//    static String endWiths = ".jpg";
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
//        type = sharedPreferences.getString("TYPE", "JPG");
//        quality = sharedPreferences.getString("QUALITY",getString(R.string.High));
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_video_to_photo);
//
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//
//        loadVideo();
//        cutMode();
//        setUpView();
//        try {
//            getData(videoUrl);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        cutImage();
//
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_menu, menu);
//        return true;
//    }
//
//    private void loadVideo() {
//        Intent intent = getIntent();
//        videoUrl = intent.getStringExtra("VIDEO PATH");
//        videoView = findViewById(R.id.vdTimeCapture);
//        Uri uri = Uri.parse(videoUrl);
//        videoView.setVideoURI(uri);
////        videoView.requestFocus();
////        if (this.mediaController == null) {
////            this.mediaController = new MediaController(VideoToPhoto.this);
////
////            // Set the videoView that acts as the anchor for the MediaController.
////            this.mediaController.setAnchorView(videoView);
////
////            // Set MediaController for VideoView
////            this.videoView.setMediaController(mediaController);
////        }
//
//    }
//
//    private void cutImage() {
//        btnCut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                videoView.pause();
//                long currentTime = videoView.getCurrentPosition() * 1000;
//                mmr = new MediaMetadataRetriever();
//                mmr.setDataSource(videoUrl);
//                Bitmap brFrame = mmr.getFrameAtTime(currentTime, MediaMetadataRetriever.OPTION_CLOSEST);
//                cutImageList.add(brFrame);
//                try {
//                    createFileImage(brFrame);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                loadRecyclerView(cutImageList);
//            }
//        });
//    }
//
//    private void loadRecyclerView(List<Bitmap> bitmaps) {
//        quickCaptureAdapter = new QuickCaptureAdapter(bitmaps, this);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setAdapter(quickCaptureAdapter);
//        quickCaptureAdapter.notifyDataSetChanged();
//
//    }
//
//    private void cutMode() {
//        rdCut = findViewById(R.id.rdCut);
//        rdCutS = findViewById(R.id.rdCutS);
//        radioGroup = findViewById(R.id.radioGroup);
//
//        rdCut.setOnCheckedChangeListener(listenerRadio);
//        rdCutS.setOnCheckedChangeListener(listenerRadio);
//
//    }
//
//    CompoundButton.OnCheckedChangeListener listenerRadio = new CompoundButton.OnCheckedChangeListener() {
//        @Override
//        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//            txtTime = findViewById(R.id.txtSnapTime);
//            if (isChecked) {
//                switch (buttonView.getId()) {
//                    case R.id.rdCut:
//                        txtTime.setVisibility(View.GONE);
//                        break;
//                    case R.id.rdCutS:
//                        txtTime.setVisibility(View.VISIBLE);
//                        break;
//                }
//            }
//        }
//    };
//
//    void checkPlay(boolean isPlaying) {
//        imgControlsVideo = findViewById(R.id.imgControlsVideo);
//
//        if (isPlaying) {
//            imgControlsVideo.setImageResource(R.drawable.ic_baseline_pause_24);
//
//        } else {
//            imgControlsVideo.setImageResource((R.drawable.ic_baseline_play_arrow_24));
//        }
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        stopthread = true;
//        videoView.pause();
//        videoView.suspend();
//    }
//
//    private void setUpView() {
//        txtVideoName = findViewById(R.id.txtNameVideo);
//        txtCurrentTime = findViewById(R.id.txtCurrentTime);
//        txtEndTime = findViewById(R.id.txtEndTime);
//        seekBar = findViewById(R.id.seekBar);
//        videoView = findViewById(R.id.vdTimeCapture);
//        imgControlsVideo = findViewById(R.id.imgControlsVideo);
//        recyclerView = findViewById(R.id.rvCutImageList);
//        btnCut = findViewById(R.id.btnCut);
//    }
//
//
//    private void getData(final String videoPath) throws IOException {
//        File videoFile = new File(videoPath);
//        videoFile = new File(videoFile.getPath());
//        Uri uri = Uri.parse(videoPath);
//        mmr.setDataSource(videoPath);
//        videoView.setVideoURI(uri);
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                seekBar.setMax(videoView.getDuration());
//                String duration = MilisecondsToTimer(videoView.getDuration() / 1000);
//                txtEndTime.setText(duration);
//            }
//        });
//        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                mp.pause();
//                mp.release();
//            }
//        });
//        txtVideoName.setText(videoFile.getName());
//        videoView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imgControlsVideo.setVisibility(View.VISIBLE);
//
//            }
//        });
//
//        imgControlsVideo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!videoView.isPlaying()) {
//                    videoView.start();
//                    checkPlay(true);
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            imgControlsVideo.setVisibility(View.GONE);
//
//                        }
//                    }, 3000);
//
//
//                } else {
//                    videoView.pause();
//                    checkPlay(false);
//                    imgControlsVideo.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if (fromUser) {
//                    videoView.pause();
//                    videoView.seekTo(progress);
//                    seekBar.setProgress(progress);
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (videoView != null) {
//                    try {
//                        if (videoView.isPlaying()) {
//                            Message msg = new Message();
//                            msg.arg1 = videoView.getCurrentPosition();
//                            handler.sendMessage(msg);
//                            Thread.sleep(1);
//                        } else {
//                            Message msg = new Message();
//                            msg.arg1 = videoView.getCurrentPosition();
//                            handler.sendMessage(msg);
//                            Thread.sleep(1);
//                        }
//                        if (stopthread) {
//                            return;
//                        }
//                    } catch (InterruptedException ie) {
//                        Log.w(TAG, "" + ie);
//                    }
//                    try {
//                        Thread.sleep(1);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
//    }
//
//    private void createFileImage(Bitmap bitmap) throws IOException {
//        File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/FrameCaptured");
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        Random generator = new Random();
//        int n = 10000;
//        n = generator.nextInt(n);
//        String fileName = "Image-" + n + endWiths;
//        Bitmap.CompressFormat bitmapImage = Bitmap.CompressFormat.JPEG;
//        if (type.equals("JPG")) {
//            endWiths = ".jpg";
//            bitmapImage = Bitmap.CompressFormat.JPEG;
//        } else {
//            endWiths = ".png";
//            bitmapImage = Bitmap.CompressFormat.PNG;
//        }
//        File outFile = new File(file,fileName);
//        FileOutputStream fos = new FileOutputStream(outFile);
//        bitmap.compress(bitmapImage,setQuality(quality),fos);
//        fos.flush();
//        fos.close();
//
//    }
//
//    int setQuality(String quality) {
//        if (quality.equals(getResources().getString(R.string.Best))) {
//            return 100;
//        } else if (quality.equals(getResources().getString(R.string.VeryHigh))) {
//            return 85;
//        } else if (quality.equals(getResources().getString(R.string.High))) {
//            return 75;
//        } else if (quality.equals(getResources().getString(R.string.Medium))) {
//            return 65;
//        } else if (quality.equals(getResources().getString(R.string.Low))) {
//            return 50;
//        } else return 75;
//    }
//
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            txtCurrentTime.setText(MilisecondsToTimer(msg.arg1 / 1000));
//            seekBar.setProgress(msg.arg1);
//        }
//    };
//
//    String MilisecondsToTimer(long milisec) {
//        String finalTimerString = "";
//        String secondString;
//        String minuteString;
//        int seconds = (int) milisec % 60;
//        int minutes = (int) milisec / 60;
//        int hours = (int) milisec / (60 * 60);
//        if (hours > 0) {
//            finalTimerString = hours + ":";
//        }
//        if (seconds < 10) {
//            secondString = "0" + seconds;
//        } else secondString = "" + seconds;
//        if (minutes < 10) {
//            minuteString = "0" + minutes;
//        } else minuteString = "" + minutes;
//        finalTimerString = finalTimerString + minuteString + ":" + secondString;
//        return finalTimerString;
//    }
//}