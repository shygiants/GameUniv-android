package kr.ac.korea.ee.shygiants.gameuniv.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import kr.ac.korea.ee.shygiants.gameuniv.R;
import kr.ac.korea.ee.shygiants.gameuniv.activities.MainActivity;
import kr.ac.korea.ee.shygiants.gameuniv.models.Game;
import kr.ac.korea.ee.shygiants.gameuniv.models.User;
import kr.ac.korea.ee.shygiants.gameuniv.ui.FeedAdapter;
import kr.ac.korea.ee.shygiants.gameuniv.ui.MomentHolder;
import kr.ac.korea.ee.shygiants.gameuniv.utils.ContentsStore;

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

        feedView = (RecyclerView) view.findViewById(R.id.feed);
        feedView.setLayoutManager(new LinearLayoutManager(null, LinearLayoutManager.VERTICAL, false));
        feedView.setAdapter(feedAdapter);

        return view;
    }

    @Override
    public void onAuthorClick(User user) {
        // TODO: Handle click event
    }

    @Override
    public void onGameClick(Game game) {
        // TODO: Handle click event
        Bundle arguments = new Bundle();
        Gson gson = new Gson();
        arguments.putString(GameFragment.GAME, gson.toJson(game));
        GameFragment gameFragment = new GameFragment();
        gameFragment.setArguments(arguments);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, gameFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
