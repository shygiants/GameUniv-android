package io.github.shygiants.gameuniv.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import io.github.shygiants.gameuniv.fragments.PostContentsBaseFragment;
import io.github.shygiants.gameuniv.fragments.PostContentsPageFragment;
import io.github.shygiants.gameuniv.fragments.PostContentsTitleFragment;
import io.github.shygiants.simplephotopicker.models.Photo;

/**
 * Created by SHYBook_Air on 2016. 1. 8..
 */
public class PostContentsPageAdapter extends FragmentPagerAdapter {

    private List<Photo> photosPicked;
    private List<PostContentsBaseFragment> fragments;

    public PostContentsPageAdapter(FragmentManager fm) {
        super(fm);
        photosPicked = new ArrayList<>();
        fragments = new ArrayList<>();
    }

    public int addPhotos(List<Photo> photosPicked) {
        int moveTo = fragments.size();
        this.photosPicked.addAll(photosPicked);
        notifyDataSetChanged();
        return moveTo;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        if (position < fragments.size())
            return fragments.get(position);
        PostContentsBaseFragment fragment = (position == 0)?
                PostContentsTitleFragment.newInstance() :
                PostContentsPageFragment.newInstance(photosPicked.get(position - 1));

        fragments.add(position, fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return (photosPicked != null)? photosPicked.size() + 1 : 1;
    }

    public List<Photo> getPhotos() {
        return photosPicked;
    }
}
