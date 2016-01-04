package io.github.shygiants.gameuniv.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.models.TimelineOwner;

/**
 * Created by SHYBook_Air on 2016. 1. 4..
 */
public class GameFeedAdapter extends FeedAdapter {

    public static final int POST_CONTENT = -1;

    private PostContentHolder.PostContentClickListener listener;

    public GameFeedAdapter(TimelineOwner owner, PostContentHolder.PostContentClickListener listener) {
        super(owner, listener);
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return POST_CONTENT;
        return super.getItemViewType(position - 1);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) return;
        super.onBindViewHolder(holder, position - 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == POST_CONTENT) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View postContentView = inflater.inflate(R.layout.card_post_content, parent, false);
            return new PostContentHolder(postContentView, listener);
        }
        return super.onCreateViewHolder(parent, viewType);
    }
}
