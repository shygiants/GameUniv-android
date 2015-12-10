package kr.ac.korea.ee.shygiants.gameuniv.models;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.graphics.Palette;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import kr.ac.korea.ee.shygiants.gameuniv.utils.ImageHandler;
import kr.ac.korea.ee.shygiants.gameuniv.utils.OnCreateGradientListener;
import kr.ac.korea.ee.shygiants.gameuniv.utils.RESTAPI;

/**
 * Created by SHYBook_Air on 15. 11. 20..
 */
public class Game extends Response implements Target, TimelineOwner {

    private String _id;
    private String gameName;
    private String contactEmail;
    private ArrayList<Achievement> achievements;

    private OnCreateGradientListener listener;

    public String getGameName() {
        return gameName;
    }

    public void getGameIcon(ImageView imageView) {
        getRequestCreator().into(imageView);
    }

    public void getGameIcon(ImageView imageView, Callback callback) {
        getRequestCreator().into(imageView, callback);
    }

    public void getGameIconGradient(OnCreateGradientListener listener) {
        this.listener = listener;
        getRequestCreator().into(this);
    }

    private RequestCreator getRequestCreator() {
        return ImageHandler.load(RESTAPI.getURL() + "/games/" + _id + "/gameIcons");
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        // DO NOTHING
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
        // TODO: Error Handling
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        Palette.Builder builder = Palette.from(bitmap);
        // Synchronously generate
        Palette palette = builder.generate();
        int[] colors = { palette.getDarkMutedColor(Color.WHITE), palette.getLightMutedColor(Color.WHITE) };
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR, colors);
        listener.onCreateGradient(gradientDrawable);
    }

    @Override
    public String getKey() {
        return _id;
    }
}
