package io.github.shygiants.gameuniv.utils;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import io.github.shygiants.gameuniv.models.Moment;
import retrofit.*;

/**
 * Created by SHY_mini on 2016. 1. 2..
 */
public class Feed {

    Call<ArrayList<Moment>> task;
    private ArrayList<Moment> store;
    // TODO: Consider duplication
    private ArrayList<RecyclerView.Adapter> adapters = new ArrayList<>();
    private SwipeRefreshLayout swipe;

    public Feed(Call<ArrayList<Moment>> task) {
        this.task = task;
        getFeed();
    }

    public void pushAdapter(RecyclerView.Adapter adapter) {
        adapters.add(adapter);
    }

    public void removeAdapter(RecyclerView.Adapter adapter) {
        adapters.remove(adapter);
        Log.i("Feed", "Adapter removed from feed");
    }

    public void refresh(SwipeRefreshLayout swipe) {
        if (store != null) {
            store.clear();
            store = null;
            this.swipe = swipe;
            getFeed();
        }
    }

    public int getFeedElementsCount() {
        return (store != null)? store.size() : 0;
    }

    public Moment getFeedElementAt(int position) {
        return (store != null)? store.get(position) : null;
    }

    private void getFeed() {
        NetworkTask<ArrayList<Moment>> getFeed = new NetworkTask.Builder<>(task)
                .onSuccess(new NetworkTask.OnSuccessListener<ArrayList<Moment>>() {
                    @Override
                    public void onSuccess(ArrayList<Moment> responseBody) {
                        // TODO: Notify to all adapters
                        store = responseBody;
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
}
