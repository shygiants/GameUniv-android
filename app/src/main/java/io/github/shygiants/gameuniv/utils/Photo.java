package io.github.shygiants.gameuniv.utils;

import android.net.Uri;

/**
 * Created by SHYBook_Air on 2016. 1. 4..
 */
public class Photo {

    private Uri thumbnailUri;
    private Uri imageUri;

    public Photo(Uri thumbnail, Uri image) {
        thumbnailUri = thumbnail;
        imageUri = image;
    }

    public Uri getThumbnailUri() {
        return thumbnailUri;
    }

    public Uri getImageUri() {
        return imageUri;
    }
}
