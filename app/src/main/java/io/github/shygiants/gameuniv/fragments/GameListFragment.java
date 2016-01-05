package io.github.shygiants.gameuniv.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.activities.MainActivity;
import io.github.shygiants.gameuniv.models.Game;
import io.github.shygiants.gameuniv.ui.GameHolder;
import io.github.shygiants.gameuniv.ui.GameListAdapter;
import io.github.shygiants.gameuniv.utils.ContentsStore;
import io.github.shygiants.gameuniv.utils.TransactionManager;

/**
 * Created by SHY_mini on 2015. 12. 11..
 */
public class GameListFragment extends Fragment implements GameHolder.OnGameClickListener {

    @Bind(R.id.game_list)
    RecyclerView gameList;
    private GameListAdapter gameAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameAdapter = new GameListAdapter(this);
        ContentsStore.initGameList(gameAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_list, container, false);
        ButterKnife.bind(this, view);

        Toolbar toolbar = ButterKnife.findById(view, R.id.toolbar);
        MainActivity activity = (MainActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.initNavigationView(toolbar);

        SwipeRefreshLayout swipe = ButterKnife.findById(view, R.id.swipe);
        gameAdapter.setSwipeRefreshLayout(swipe);

        gameList.setLayoutManager(new GridLayoutManager(null, 3, LinearLayoutManager.VERTICAL, false));
        gameList.setAdapter(gameAdapter);

        return view;
    }

    @Override
    public void onGameClick(Game game) {
        TransactionManager.commitTransaction(game, getFragmentManager());

    }
}
