package io.github.shygiants.gameuniv.ui;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.models.Achievement;
import io.github.shygiants.gameuniv.models.Game;
import io.github.shygiants.gameuniv.models.Moment;
import io.github.shygiants.gameuniv.models.TimelineOwner;
import io.github.shygiants.gameuniv.models.User;
import io.github.shygiants.gameuniv.utils.ContentsStore;

/**
 * Created by SHYBook_Air on 15. 11. 19..
 */
public class MomentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public interface OnMomentClickListener {
        void onAuthorClick(User user);
        void onGameClick(Game game);
    }

    CardView cardView;

    TextView authorText;
    TextView timestampText;
    TextView contentText;

    ImageView image;

    TextView titleText;
    TextView descText;
    TextView pointText;

    CircleImageView profilePhoto;
    ImageView gameIcon;

    OnMomentClickListener listener;
    Moment moment;

    public MomentHolder(View view, int viewType, OnMomentClickListener listener) {
        super(view);
        this.listener = listener;

        cardView = (CardView) view;

        profilePhoto = (CircleImageView) view.findViewById(R.id.profile_photo);
        profilePhoto.setOnClickListener(this);
        authorText = (TextView) view.findViewById(R.id.author);
        authorText.setOnClickListener(this);
        timestampText = (TextView) view.findViewById(R.id.created_at);

        switch (viewType) {
            case Moment.ACHIEVEMENT:
                titleText = (TextView) view.findViewById(R.id.title);
                descText = (TextView) view.findViewById(R.id.description);
                pointText = (TextView) view.findViewById(R.id.point);
                break;
            case Moment.IMAGE:
                image = (ImageView) view.findViewById(R.id.image);
            default:
                contentText = (TextView) view.findViewById(R.id.content);
                break;
        }

        gameIcon = (ImageView) view.findViewById(R.id.game_icon);
        gameIcon.setOnClickListener(this);
    }

    public void populate(Moment moment) {
        this.moment = moment;
        moment.getAuthor().getProfilePhoto(profilePhoto);
        moment.getGame().getGameIcon(gameIcon);
        authorText.setText(moment.getAuthor().getUserName());
        timestampText.setText(moment.getTimeStamp());

        switch (moment.getViewType()) {
            case Moment.ACHIEVEMENT:
                Achievement achievement = moment.getAchievement();
                titleText.setText(achievement.getTitle());
                descText.setText(achievement.getDescription());
                pointText.setText(achievement.getPoint());
                break;
            case Moment.IMAGE:
                moment.getImage(image);
            default:
                String content = moment.getContent();
                if (content != null)
                    contentText.setText(moment.getContent());
                else
                    contentText.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.author:
            case R.id.profile_photo:
                listener.onAuthorClick(moment.getAuthor());
                break;
            case R.id.game_icon:
                listener.onGameClick(moment.getGame());
                break;
            default:
                break;
        }
    }
}
