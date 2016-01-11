package io.github.shygiants.gameuniv.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.activities.PhotoPickerActivity;
import io.github.shygiants.gameuniv.utils.Photo;

/**
 * Created by SHYBook_Air on 2016. 1. 5..
 */
public class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public interface OnPhotoPickListener {
        void onSelect(PhotoHolder photoHolder);
        void onDeselect(PhotoHolder photoHolder);
    }

    @Bind(R.id.photo)
    ImageView photoView;
    @Bind(R.id.square)
    ImageView square;
    @Bind(R.id.order_text)
    TextView orderText;

    private boolean isSelected = false;

    private Photo photo;
    private OnPhotoPickListener listener;

    public PhotoHolder(View view, OnPhotoPickListener listener) {
        super(view);
        ButterKnife.bind(this, view);

        view.setOnClickListener(this);

        this.listener = listener;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
        photoView.setImageURI(photo.getThumbnailUri());
    }

    public Photo getPhoto() {
        return photo;
    }

    @Override
    public void onClick(View v) {
        if (isSelected)
            listener.onDeselect(this);
        else
            listener.onSelect(this);
    }

    public void setSelected(boolean select, int order) {
        if (!select) {
            orderText.setVisibility(View.INVISIBLE);
            square.setVisibility(View.INVISIBLE);
        } else {
            square.setVisibility(View.VISIBLE);
            if (order == 0)
                orderText.setVisibility(View.INVISIBLE);
            else {
                orderText.setVisibility(View.VISIBLE);
                orderText.setText(String.valueOf(order));
            }
        }

        isSelected = select;
    }
}