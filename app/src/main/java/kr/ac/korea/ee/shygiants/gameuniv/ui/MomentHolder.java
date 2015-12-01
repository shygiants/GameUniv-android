package kr.ac.korea.ee.shygiants.gameuniv.ui;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import kr.ac.korea.ee.shygiants.gameuniv.R;
import kr.ac.korea.ee.shygiants.gameuniv.models.Moment;
import kr.ac.korea.ee.shygiants.gameuniv.models.User;
import kr.ac.korea.ee.shygiants.gameuniv.utils.ContentsStore;

/**
 * Created by SHYBook_Air on 15. 11. 19..
 */
public class MomentHolder extends RecyclerView.ViewHolder {

    CardView cardView;

    TextView authorText;
    TextView timestampText;
    TextView contentText;

    ImageView gameIcon;

    User user;

    public MomentHolder(View view) {
        super(view);

        cardView = (CardView) view;

        authorText = (TextView) view.findViewById(R.id.author);
        timestampText = (TextView) view.findViewById(R.id.created_at);
        contentText = (TextView) view.findViewById(R.id.content);

        gameIcon = (ImageView) view.findViewById(R.id.game_icon);
    }

    public void populate(int position) {
        Moment moment = ContentsStore.getFeedElementAt(position);

//        Bitmap iconBitmap = ((BitmapDrawable) gameIcon.getDrawable()).getBitmap();
//        Palette.Builder builder = Palette.from(iconBitmap);
//        // Synchronously generate
//        Palette palette = builder.generate();
//        int[] colors = {palette.getLightVibrantColor(Color.WHITE), Color.WHITE };
//        GradientDrawable background = new GradientDrawable(GradientDrawable.Orientation.TR_BL, colors);
//        container.setBackground(background);

        populate(moment);
    }

    public void populate(User user, int position) {
        Moment moment = ContentsStore.getTimelineElementAt(user, position);

        populate(moment);
    }

    private void populate(Moment moment) {
        authorText.setText(moment.getAuthor().getUserName());
        timestampText.setText(moment.getTimeStamp());
        contentText.setText(moment.getContent());
    }
}
