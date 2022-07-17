package com.example.pip;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;

import com.example.pip.Adapters.StateAdapter;
import com.google.android.material.tabs.TabLayout;

public class following_follower_AnotherProfile extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 pager2;
    private StateAdapter adapter;
    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_follower_another_profile);

        ActionBar bar;
        bar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#ffffff"));
        bar.setBackgroundDrawable(colorDrawable);
        bar.setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow =  ContextCompat.getDrawable(this, R.drawable.arrow_back);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP);
        bar.setHomeAsUpIndicator(upArrow);
        bar.setElevation(5);



        Bundle bundle = getIntent().getExtras();
        userName = bundle.getString("usName");

        tabLayout = findViewById(R.id.tabLayout2);
        pager2 = findViewById(R.id.pager2);

        FragmentManager fm = getSupportFragmentManager();
        adapter = new StateAdapter(fm , getLifecycle());
        pager2.setAdapter(adapter);



        bar.setTitle(Html.fromHtml("<font color='#000000'>" + userName + "</font>"));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

    }
}