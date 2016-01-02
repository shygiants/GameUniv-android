package io.github.shygiants.gameuniv.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.models.Moment;
import io.github.shygiants.gameuniv.models.TimelineOwner;
import io.github.shygiants.gameuniv.utils.ContentsStore;
import io.github.shygiants.gameuniv.utils.Feed;

/**
 * Created by SHYBook_Air on 15. 11. 19..
 */
public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeRefreshLayout.OnRefreshListener {

    private static final String NEWSFEED = "NEWSFEED";
    private static final String TIMELINE = "TIMELINE";

    private String context;
    private Feed feed;
    private MomentHolder.OnMomentClickListener listener;
    private SwipeRefreshLayout swipeRefreshLayout;

    public FeedAdapter(TimelineOwner owner, MomentHolder.OnMomentClickListener listener) {
        context = TIMELINE;
        feed = ContentsStore.getInstance().getTimeline(owner);
        feed.pushAdapter(this);
        this.listener = listener;
    }

    public FeedAdapter(MomentHolder.OnMomentClickListener listener) {
        context = NEWSFEED;
        feed = ContentsStore.getInstance().getFeed();
        feed.pushAdapter(this);
        this.listener = listener;
    }

    private boolean isTimeline() {
        return context.equals(TIMELINE);
    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout swipe) {
        swipeRefreshLayout = swipe;
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        feed.refresh(swipeRefreshLayout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Populate view
        MomentHolder momentHolder = (MomentHolder) holder;
        momentHolder.populate(feed.getFeedElementAt(position));
    }

    @Override
    public int getItemCount() {
        return feed.getFeedElementsCount();
    }

    @Override
    public int getItemViewType(int position) {
        return feed.getFeedElementAt(position).getViewType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Determine layout
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View momentView = inflater.inflate(R.layout.card_moment_container, parent, false);

        LinearLayout container = (LinearLayout) momentView.findViewById(R.id.container);

        int resourceId;
        switch (viewType) {
            // TODO: Handle achievement moments
            case Moment.TEXT:
                resourceId = R.layout.card_moment_text;
                break;
            case Moment.SCORE:
                resourceId = R.layout.card_moment_score;
                break;
            case Moment.ACHIEVEMENT:
                resourceId = R.layout.card_moment_achievement;
                break;
            case Moment.IMAGE:
                resourceId = R.layout.card_moment_image;
                break;
            default:
                resourceId = R.layout.card_moment_text;
                break;
        }
        container.addView(inflater.inflate(resourceId, null, false), 1);

        return new MomentHolder(momentView, viewType, listener);
    }

    public void destroy() {
        feed.removeAdapter(this);
    }
}
