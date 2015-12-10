package kr.ac.korea.ee.shygiants.gameuniv.fragments;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import kr.ac.korea.ee.shygiants.gameuniv.R;
import kr.ac.korea.ee.shygiants.gameuniv.activities.EditProfileActivity;
import kr.ac.korea.ee.shygiants.gameuniv.activities.MainActivity;
import kr.ac.korea.ee.shygiants.gameuniv.models.User;
import kr.ac.korea.ee.shygiants.gameuniv.ui.TimelineAdapter;
import kr.ac.korea.ee.shygiants.gameuniv.utils.ContentsStore;
import kr.ac.korea.ee.shygiants.gameuniv.utils.OnCreateGradientListener;

/**
 * Created by SHYBook_Air on 15. 11. 27..
 */
public class ProfileFragment extends Fragment implements TabLayout.OnTabSelectedListener, View.OnClickListener {

    public static final String TIMELINE_USER = "Timeline user";
    public static final String IS_OWNER = "Whether it's owner";

    private boolean isOwner;

    private User user;

    private ViewPager viewPager;

    // TODO: When destroying this fragment, delete timeline resource

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        Gson gson = new Gson();
        user = (isOwner = arguments.getBoolean(IS_OWNER))?
                ContentsStore.getUser() : gson.fromJson(arguments.getString(TIMELINE_USER), User.class);

        MainActivity activity = (MainActivity) getActivity();
        activity.disableNavigationView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        MainActivity activity = (MainActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(user.getUserName());

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getActivity().getString(R.string.profile_tab_timeline)));

        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new TimelineAdapter(user, getChildFragmentManager()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(this);

        TextView userNameText = (TextView) view.findViewById(R.id.user_name_text);
        userNameText.setText(user.getUserName());

        int resId = (isOwner)?
                R.string.edit_profile_button : R.string.follow_button;
        Button button = (Button) view.findViewById(R.id.button);
        button.setText(activity.getString(resId));
        button.setOnClickListener(this);

        final View profileArea = view.findViewById(R.id.profile_area);
        ImageView userProfile = (ImageView) view.findViewById(R.id.user_profile);
        user.getProfilePhoto(userProfile);
        user.getProfilePhotoGradient(new OnCreateGradientListener() {
            @Override
            public void onCreateGradient(GradientDrawable gradient) {
                profileArea.setBackground(gradient);
            }
        });

        return view;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {
        if (isOwner) {
            // Edit profile
            startActivity(new Intent(getActivity(), EditProfileActivity.class));
        } else {
            // TODO: Follow user
        }
    }
}
