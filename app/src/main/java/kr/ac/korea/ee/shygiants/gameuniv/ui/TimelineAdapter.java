package kr.ac.korea.ee.shygiants.gameuniv.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.google.gson.Gson;

import kr.ac.korea.ee.shygiants.gameuniv.fragments.TimelineFragment;
import kr.ac.korea.ee.shygiants.gameuniv.models.User;

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
