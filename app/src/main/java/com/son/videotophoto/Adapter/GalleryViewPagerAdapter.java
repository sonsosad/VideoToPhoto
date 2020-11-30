package com.son.videotophoto.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import com.son.videotophoto.Fragment.ImagesFragment;
import com.son.videotophoto.Fragment.QuickCapture;
import com.son.videotophoto.Fragment.TimeCapture;
import com.son.videotophoto.Fragment.VideosFragment;
import com.son.videotophoto.R;

import java.util.ArrayList;
import java.util.List;


public class GalleryViewPagerAdapter extends FragmentStatePagerAdapter {
    Context context;
    List<String>   titles;
    VideosFragment videosFragment;
    ImagesFragment imagesFragment;

    public GalleryViewPagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        titles = new ArrayList<>();
        titles.add(context.getString(R.string.Videos));
        titles.add(context.getString(R.string.Images));
        videosFragment = new VideosFragment();
        imagesFragment = new ImagesFragment();
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return  videosFragment;
            case 1:
                return imagesFragment;
        }
        return null;
    }
    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
//        String title = "";
//        switch (position){
//            case 0:
//                title = "Videos";
//                break;
//            case 1:
//                title = "Images";
//                break;
//
//        }
        return titles.get(position);
    }
}
