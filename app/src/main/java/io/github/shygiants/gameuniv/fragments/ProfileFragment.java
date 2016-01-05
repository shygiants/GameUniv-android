package io.github.shygiants.gameuniv.fragments;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.activities.EditProfileActivity;
import io.github.shygiants.gameuniv.activities.MainActivity;
import io.github.shygiants.gameuniv.models.User;
import io.github.shygiants.gameuniv.ui.TimelineAdapter;
import io.github.shygiants.gameuniv.utils.ContentsStore;
import io.github.shygiants.gameuniv.utils.OnCreateGradientListener;

/**
 * Created by SHYBook_Air on 15. 11. 27..
 */
public class ProfileFragment extends Fragment implements TabLayout.OnTabSelectedListener, View.OnClickListener {

    public static final String TIMELINE_USER = "Timeline user";
    public static final String IS_OWNER = "Whether it's owner";

    private boolean isOwner;

    private User user;

    @Bind(R.id.view_pager)
    ViewPager viewPager;

    // TODO: When destroying this fragment, delete timeline resource

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        Gson gson = new Gson();
        user = (isOwner = arguments.getBoolean(IS_OWNER))?
                ContentsStore.getInstance().getUser() : gson.fromJson(arguments.getString(TIMELINE_USER), User.class);

        MainActivity activity = (MainActivity) getActivity();
        activity.disableNavigationView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        Toolbar toolbar = ButterKnife.findById(view, R.id.toolbar);
        MainActivity activity = (MainActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout toolBarLayout = ButterKnife.findById(view, R.id.toolbar_layout);
        toolBarLayout.setTitle(user.getUserName());

        TabLayout tabLayout = ButterKnife.findById(view, R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getActivity().getString(R.string.profile_tab_timeline)));

        viewPager.setAdapter(new TimelineAdapter(user, getChildFragmentManager()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(this);

        TextView userNameText = ButterKnife.findById(view, R.id.user_name_text);
        userNameText.setText(user.getUserName());

        int resId = (isOwner)?
                R.string.edit_profile_button : R.string.follow_button;
        Button button = ButterKnife.findById(view, R.id.button);
        button.setText(activity.getString(resId));
        button.setOnClickListener(this);

        final View profileArea = ButterKnife.findById(view, R.id.profile_area);
        ImageView userProfile = ButterKnife.findById(view, R.id.user_profile);
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
