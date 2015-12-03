package kr.ac.korea.ee.shygiants.gameuniv.fragments;

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
import android.widget.TextView;

import com.google.gson.Gson;

import kr.ac.korea.ee.shygiants.gameuniv.R;
import kr.ac.korea.ee.shygiants.gameuniv.activities.MainActivity;
import kr.ac.korea.ee.shygiants.gameuniv.models.Game;
import kr.ac.korea.ee.shygiants.gameuniv.models.User;
import kr.ac.korea.ee.shygiants.gameuniv.ui.FeedAdapter;
import kr.ac.korea.ee.shygiants.gameuniv.ui.MomentHolder;
import kr.ac.korea.ee.shygiants.gameuniv.utils.ContentsStore;

/**
 * Created by SHYBook_Air on 15. 12. 3..
 */
public class GameFragment extends Fragment implements MomentHolder.OnMomentClickListener {

    public static final String GAME = "Game";

    private RecyclerView feedView;
    private FeedAdapter feedAdapter;

    private Game game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        Gson gson = new Gson();
        game = gson.fromJson(arguments.getString(GAME), Game.class);

        MainActivity activity = (MainActivity) getActivity();
        activity.disableNavigationView();

        feedAdapter = new FeedAdapter(this);
        ContentsStore.pushAdapter(feedAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(game.getGameName());

        TextView gameNameText = (TextView) view.findViewById(R.id.game_name_text);
        gameNameText.setText(game.getGameName());

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
    }
}
