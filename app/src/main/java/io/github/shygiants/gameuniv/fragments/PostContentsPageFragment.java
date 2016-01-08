package io.github.shygiants.gameuniv.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.utils.ImageHandler;
import io.github.shygiants.gameuniv.utils.Photo;

/**
 * Created by SHYBook_Air on 2016. 1. 8..
 */
public class PostContentsPageFragment extends Fragment {

    private static final String ARG_PHOTO = "Photo";

    private Photo pagePhoto;
    @Bind(R.id.content_image)
    ImageView photoView;
    @Bind(R.id.content_text)
    EditText contentText;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PostContentsPageFragment newInstance(Photo photo) {
        PostContentsPageFragment fragment = new PostContentsPageFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PHOTO, photo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pagePhoto = getArguments().getParcelable(ARG_PHOTO);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_contents_page, container, false);
        ButterKnife.bind(this, rootView);

        photoView.setImageURI(pagePhoto.getImageUri());
        contentText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                photoView.setColorFilter(ImageHandler.getInstance().getColor(hasFocus? R.color.colorDimming : R.color.transparent));
            }
        });

        return rootView;
    }
}