package io.github.shygiants.gameuniv.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.google.gson.Gson;

import io.github.shygiants.gameuniv.fragments.TimelineFragment;
import io.github.shygiants.gameuniv.models.User;
import io.github.shygiants.gameuniv.utils.ContentsStore;

/**
 * Created by SHYBook_Air on 15. 12. 2..
 */
public class TimelineAdapter extends FragmentPagerAdapter {

    private User user;

    public TimelineAdapter(User user, FragmentManager fragmentManager) {
        super(fragmentManager);

        this.user = user;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TimelineFragment timelineFragment = new TimelineFragment();

                Bundle arguments = new Bundle();

                arguments.putBoolean(TimelineFragment.IS_OWNER, user.equals(ContentsStore.getUser()));
                Gson gson = new Gson();
                arguments.putString(TimelineFragment.TIMELINE_USER, gson.toJson(user));

                timelineFragment.setArguments(arguments);
                return timelineFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 1;
    }
}
