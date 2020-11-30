package com.son.videotophoto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.son.videotophoto.Adapter.GalleryViewPagerAdapter;
import com.son.videotophoto.Adapter.ViewPagerAdapter;

public class Gallery extends AppCompatActivity {
    private ViewPager pager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        setUpView();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    private void setUpView(){
        pager = findViewById(R.id.viewPagerGallery);
        tabLayout = findViewById(R.id.tabLayoutGallery);
        FragmentManager fragmentManager = getSupportFragmentManager();
        GalleryViewPagerAdapter adapter = new GalleryViewPagerAdapter(fragmentManager,this);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));
    }
}