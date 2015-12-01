package kr.ac.korea.ee.shygiants.gameuniv.ui;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import kr.ac.korea.ee.shygiants.gameuniv.R;
import kr.ac.korea.ee.shygiants.gameuniv.models.Moment;
import kr.ac.korea.ee.shygiants.gameuniv.models.User;
import kr.ac.korea.ee.shygiants.gameuniv.utils.ContentsStore;

/**
 * Created by SHYBook_Air on 15. 11. 19..
 */
public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String NEWSFEED = "NEWSFEED";
    private static final String TIMELINE = "TIMELINE";

    private String context;
    private User user;

    public FeedAdapter(User user) {
        context = TIMELINE;
        this.user = user;
    }

    public FeedAdapter() {
        context = NEWSFEED;
    }

    private boolean isTimeline() {
        return context.equals(TIMELINE);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Populate view
        MomentHolder momentHolder = (MomentHolder) holder;
        if (isTimeline()) momentHolder.populate(user, position);
        else momentHolder.populate(position);
    }

    @Override
    public int getItemCount() {
        return (isTimeline())?
                ContentsStore.getTimelineElementsCount(user) : ContentsStore.getFeedElementsCount();
    }

    @Override
    public int getItemViewType(int position) {
        return (isTimeline())?
                ContentsStore.getTimelineElementAt(user, position).getViewType() :
                ContentsStore.getFeedElementAt(position).getViewType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Determine layout
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View momentView = inflater.inflate(R.layout.card_moment_container, parent, false);

        LinearLayout container = (LinearLayout) momentView.findViewById(R.id.container);

        int resourceId;
        switch (viewType) {
            case Moment.TEXT:
                resourceId = R.layout.card_moment_text;
                break;
            case Moment.SCORE:
                resourceId = R.layout.card_moment_score;
                break;
            default:
                resourceId = R.layout.card_moment_text;
                break;
        }
        container.addView(inflater.inflate(resourceId, null, false), 1);

        return new MomentHolder(momentView);
    }
}
