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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Populate view
        MomentHolder momentHolder = (MomentHolder) holder;
        momentHolder.populate(position);
    }

    @Override
    public int getItemCount() {
        return ContentsStore.getMomentsCount();
    }

    @Override
    public int getItemViewType(int position) {
        return ContentsStore.getMomentAt(position).getViewType();
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
