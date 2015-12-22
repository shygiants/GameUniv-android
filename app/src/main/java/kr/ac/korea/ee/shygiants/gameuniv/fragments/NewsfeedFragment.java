package kr.ac.korea.ee.shygiants.gameuniv.fragments;

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

import kr.ac.korea.ee.shygiants.gameuniv.R;
import kr.ac.korea.ee.shygiants.gameuniv.activities.MainActivity;
import kr.ac.korea.ee.shygiants.gameuniv.models.Game;
import kr.ac.korea.ee.shygiants.gameuniv.models.User;
import kr.ac.korea.ee.shygiants.gameuniv.ui.FeedAdapter;
import kr.ac.korea.ee.shygiants.gameuniv.ui.MomentHolder;
import kr.ac.korea.ee.shygiants.gameuniv.utils.ContentsStore;
import kr.ac.korea.ee.shygiants.gameuniv.utils.TransactionManager;

/**
 * Created by SHYBook_Air on 15. 11. 27..
 */
public class NewsfeedFragment extends Fragment implements MomentHolder.OnMomentClickListener {

    private RecyclerView feedView;
    private FeedAdapter feedAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        feedAdapter = new FeedAdapter(this);
        ContentsStore.pushAdapter(feedAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsfeed, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        MainActivity activity = (MainActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.initNavigationView(toolbar);

        SwipeRefreshLayout swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        feedAdapter.setSwipeRefreshLayout(swipe);

        feedView = (RecyclerView) view.findViewById(R.id.feed);
        feedView.setLayoutManager(new LinearLayoutManager(null, LinearLayoutManager.VERTICAL, false));
        feedView.setAdapter(feedAdapter);

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
