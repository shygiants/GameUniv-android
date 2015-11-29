package kr.ac.korea.ee.shygiants.gameuniv.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import kr.ac.korea.ee.shygiants.gameuniv.R;
import kr.ac.korea.ee.shygiants.gameuniv.models.Moment;
import kr.ac.korea.ee.shygiants.gameuniv.utils.ContentsStore;

/**
 * Created by SHYBook_Air on 15. 11. 19..
 */
public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class Context {
        private static final String NEWSFEED = "NEWSFEED";
        private static final String TIMELINE = "TIMELINE";

        private String context;

        public static Context NEWSFEED() {
            Context newsfeed = new Context();
            newsfeed.context = NEWSFEED;
            return newsfeed;
        }

        public static Context TIMELINE() {
            Context timeline = new Context();
            timeline.context = TIMELINE;
            return timeline;
        }

        public boolean isTimeline() {
            return context.equals(TIMELINE);
        }
    }

    private static final int HEADER = -1;

    private Context context;

    public FeedAdapter(FeedAdapter.Context context) {
        this.context = context;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Populate view
        if (context.isTimeline()) {
            if (position == 0) {
                TimelineProfileHolder timelineProfileHolder = (TimelineProfileHolder) holder;
                timelineProfileHolder.populate(ContentsStore.getUser());
                return;
            }
            position--;
        }

        MomentHolder momentHolder = (MomentHolder) holder;
        momentHolder.populate(position);
    }

    @Override
    public int getItemCount() {
        int itemCount = ContentsStore.getMomentsCount();
        if (context.isTimeline()) ++itemCount;
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (context.isTimeline()) {
            if (position == 0) return HEADER;
            --position;
        }

        return ContentsStore.getMomentAt(position).getViewType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Determine layout
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == HEADER) {
            View timelineProfileView = inflater.inflate(R.layout.area_timeline_profile, parent, false);
            return new TimelineProfileHolder(timelineProfileView);
        }

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
