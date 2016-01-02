package io.github.shygiants.gameuniv.utils;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import io.github.shygiants.gameuniv.apis.Moments;
import io.github.shygiants.gameuniv.models.Game;
import io.github.shygiants.gameuniv.models.Moment;
import io.github.shygiants.gameuniv.models.TimelineOwner;
import io.github.shygiants.gameuniv.models.User;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by SHYBook_Air on 15. 11. 19..
 */
public class ContentsStore {

    // Singleton object
    private static ContentsStore singleton;

    // For newsfeed
    private User user;
    private Feed feed;

    // For timeline
    private HashMap<String, Feed> timelineStore = new HashMap<>();

    // For game list
    private ArrayList<Game> gameStore;
    private RecyclerView.Adapter gameAdapter;
    private SwipeRefreshLayout gameSwipe;

    private ContentsStore(User user) {
        this.user = user;
        Call<ArrayList<Moment>> task = RESTAPI.Moments.getFeed(user.getEmail(), user.getAuthToken());
        feed = new Feed(task);
    }

    public static void initFeed(User user) {
        if (singleton == null) singleton = new ContentsStore(user);
    }

    public static ContentsStore getInstance() {
        // TODO: Throw exception
        if (singleton == null) return null;
        return singleton;
    }

    public Feed getFeed() {
        return feed;
    }

    public User getUser() {
        // TODO: Consider possibility for null value
        return user;
    }

    public Feed getTimeline(TimelineOwner owner) {
        String key = owner.getKey();
        if (timelineStore.containsKey(key)) return timelineStore.get(key);
        else {
            Moments moments = RESTAPI.Moments;
            Call<ArrayList<Moment>> task = (owner instanceof Game)?
                    moments.getTimelineForGame(key, user.getAuthToken()) :
                    moments.getTimelineForUser(key, user.getAuthToken());
            Feed timeline = new Feed(task);
            timelineStore.put(key, timeline);
            return timeline;
        }
    }

    public static void initGameList(RecyclerView.Adapter adapter) {
        singleton.gameAdapter = adapter;
        singleton.getGameList();
    }

    public static int getGameListElementsCount() {
        return (singleton.gameStore != null)? singleton.gameStore.size() : 0;
    }

    public static Game getGameListElementAt(int position) {
        return (singleton.gameStore != null)? singleton.gameStore.get(position) : null;
    }

    public static void refreshGameList(SwipeRefreshLayout swipeRefreshLayout) {
        if (singleton.gameStore != null) {
            singleton.gameStore.clear();
            singleton.gameStore = null;
            singleton.gameSwipe = swipeRefreshLayout;
            singleton.getGameList();
        }
    }

    private void getGameList() {
        Call<ArrayList<Game>> task = RESTAPI.Games.getAllGames();
        NetworkTask<ArrayList<Game>> getAllGames = new NetworkTask.Builder<>(task)
                .onSuccess(new NetworkTask.OnSuccessListener<ArrayList<Game>>() {
                    @Override
                    public void onSuccess(ArrayList<Game> responseBody) {
                        // TODO: Notify to all adapters
                        gameStore = responseBody;
                        gameAdapter.notifyDataSetChanged();
                        if (gameSwipe != null) {
                            gameSwipe.setRefreshing(false);
                            gameSwipe = null;
                        }
                    }
                })
                .build();
        getAllGames.execute();
    }
}
