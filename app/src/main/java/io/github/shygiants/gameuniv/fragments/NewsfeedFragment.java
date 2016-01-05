package io.github.shygiants.gameuniv.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import io.github.shygiants.gameuniv.models.User;
import io.github.shygiants.gameuniv.ui.MomentAdapter;
import io.github.shygiants.gameuniv.ui.MomentHolder;
import io.github.shygiants.gameuniv.utils.TransactionManager;

/**
 * Created by SHYBook_Air on 15. 11. 27..
 */
public class NewsfeedFragment extends Fragment implements MomentHolder.OnMomentClickListener {

    @Bind(R.id.feed)
    RecyclerView feedView;
    private MomentAdapter momentAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        momentAdapter = new MomentAdapter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsfeed, container, false);
        ButterKnife.bind(this, view);

        Toolbar toolbar = ButterKnife.findById(view, R.id.toolbar);
        MainActivity activity = (MainActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.initNavigationView(toolbar);

        SwipeRefreshLayout swipe = ButterKnife.findById(view, R.id.swipe);
        momentAdapter.setSwipeRefreshLayout(swipe);

        feedView.setLayoutManager(new LinearLayoutManager(null, LinearLayoutManager.VERTICAL, false));
        feedView.setAdapter(momentAdapter);

        return view;
    }

    @Override
    public void onAuthorClick(User user) {
        // TODO: Handle click event
        TransactionManager.commitTransaction(user, getFragmentManager());
    }

    @Override
    public void onGameClick(Game game) {
        // TODO: Handle click event
        TransactionManager.commitTransaction(game, getFragmentManager());
    }
}
