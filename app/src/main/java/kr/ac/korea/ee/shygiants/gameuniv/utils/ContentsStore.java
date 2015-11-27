package kr.ac.korea.ee.shygiants.gameuniv.utils;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

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
    private static ContentsStore store = new ContentsStore();

    User user;

    ArrayList<RecyclerView.Adapter> adapters = new ArrayList<>();
    ArrayList<Moment> momentStore;

    private Moments momentsAPI = RESTAPI.create(Moments.class);

    public static void init(User user) {
        store.user = user;
        store.getFeed();
    }

    public static int getMomentsCount() {
        return (store.momentStore != null)? store.momentStore.size() : 0;
    }

    public static Moment getMomentAt(int position) {
        return (store.momentStore != null)? store.momentStore.get(position) : null;
    }

    public static void pushAdapter(RecyclerView.Adapter adapter) {
        store.adapters.add(adapter);
    }

    private void getFeed() {
        momentsAPI.getFeed(user.getEmail(), user.getAuthToken())
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
                        momentStore = response.body();
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
