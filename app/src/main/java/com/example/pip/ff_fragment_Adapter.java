package com.example.pip;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ff_fragment_Adapter extends FragmentStateAdapter {
    public ff_fragment_Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 1:

                return new follower_fragment();
        }
        return new following_fragment();

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
