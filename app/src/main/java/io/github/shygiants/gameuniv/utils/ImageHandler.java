package io.github.shygiants.gameuniv.utils;

import android.content.Context;
import android.content.res.Resources;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;

/**
 * Created by SHYBook_Air on 15. 12. 3..
 */
public class ImageHandler {
    private static ImageHandler singleton;

    private Picasso picasso;
    private Resources resources;

    private ImageHandler(Context context) {
        picasso = Picasso.with(context);
        resources = context.getResources();
    }

    public static void init(Context context) {
        if (singleton == null)
            singleton = new ImageHandler(context);
    }

    public static ImageHandler getInstance() {
        // TODO: Throw exception
        if (singleton == null) return null;
        return singleton;
    }

    public RequestCreator load(String path) {
        return picasso.load(path);
    }

    public RequestCreator load(File file) {
        return picasso.load(file);
    }

    public int getColor(int resourceId) {
        return resources.getColor(resourceId);
    }
}
