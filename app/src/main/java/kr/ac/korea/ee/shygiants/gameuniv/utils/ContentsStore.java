package kr.ac.korea.ee.shygiants.gameuniv.utils;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import kr.ac.korea.ee.shygiants.gameuniv.models.Game;
import kr.ac.korea.ee.shygiants.gameuniv.models.Moment;
import kr.ac.korea.ee.shygiants.gameuniv.models.TimelineOwner;
import kr.ac.korea.ee.shygiants.gameuniv.models.User;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by SHYBook_Air on 15. 11. 19..
 */
public class ContentsStore {

    private static class Timeline {
        private String key;
        private ArrayList<Moment> store;
        // TODO: Consider duplication
        private ArrayList<RecyclerView.Adapter> adapters;

        public Timeline(TimelineOwner owner, RecyclerView.Adapter adapter) {
            key = owner.getKey();
            adapters = new ArrayList<>();
            pushAdapter(adapter);
            if (owner instanceof User) getTimelineForUser();
            else getTimelineForGame();
        }

        public void pushAdapter(RecyclerView.Adapter adapter) {
            adapters.add(adapter);
        }

        public int getTimelineElementsCount() {
            return (store != null)? store.size() : 0;
        }

        public Moment getTimelineElementAt(int position) {
            return (store != null)? store.get(position) : null;
        }

        private void getTimelineForUser() {
            RESTAPI.Moments.getTimelineForUser(key, singleton.user.getAuthToken())
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
            RESTAPI.Moments.getTimelineForGame(key, singleton.user.getAuthToken())
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

    // For timeline
    private HashMap<String, Timeline> timelineStore = new HashMap<>();

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

    private void getFeed() {
        RESTAPI.Moments.getFeed(user.getEmail(), user.getAuthToken())
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
                        feedStore = response.body();
                        for (RecyclerView.Adapter adapter : adapters)
                            adapter.notifyDataSetChanged();
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

    public static void initTimeline(TimelineOwner owner, RecyclerView.Adapter adapter) {
        String key = owner.getKey();
        if (singleton.timelineStore.containsKey(key))
            singleton.timelineStore.get(key).pushAdapter(adapter);

        singleton.timelineStore.put(key, new Timeline(owner, adapter));
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
}
