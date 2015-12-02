package kr.ac.korea.ee.shygiants.gameuniv.utils;

import android.content.Context;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;

/**
 * Created by SHYBook_Air on 15. 12. 3..
 */
public class ImageHandler {
    private static ImageHandler singleton;

    private Picasso picasso;

    public static void init(Context context) {
        if (singleton == null)
            singleton = new ImageHandler();
        singleton.picasso = Picasso.with(context);
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
}
