package io.github.shygiants.gameuniv.utils;

import android.content.Intent;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import io.github.shygiants.gameuniv.activities.PhotoPickerActivity;

/**
 * Created by SHYBook_Air on 2016. 1. 8..
 */
public class PhotoPickerResultResolver {

    public static List<Photo> resolve(Intent data) {
        Parcelable[] photoPicked = data.getParcelableArrayExtra(PhotoPickerActivity.PICKED_PHOTOS);

        List<Photo> photosToReturn = new ArrayList<>();
        for (Parcelable photo : photoPicked)
            photosToReturn.add((Photo)photo);

        return photosToReturn;
    }
}
