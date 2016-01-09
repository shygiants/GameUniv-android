package io.github.shygiants.gameuniv.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import io.github.shygiants.gameuniv.fragments.PostContentsPageFragment;
import io.github.shygiants.gameuniv.fragments.PostContentsTitleFragment;
import io.github.shygiants.gameuniv.utils.Photo;

/**
 * Created by SHYBook_Air on 2016. 1. 8..
 */
public class PostContentsPageAdapter extends FragmentPagerAdapter {

    private List<Photo> photosPicked;

    public PostContentsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setPhotos(List<Photo> photosPicked) {
        this.photosPicked = photosPicked;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        if (position == 0) return PostContentsTitleFragment.newInstance();
        return PostContentsPageFragment.newInstance(photosPicked.get(position - 1));
    }

    @Override
    public int getCount() {
        return (photosPicked != null)? photosPicked.size() + 1 : 1;
    }

    public List<Photo> getPhotos() {
        return photosPicked;
    }
}
