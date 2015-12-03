package kr.ac.korea.ee.shygiants.gameuniv.ui;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.ac.korea.ee.shygiants.gameuniv.R;
import kr.ac.korea.ee.shygiants.gameuniv.models.Game;
import kr.ac.korea.ee.shygiants.gameuniv.models.Moment;
import kr.ac.korea.ee.shygiants.gameuniv.models.User;
import kr.ac.korea.ee.shygiants.gameuniv.utils.ContentsStore;

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

    CircleImageView profilePhoto;
    ImageView gameIcon;

    OnMomentClickListener listener;
    Moment moment;

    public MomentHolder(View view, OnMomentClickListener listener) {
        super(view);
        this.listener = listener;

        cardView = (CardView) view;

        profilePhoto = (CircleImageView) view.findViewById(R.id.profile_photo);
        authorText = (TextView) view.findViewById(R.id.author);
        authorText.setOnClickListener(this);
        timestampText = (TextView) view.findViewById(R.id.created_at);
        contentText = (TextView) view.findViewById(R.id.content);

        gameIcon = (ImageView) view.findViewById(R.id.game_icon);
        gameIcon.setOnClickListener(this);
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
        this.moment = moment;
        authorText.setText(moment.getAuthor().getUserName());
        timestampText.setText(moment.getTimeStamp());
        contentText.setText(moment.getContent());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.author:
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
