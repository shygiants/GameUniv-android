package io.github.shygiants.gameuniv.activities;

import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.utils.Photo;
import io.github.shygiants.gameuniv.utils.PhotoLoader;

public class PhotoPickerActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Photo>> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<List<Photo>> onCreateLoader(int id, Bundle args) {
        return new PhotoLoader(this);
    }

    @Override
    public void onLoaderReset(Loader<List<Photo>> loader) {

    }

    @Override
    public void onLoadFinished(Loader<List<Photo>> loader, List<Photo> data) {
        
    }
}
