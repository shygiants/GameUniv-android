package io.github.shygiants.gameuniv.fragments;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.activities.PostContentsActivity;
import io.github.shygiants.gameuniv.activities.MainActivity;
import io.github.shygiants.gameuniv.models.Game;
import io.github.shygiants.gameuniv.models.User;
import io.github.shygiants.gameuniv.ui.GameFeedAdapter;
import io.github.shygiants.gameuniv.ui.PostContentHolder;
import io.github.shygiants.gameuniv.utils.OnCreateGradientListener;
import io.github.shygiants.gameuniv.utils.TransactionManager;

/**
 * Created by SHYBook_Air on 15. 12. 3..
 */
public class GameFragment extends Fragment implements PostContentHolder.PostContentClickListener {

    public static final String GAME = "Game";

    @Bind(R.id.timeline)
    RecyclerView timelineView;

    private GameFeedAdapter timelineAdapter;

    private Game game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        Gson gson = new Gson();
        game = gson.fromJson(arguments.getString(GAME), Game.class);

        MainActivity activity = (MainActivity) getActivity();
        activity.disableNavigationView();

        timelineAdapter = new GameFeedAdapter(game, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        ButterKnife.bind(this, view);

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        Toolbar toolbar = ButterKnife.findById(view, R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout toolBarLayout = ButterKnife.findById(view, R.id.toolbar_layout);
        toolBarLayout.setTitle(game.getGameName());

        final View gameArea = ButterKnife.findById(view, R.id.game_area);
        ImageView gameIcon = ButterKnife.findById(view, R.id.game_icon);
        game.getGameIcon(gameIcon);
        game.getGameIconGradient(new OnCreateGradientListener() {
            @Override
            public void onCreateGradient(GradientDrawable gradient) {
                gameArea.setBackground(gradient);
            }
        });

        TextView gameNameText = ButterKnife.findById(view, R.id.game_name_text);
        gameNameText.setText(game.getGameName());

//        SwipeRefreshLayout swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
//        timelineAdapter.setSwipeRefreshLayout(swipe);

        timelineView.setLayoutManager(new LinearLayoutManager(null, LinearLayoutManager.VERTICAL, false));
        timelineView.setAdapter(timelineAdapter);

        return view;
    }

    @Override
    public void onDestroy() {
        timelineAdapter.destroy();
        super.onDestroy();
    }

    @Override
    public void onAuthorClick(User user) {
        TransactionManager.commitTransaction(user, getFragmentManager());
    }

    @Override
    public void onGameClick(Game game) {
        // DO NOTHING
    }

    @Override
    public void onClickAddPhotos() {
        Intent intent = new Intent(getActivity(), PostContentsActivity.class);
        Gson gson = new Gson();
        intent.putExtra(PostContentsActivity.ARG_GAME, gson.toJson(game));
        startActivityForResult(intent, PostContentsActivity.REQ_PICK_TITLE_PHOTO);

        // TODO: Handle activity result
    }

    @Override
    public void onClickWriteText() {
        // TODO: Handle
    }
}
