package io.github.shygiants.gameuniv.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.models.Game;
import io.github.shygiants.gameuniv.models.User;
import io.github.shygiants.gameuniv.ui.FeedAdapter;
import io.github.shygiants.gameuniv.ui.MomentHolder;
import io.github.shygiants.gameuniv.utils.ContentsStore;
import io.github.shygiants.gameuniv.utils.TransactionManager;

/**
 * Created by SHYBook_Air on 15. 12. 2..
 */
public class TimelineFragment extends Fragment implements MomentHolder.OnMomentClickListener {

    public static final String TIMELINE_USER = "Timeline user";
    public static final String IS_OWNER = "Whether it's owner";

    private RecyclerView timelineView;
    private FeedAdapter timelineAdapter;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        Gson gson = new Gson();
        user = (arguments.getBoolean(IS_OWNER))?
                ContentsStore.getInstance().getUser() : gson.fromJson(arguments.getString(TIMELINE_USER), User.class);

        timelineAdapter = new FeedAdapter(user, this);
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
    public void onDestroy() {
        timelineAdapter.destroy();
        super.onDestroy();
    }

    @Override
    public void onAuthorClick(User user) {
        // DO NOTHING
    }

    @Override
    public void onGameClick(Game game) {
        TransactionManager.commitTransaction(game, getParentFragment().getFragmentManager());
    }
}
