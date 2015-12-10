package kr.ac.korea.ee.shygiants.gameuniv.models;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.okhttp.*;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.ArrayList;

import kr.ac.korea.ee.shygiants.gameuniv.R;
import kr.ac.korea.ee.shygiants.gameuniv.utils.ImageHandler;
import kr.ac.korea.ee.shygiants.gameuniv.utils.OnCreateGradientListener;
import kr.ac.korea.ee.shygiants.gameuniv.utils.RESTAPI;
import retrofit.Retrofit;

/**
 * Created by SHYBook_Air on 15. 10. 5..
 */
public class User extends Response implements Target, TimelineOwner {

    private String _id;
    private String userName;
    private String email;
    private ArrayList<Game> havePlayed;

    private String authToken;
    private OnCreateGradientListener listener;

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

//    public ArrayList getPlayedGames() {
//        return havePlayed;
//    }

    public void setAuthToken(final String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setProfilePhoto(final File profileFile, final ImageView profile) {
        com.squareup.okhttp.RequestBody requestBody =
                com.squareup.okhttp.RequestBody.create(MediaType.parse("multipart/form-data"), profileFile);

        RESTAPI.Users.uploadProfilePhoto(email, requestBody, authToken)
                .enqueue(new retrofit.Callback<String>() {
                    @Override
                    public void onResponse(retrofit.Response<String> response, Retrofit retrofit) {
                        // HERE IS MAIN THREAD!
                        switch (response.code()) {
                            // TODO: Error Handling
                            case 401: // Unauthorized
                                break;
                            case 500: // Server Error
                                break;
                            case 404: // Not Found
                                break;
                            case 200:
                                // TODO: Notify to all users
                                ImageHandler.load(profileFile).into(profile);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        // TODO: Error Handling
                        t.printStackTrace();
                    }
                });
    }

    public boolean equals(User user) {
        return email.equals(user.getEmail());
    }

    public void getProfilePhoto(ImageView imageView) {
        getRequestCreator().into(imageView);
    }

    public void getProfilePhoto(ImageView imageView, Callback callback) {
        getRequestCreator().into(imageView, callback);
    }

    public void getProfilePhotoGradient(OnCreateGradientListener listener) {
        this.listener = listener;
        getRequestCreator().into(this);
    }

    private RequestCreator getRequestCreator() {
        return ImageHandler.load(RESTAPI.getURL() + "/users/" + email + "/profilePhotos");
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
        int primaryColor = ImageHandler.getColor(R.color.colorPrimary);
        int[] colors = { palette.getDarkMutedColor(primaryColor), palette.getLightMutedColor(primaryColor) };
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR, colors);
        listener.onCreateGradient(gradientDrawable);
    }

    @Override
    public String getKey() {
        return email;
    }
}
