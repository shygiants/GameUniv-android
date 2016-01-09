package io.github.shygiants.gameuniv.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.activities.PostContentsActivity;
import io.github.shygiants.gameuniv.models.User;
import io.github.shygiants.gameuniv.ui.KeyboardHandlerRelativeLayout;
import io.github.shygiants.gameuniv.utils.ContentsStore;

public class PostContentsTitleFragment extends Fragment implements KeyboardHandlerRelativeLayout.OnKeyboardEventListener{

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
    @Bind(R.id.container)
    KeyboardHandlerRelativeLayout container;

    private boolean readyToBePosted;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_contents_title, container, false);
        ButterKnife.bind(this, view);

        this.container.setOnKeyboardEventListener(this);
        User user = ContentsStore.getInstance().getUser();
        user.getProfilePhoto(profilePhoto);
        author.setText(user.getUserName());

        return view;
    }

    @Override
    public void onKeyboardHide() {
        readyToBePosted = (!title.getText().toString().isEmpty()
                        && !desc.getText().toString().isEmpty());
        ((PostContentsActivity)getActivity()).onEdited();
    }

    @Override
    public void onKeyboardShow() {
        ((PostContentsActivity)getActivity()).onEditing();
    }

    public boolean isReadyToBePosted() {
        return readyToBePosted;
    }
}