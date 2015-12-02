package kr.ac.korea.ee.shygiants.gameuniv.utils;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import kr.ac.korea.ee.shygiants.gameuniv.apis.Moments;
import kr.ac.korea.ee.shygiants.gameuniv.models.Moment;
import kr.ac.korea.ee.shygiants.gameuniv.models.User;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by SHYBook_Air on 15. 11. 19..
 */
public class ContentsStore {

    private static class Timeline {
        private User user;
        private ArrayList<Moment> store;
        // TODO: Consider duplication
        private ArrayList<RecyclerView.Adapter> adapters;

        public Timeline(User user, RecyclerView.Adapter adapter) {
            this.user = user;
            adapters = new ArrayList<>();
            pushAdapter(adapter);
            getTimeline();
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

        private void getTimeline() {
            RESTAPI.Moments.getTimeline(user.getEmail(), singleton.user.getAuthToken())
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

    public static void initTimeline(User user, RecyclerView.Adapter adapter) {
        String userEmail = user.getEmail();
        if (singleton.timelineStore.containsKey(userEmail)) {
            singleton.timelineStore.get(userEmail).pushAdapter(adapter);
            return;
        }

        singleton.timelineStore.put(user.getEmail(), new Timeline(user, adapter));
    }

    public static int getTimelineElementsCount(User user) {
        String userEmail = user.getEmail();
        return (singleton.timelineStore.containsKey(userEmail))?
                singleton.timelineStore.get(userEmail).getTimelineElementsCount() : 0;
    }

    public static Moment getTimelineElementAt(User user, int position) {
        String userEmail = user.getEmail();
        return (singleton.timelineStore.containsKey(userEmail))?
                singleton.timelineStore.get(userEmail).getTimelineElementAt(position) : null;
    }
}
