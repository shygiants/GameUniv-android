package io.github.shygiants.gameuniv.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.models.Game;
import io.github.shygiants.gameuniv.utils.ContentsStore;

/**
 * Created by SHY_mini on 2015. 12. 11..
 */
public class GameHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public interface OnGameClickListener {
        void onGameClick(Game game);
    }

    private OnGameClickListener listener;

    @Bind(R.id.game_icon)
    ImageView gameIcon;
    @Bind(R.id.game_title)
    TextView gameTitle;

    private Game game;

    public GameHolder(View view, OnGameClickListener listener) {
        super(view);
        ButterKnife.bind(this, view);
        this.listener = listener;

        view.setOnClickListener(this);
    }

    public void populate(int position) {
        game = ContentsStore.getGameListElementAt(position);

        game.getGameIcon(gameIcon);
        gameTitle.setText(game.getGameName());
    }

    @Override
    public void onClick(View v) {
        listener.onGameClick(game);
    }
}
