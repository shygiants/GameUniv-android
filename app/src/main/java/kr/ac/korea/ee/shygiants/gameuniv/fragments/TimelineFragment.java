package kr.ac.korea.ee.shygiants.gameuniv.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import kr.ac.korea.ee.shygiants.gameuniv.R;
import kr.ac.korea.ee.shygiants.gameuniv.models.Game;
import kr.ac.korea.ee.shygiants.gameuniv.models.User;
import kr.ac.korea.ee.shygiants.gameuniv.ui.FeedAdapter;
import kr.ac.korea.ee.shygiants.gameuniv.ui.MomentHolder;
import kr.ac.korea.ee.shygiants.gameuniv.utils.ContentsStore;

/**
 * Created by SHYBook_Air on 15. 12. 2..
 */
public class TimelineFragment extends Fragment implements MomentHolder.OnMomentClickListener {

    public static final String TIMELINE_USER = "Timeline user";
    public static final String IS_OWNER = "Whether it's owner";

    private RecyclerView timelineView;
    private FeedAdapter timelineAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        Gson gson = new Gson();
        User user = (arguments.getBoolean(IS_OWNER))?
                ContentsStore.getUser() : gson.fromJson(arguments.getString(TIMELINE_USER), User.class);

        timelineAdapter = new FeedAdapter(user, this);
        ContentsStore.initTimeline(user, timelineAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_profile_timeline, container, false);

        timelineView = (RecyclerView) view.findViewById(R.id.timeline);
        timelineView.setLayoutManager(new LinearLayoutManager(null, LinearLayoutManager.VERTICAL, false));
        timelineView.setAdapter(timelineAdapter);

        return view;
    }

    @Override
    public void onAuthorClick(User user) {
// TODO: Handle click event
    }

    @Override
    public void onGameClick(Game game) {
        Bundle arguments = new Bundle();
        Gson gson = new Gson();
        arguments.putString(GameFragment.GAME, gson.toJson(game));
        GameFragment gameFragment = new GameFragment();
        gameFragment.setArguments(arguments);

        FragmentTransaction transaction = getParentFragment().getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, gameFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
