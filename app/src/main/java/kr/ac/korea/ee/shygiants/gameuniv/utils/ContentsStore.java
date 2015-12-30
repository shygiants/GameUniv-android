package kr.ac.korea.ee.shygiants.gameuniv.utils;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.ac.korea.ee.shygiants.gameuniv.models.Game;
import kr.ac.korea.ee.shygiants.gameuniv.models.Moment;
import kr.ac.korea.ee.shygiants.gameuniv.models.TimelineOwner;
import kr.ac.korea.ee.shygiants.gameuniv.models.User;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by SHYBook_Air on 15. 11. 19..
 */
public class ContentsStore {

    private static class Timeline {
        private TimelineOwner owner;
        private ArrayList<Moment> store;
        // TODO: Consider duplication
        private ArrayList<RecyclerView.Adapter> adapters;
        private SwipeRefreshLayout swipe;

        public Timeline(TimelineOwner owner, RecyclerView.Adapter adapter) {
            this.owner = owner;
            adapters = new ArrayList<>();
            pushAdapter(adapter);
            getTimeline();
        }

        public void pushAdapter(RecyclerView.Adapter adapter) {
            adapters.add(adapter);
        }

        public void removeAdapter(RecyclerView.Adapter adapter) {
            adapters.remove(adapter);
            Log.i("ContentsStore", "Adapter removed");
        }

        public void refresh(SwipeRefreshLayout swipe) {
            if (store != null) {
                store.clear();
                store = null;
                this.swipe = swipe;
                getTimeline();
            }
        }

        public int getTimelineElementsCount() {
            return (store != null)? store.size() : 0;
        }

        public Moment getTimelineElementAt(int position) {
            return (store != null)? store.get(position) : null;
        }

        private void getTimeline() {
            if (owner instanceof User) getTimelineForUser();
            else getTimelineForGame();
        }

        private void getTimelineForUser() {
            RESTAPI.Moments.getTimelineForUser(owner.getKey(), singleton.user.getAuthToken())
                    .enqueue(new Callback<ArrayList<Moment>>() {
                        @Override
                        public void onResponse(Response<ArrayList<Moment>> response, Retrofit retrofit) {
                            // HERE IS MAIN THREAD!
                            switch (response.code()) {
                                case 401: // Unauthorized
                                    break;
                                case 404: // Not Found
                                    break;
                                case 500: // Server Error
                                    break;
                                case 200:
                                    // TODO: Notify to all adapters
                                    store = response.body();
                                    for (RecyclerView.Adapter adapter : adapters)
                                        adapter.notifyDataSetChanged();
                                    if (swipe != null) {
                                        swipe.setRefreshing(false);
                                        swipe = null;
                                    }
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            // HERE IS MAIN THREAD!
                            // TODO: Something wrong on network
                            // TODO: Ask user to retry
                            t.printStackTrace();
                        }
                    });
        }

        private void getTimelineForGame() {
            RESTAPI.Moments.getTimelineForGame(owner.getKey(), singleton.user.getAuthToken())
                    .enqueue(new Callback<ArrayList<Moment>>() {
                        @Override
                        public void onResponse(Response<ArrayList<Moment>> response, Retrofit retrofit) {
                            // HERE IS MAIN THREAD!
                            switch (response.code()) {
                                case 401: // Unauthorized
                                    break;
                                case 404: // Not Found
                                    break;
                                case 500: // Server Error
                                    break;
                                case 200:
                                    // TODO: Notify to all adapters
                                    store = response.body();
                                    for (RecyclerView.Adapter adapter : adapters)
                                        adapter.notifyDataSetChanged();
                                    if (swipe != null) {
                                        swipe.setRefreshing(false);
                                        swipe = null;
                                    }
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            // HERE IS MAIN THREAD!
                            // TODO: Something wrong on network
                            // TODO: Ask user to retry
                            t.printStackTrace();
                        }
                    });
        }
    }

    // Singleton object
    private static ContentsStore singleton = new ContentsStore();

    // For newsfeed
    private User user;
    private ArrayList<RecyclerView.Adapter> adapters = new ArrayList<>();
    private ArrayList<Moment> feedStore;
    private SwipeRefreshLayout swipe;

    // For timeline
    private HashMap<String, Timeline> timelineStore = new HashMap<>();

    // For game list
    private ArrayList<Game> gameStore;
    private RecyclerView.Adapter gameAdapter;
    private SwipeRefreshLayout gameSwipe;

    public static void initFeed(User user) {
        singleton.user = user;
        singleton.getFeed();
    }

    public static int getFeedElementsCount() {
        return (singleton.feedStore != null)? singleton.feedStore.size() : 0;
    }

    public static Moment getFeedElementAt(int position) {
        return (singleton.feedStore != null)? singleton.feedStore.get(position) : null;
    }

    public static User getUser() {
        // TODO: Consider possibility for null value
        return singleton.user;
    }

    public static void pushAdapter(RecyclerView.Adapter adapter) {
        singleton.adapters.add(adapter);
    }

    public static void removeAdapter(RecyclerView.Adapter adapter) {
        singleton.adapters.remove(adapter);
    }

    public static void refresh(SwipeRefreshLayout swipeRefreshLayout) {
        if (singleton.feedStore != null) {
            singleton.feedStore.clear();
            singleton.feedStore = null;
            singleton.swipe = swipeRefreshLayout;
            singleton.getFeed();
        }
    }

    private void getFeed() {
        Call<ArrayList<Moment>> task = RESTAPI.Moments.getFeed(user.getEmail(), user.getAuthToken());
        NetworkTask<ArrayList<Moment>> getFeed = new NetworkTask.Builder<>(task)
                .onSuccess(new NetworkTask.OnSuccessListener<ArrayList<Moment>>() {
                    @Override
                    public void onSuccess(ArrayList<Moment> responseBody) {
                        // TODO: Notify to all adapters
                        feedStore = responseBody;
                        for (RecyclerView.Adapter adapter : adapters)
                            adapter.notifyDataSetChanged();
                        if (swipe != null) {
                            swipe.setRefreshing(false);
                            swipe = null;
                        }
                    }
                })
                .build();
        getFeed.execute();
    }

    public static void initTimeline(TimelineOwner owner, RecyclerView.Adapter adapter) {
        String key = owner.getKey();
        if (singleton.timelineStore.containsKey(key))
            singleton.timelineStore.get(key).pushAdapter(adapter);

        singleton.timelineStore.put(key, new Timeline(owner, adapter));
    }

    public static void removeTimelineAdapter(TimelineOwner owner, RecyclerView.Adapter adapter) {
        String key = owner.getKey();
        if (singleton.timelineStore.containsKey(key))
            singleton.timelineStore.get(key).removeAdapter(adapter);
    }

    public static void refreshTimeline(TimelineOwner owner, SwipeRefreshLayout swipeRefreshLayout) {
        String key = owner.getKey();
        if (singleton.timelineStore.containsKey(key))
            singleton.timelineStore.get(key).refresh(swipeRefreshLayout);
    }

    public static int getTimelineElementsCount(TimelineOwner owner) {
        String key = owner.getKey();
        return (singleton.timelineStore.containsKey(key))?
                singleton.timelineStore.get(key).getTimelineElementsCount() : 0;
    }

    public static Moment getTimelineElementAt(TimelineOwner owner, int position) {
        String key = owner.getKey();
        return (singleton.timelineStore.containsKey(key))?
                singleton.timelineStore.get(key).getTimelineElementAt(position) : null;
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
        RESTAPI.Games.getAllGames()
        .enqueue(new Callback<ArrayList<Game>>() {
            @Override
            public void onResponse(Response<ArrayList<Game>> response, Retrofit retrofit) {
                // HERE IS MAIN THREAD!
                switch (response.code()) {
                    case 404: // Not Found
                        break;
                    case 500: // Server Error
                        break;
                    case 200:
                        // TODO: Notify to all adapters
                        gameStore = response.body();
                        gameAdapter.notifyDataSetChanged();
                        if (gameSwipe != null) {
                            gameSwipe.setRefreshing(false);
                            gameSwipe = null;
                        }
                        break;
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // HERE IS MAIN THREAD!
                // TODO: Something wrong on network
                // TODO: Ask user to retry
                t.printStackTrace();
            }
        });
    }
}
