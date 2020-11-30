package com.son.videotophoto.Model;

import java.io.Serializable;

public class Video implements Serializable {
    private String title;
    private String duration;
    private String video;
    private String videoPath;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public Video(String title, String duration, String video, String videoPath) {
        this.title = title;
        this.duration = duration;
        this.video = video;
        this.videoPath = videoPath;
    }

    public Video() {
    }
}
