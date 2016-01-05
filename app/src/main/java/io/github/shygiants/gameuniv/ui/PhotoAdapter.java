package io.github.shygiants.gameuniv.ui;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.activities.PhotoPickerActivity;
import io.github.shygiants.gameuniv.utils.Photo;
import io.github.shygiants.gameuniv.utils.PhotoLoader;

/**
 * Created by SHYBook_Air on 2016. 1. 5..
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder>  implements LoaderManager.LoaderCallbacks<List<Photo>> {

    PhotoPickerActivity activity;
    List<Photo> photos;

    public PhotoAdapter(PhotoPickerActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onBindViewHolder(PhotoHolder holder, int position) {
        holder.setPhoto(photos.get(position));
    }

    @Override
    public int getItemCount() {
        return (photos != null)? photos.size() : 0;
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_photo, parent, false);

        return new PhotoHolder(view, activity);
    }

    @Override
    public Loader<List<Photo>> onCreateLoader(int id, Bundle args) {
        return new PhotoLoader(activity);
    }

    @Override
    public void onLoaderReset(Loader<List<Photo>> loader) {
        photos.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onLoadFinished(Loader<List<Photo>> loader, List<Photo> photos) {
        this.photos = new ArrayList<>(photos);
        notifyDataSetChanged();
    }
}
