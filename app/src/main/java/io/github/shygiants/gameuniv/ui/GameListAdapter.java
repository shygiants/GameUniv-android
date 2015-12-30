package io.github.shygiants.gameuniv.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.utils.ContentsStore;

/**
 * Created by SHY_mini on 2015. 12. 11..
 */
public class GameListAdapter extends RecyclerView.Adapter<GameHolder> implements SwipeRefreshLayout.OnRefreshListener {

    GameHolder.OnGameClickListener listener;
    private SwipeRefreshLayout swipeRefreshLayout;

    public GameListAdapter(GameHolder.OnGameClickListener listener) {
        this.listener = listener;
    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout swipe) {
        swipeRefreshLayout = swipe;
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onBindViewHolder(GameHolder gameHolder, int position) {
        gameHolder.populate(position);
    }

    @Override
    public int getItemCount() {
        return ContentsStore.getGameListElementsCount();
    }

    @Override
    public GameHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_game, parent, false);

        return new GameHolder(view, listener);
    }

    @Override
    public void onRefresh() {
        ContentsStore.refreshGameList(swipeRefreshLayout);
    }
}
