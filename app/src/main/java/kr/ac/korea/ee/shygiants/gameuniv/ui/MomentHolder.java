package kr.ac.korea.ee.shygiants.gameuniv.ui;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
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

    CircleImageView profilePhoto;
    ImageView gameIcon;

    public MomentHolder(View view) {
        super(view);

        cardView = (CardView) view;

        profilePhoto = (CircleImageView) view.findViewById(R.id.profile_photo);
        authorText = (TextView) view.findViewById(R.id.author);
        timestampText = (TextView) view.findViewById(R.id.created_at);
        contentText = (TextView) view.findViewById(R.id.content);

        gameIcon = (ImageView) view.findViewById(R.id.game_icon);
    }

    public void populate(int position) {
        Moment moment = ContentsStore.getFeedElementAt(position);
        moment.getAuthor().getProfilePhoto(profilePhoto);
        populate(moment);
    }

    public void populate(User user, int position) {
        Moment moment = ContentsStore.getTimelineElementAt(user, position);
        user.getProfilePhoto(profilePhoto);
        populate(moment);
    }

    private void populate(Moment moment) {
        authorText.setText(moment.getAuthor().getUserName());
        timestampText.setText(moment.getTimeStamp());
        contentText.setText(moment.getContent());
    }
}
