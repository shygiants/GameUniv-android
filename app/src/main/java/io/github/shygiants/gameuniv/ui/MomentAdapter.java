package io.github.shygiants.gameuniv.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.ButterKnife;
import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.models.Moment;
import io.github.shygiants.gameuniv.models.TimelineOwner;
import io.github.shygiants.gameuniv.utils.ContentsStore;
import io.github.shygiants.gameuniv.utils.Feed;

/**
 * Created by SHYBook_Air on 15. 11. 19..
 */
public class MomentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeRefreshLayout.OnRefreshListener {

    private Feed feed;
    private MomentHolder.OnMomentClickListener listener;
    private SwipeRefreshLayout swipeRefreshLayout;

    public MomentAdapter(MomentHolder.OnMomentClickListener listener) {
        this(ContentsStore.getInstance().getFeed(), listener);
    }

    public MomentAdapter(TimelineOwner owner, MomentHolder.OnMomentClickListener listener) {
        this(ContentsStore.getInstance().getTimeline(owner), listener);
    }

    private MomentAdapter(Feed feed, MomentHolder.OnMomentClickListener listener) {
        this.feed = feed;
        feed.pushAdapter(this);
        this.listener = listener;
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
        int resourceId;
        switch (viewType) {
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
        // Determine layout
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View momentView = inflater.inflate(R.layout.card_moment_container, parent, false);
        LinearLayout container = ButterKnife.findById(momentView, R.id.container);

        container.addView(inflater.inflate(resourceId, null, false), 1);

        return new MomentHolder(momentView, listener);
    }

    public void destroy() {
        feed.removeAdapter(this);
    }
}
