package kr.ac.korea.ee.shygiants.gameuniv.utils;

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

    public static void init(Context context) {
        if (singleton == null)
            singleton = new ImageHandler();
        singleton.picasso = Picasso.with(context);
        singleton.resources = context.getResources();
    }

    public static RequestCreator load(String path) {
        if (singleton == null) {
            // TODO: Throw exception
            return null;
        }

        return singleton.picasso.load(path);
    }

    public static RequestCreator load(File file) {
        if (singleton == null) {
            // TODO: Throw exception
            return null;
        }

        return singleton.picasso.load(file);
    }

    public static int getColor(int resourceId) {
        return singleton.resources.getColor(resourceId);
    }
}
