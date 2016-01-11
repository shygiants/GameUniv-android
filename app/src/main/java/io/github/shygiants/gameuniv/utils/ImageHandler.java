package io.github.shygiants.gameuniv.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;

/**
 * Created by SHYBook_Air on 15. 12. 3..
 */
public class ImageHandler {
    private static final float BITMAP_SCALE = 0.4f;
    private static final float BLUR_RADIUS = 10f;

    private static ImageHandler singleton;

    private Picasso picasso;
    private Resources resources;
    private RenderScript renderScript;
    private ScriptIntrinsicBlur theIntrinsic;
    private ContentResolver contentResolver;

    private ImageHandler(Context context) {
        picasso = Picasso.with(context);
        resources = context.getResources();
        renderScript = RenderScript.create(context);
        theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        contentResolver = context.getContentResolver();
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

    public Bitmap blur(Bitmap image) {
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        Allocation tmpIn = Allocation.createFromBitmap(renderScript, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

    public Bitmap blur(Uri image) {
        try {
            return blur(MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse("file://" + image)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
