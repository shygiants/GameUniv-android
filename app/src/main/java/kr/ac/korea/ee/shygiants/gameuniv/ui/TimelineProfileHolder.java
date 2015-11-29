package kr.ac.korea.ee.shygiants.gameuniv.ui;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import kr.ac.korea.ee.shygiants.gameuniv.R;
import kr.ac.korea.ee.shygiants.gameuniv.models.User;

/**
 * Created by SHYBook_Air on 15. 11. 29..
 */
public class TimelineProfileHolder extends RecyclerView.ViewHolder {

    View container;
    ImageView userProfile;
    TextView userNameText;

    public TimelineProfileHolder(View view) {
        super(view);

        container = view.findViewById(R.id.profile_area);
        userProfile = (ImageView) view.findViewById(R.id.user_profile);
        userNameText = (TextView) view.findViewById(R.id.user_name_text);
    }

    public void populate(User user) {
        Bitmap iconBitmap = ((BitmapDrawable) userProfile.getDrawable()).getBitmap();
        Palette.Builder builder = Palette.from(iconBitmap);
        // Synchronously generate
        Palette palette = builder.generate();
        int[] colors = { palette.getDarkMutedColor(Color.WHITE), palette.getLightMutedColor(Color.WHITE) };
        GradientDrawable background = new GradientDrawable(GradientDrawable.Orientation.TL_BR, colors);
        container.setBackground(background);

        userNameText.setText(user.getUserName());
    }
}
