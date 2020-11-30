package com.son.videotophoto.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.son.videotophoto.Fragment.QuickCapture;
import com.son.videotophoto.Fragment.TimeCapture;
import com.son.videotophoto.R;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    Context context;
    List<String> titles;
    QuickCapture quickCapture;
    TimeCapture timeCapture;

    public ViewPagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        titles = new ArrayList<>();
        titles.add(context.getString(R.string.QuickCapture));
        titles.add(context.getString(R.string.TimeCapture));
        quickCapture = new QuickCapture();
        timeCapture = new TimeCapture();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return quickCapture;
            case 1:
                return timeCapture;
        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return titles.get(position);
    }
}
