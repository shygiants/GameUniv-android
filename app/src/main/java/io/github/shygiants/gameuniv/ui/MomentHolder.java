package io.github.shygiants.gameuniv.ui;

import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.models.Achievement;
import io.github.shygiants.gameuniv.models.Game;
import io.github.shygiants.gameuniv.models.Moment;
import io.github.shygiants.gameuniv.models.User;

/**
 * Created by SHYBook_Air on 15. 11. 19..
 */
public class MomentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public interface OnMomentClickListener {
        void onAuthorClick(User user);
        void onGameClick(Game game);
    }

    CardView cardView;

    // Common
    @Bind(R.id.author)
    TextView authorText;
    @Bind(R.id.created_at)
    TextView timestampText;
    @Bind(R.id.profile_photo)
    CircleImageView profilePhoto;
    @Bind(R.id.game_icon)
    ImageView gameIcon;

    // For image, score and text
    @Nullable @Bind(R.id.content)
    TextView contentText;

    // For image
    @Nullable @Bind(R.id.image)
    ImageView image;

    // For achievement
    @Nullable @Bind(R.id.title)
    TextView titleText;
    @Nullable @Bind(R.id.description)
    TextView descText;
    @Nullable @Bind(R.id.point)
    TextView pointText;



    OnMomentClickListener listener;
    Moment moment;

    public MomentHolder(View view, OnMomentClickListener listener) {
        super(view);
        this.listener = listener;
        ButterKnife.bind(this, view);

        cardView = (CardView) view;

        profilePhoto.setOnClickListener(this);
        authorText.setOnClickListener(this);
        gameIcon.setOnClickListener(this);
    }

    @SuppressWarnings("ConstantConditions")
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
