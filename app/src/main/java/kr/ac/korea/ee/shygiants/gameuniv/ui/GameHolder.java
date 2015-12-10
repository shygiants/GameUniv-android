package kr.ac.korea.ee.shygiants.gameuniv.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import kr.ac.korea.ee.shygiants.gameuniv.R;
import kr.ac.korea.ee.shygiants.gameuniv.models.Game;
import kr.ac.korea.ee.shygiants.gameuniv.utils.ContentsStore;

/**
 * Created by SHY_mini on 2015. 12. 11..
 */
public class GameHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public interface OnGameClickListener {
        void onGameClick(Game game);
    }

    private OnGameClickListener listener;

    private ImageView gameIcon;
    private TextView gameTitle;
    private Game game;

    public GameHolder(View view, OnGameClickListener listener) {
        super(view);
        this.listener = listener;

        view.setOnClickListener(this);
        gameIcon = (ImageView) view.findViewById(R.id.game_icon);
        gameTitle = (TextView) view.findViewById(R.id.game_title);
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
