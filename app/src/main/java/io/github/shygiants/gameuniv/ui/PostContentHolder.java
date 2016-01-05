package io.github.shygiants.gameuniv.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.utils.ContentsStore;

/**
 * Created by SHYBook_Air on 2016. 1. 4..
 */
public class PostContentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public interface PostContentClickListener extends MomentHolder.OnMomentClickListener {
        void onClickAddPhotos();
        void onClickWriteText();
    }

    PostContentClickListener listener;

    public PostContentHolder(View view, PostContentClickListener listener) {
        super(view);

        this.listener = listener;

        CircleImageView profilePhoto = ButterKnife.findById(view, R.id.profile_photo);
        ContentsStore.getInstance().getUser().getProfilePhoto(profilePhoto);

        ImageButton addPhotoButton = ButterKnife.findById(view, R.id.add_photo_button);
        addPhotoButton.setOnClickListener(this);

        TextView writeContentText = ButterKnife.findById(view, R.id.write_content_text);
        writeContentText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.write_content_text:
                listener.onClickWriteText();
                break;
            case R.id.add_photo_button:
                listener.onClickAddPhotos();
                break;
        }
    }
}
