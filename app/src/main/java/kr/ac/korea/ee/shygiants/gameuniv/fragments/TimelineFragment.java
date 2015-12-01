package kr.ac.korea.ee.shygiants.gameuniv.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.ac.korea.ee.shygiants.gameuniv.R;
import kr.ac.korea.ee.shygiants.gameuniv.models.User;
import kr.ac.korea.ee.shygiants.gameuniv.ui.FeedAdapter;
import kr.ac.korea.ee.shygiants.gameuniv.utils.ContentsStore;

/**
 * Created by SHYBook_Air on 15. 11. 27..
 */
public class TimelineFragment extends Fragment {

    private RecyclerView timelineView;
    private FeedAdapter timelineAdapter;

    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        timelineAdapter = new FeedAdapter(ContentsStore.getUser());
        ContentsStore.initTimeline(ContentsStore.getUser(), timelineAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);

        timelineView = (RecyclerView) view.findViewById(R.id.feed);
        timelineView.setLayoutManager(new LinearLayoutManager(null, LinearLayoutManager.VERTICAL, false));
        timelineView.setAdapter(timelineAdapter);

        return view;
    }
}
