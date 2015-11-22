package kr.ac.korea.ee.shygiants.gameuniv.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.ac.korea.ee.shygiants.gameuniv.R;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Determine layout
        View momentView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.card_moment, parent, false);

        return new MomentHolder(momentView);
    }
}
