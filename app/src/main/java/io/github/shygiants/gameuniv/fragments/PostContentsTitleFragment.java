package io.github.shygiants.gameuniv.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.models.User;
import io.github.shygiants.gameuniv.utils.ContentsStore;

public class PostContentsTitleFragment extends PostContentsBaseFragment {

    public PostContentsTitleFragment() {
        // Required empty public constructor
        // TODO: Make base class with PostContentsPageFragment
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PostContentsTitleFragment.
     */
    public static PostContentsTitleFragment newInstance() {
        PostContentsTitleFragment fragment = new PostContentsTitleFragment();
        return fragment;
    }

    @Bind(R.id.profile_photo)
    CircleImageView profilePhoto;
    @Bind(R.id.author)
    TextView author;
    @Bind(R.id.title_text)
    EditText title;
    @Bind(R.id.desc_text)
    EditText desc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        this.container.addView(inflater.inflate(R.layout.fragment_post_contents_title, container, false));
        ButterKnife.bind(this, rootView);

        User user = ContentsStore.getInstance().getUser();
        user.getProfilePhoto(profilePhoto);
        author.setText(user.getUserName());

        return rootView;
    }

    public boolean isReadyToBePosted() {
        return (!title.getText().toString().isEmpty()
                && !desc.getText().toString().isEmpty());
    }
}