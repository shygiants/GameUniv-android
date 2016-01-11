package io.github.shygiants.gameuniv.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.activities.PostContentsActivity;
import io.github.shygiants.gameuniv.ui.KeyboardHandlerRelativeLayout;

/**
 * Created by SHYBook_Air on 2016. 1. 11..
 */
public abstract class PostContentsBaseFragment extends Fragment
        implements KeyboardHandlerRelativeLayout.OnKeyboardEventListener {

    KeyboardHandlerRelativeLayout container;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_contents_common, container, false);
        this.container = (KeyboardHandlerRelativeLayout) view.findViewById(R.id.container);

        this.container.setOnKeyboardEventListener(this);

        return view;
    }

    @Override
    public void onKeyboardHide() {
        ((PostContentsActivity)getActivity()).onEdited();
    }

    @Override
    public void onKeyboardShow() {
        ((PostContentsActivity)getActivity()).onEditing();
    }

    public abstract boolean isReadyToBePosted();
}
