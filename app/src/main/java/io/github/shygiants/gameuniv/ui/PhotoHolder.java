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

    @Bind(R.id.photo)
    ImageView photoView;
    @Bind(R.id.square)
    ImageView square;
    @Bind(R.id.order_text)
    TextView orderText;

    private boolean isSelected = false;
    private PhotoPickerActivity activity;

    public PhotoHolder(View view, PhotoPickerActivity activity) {
        super(view);
        ButterKnife.bind(this, view);

        view.setOnClickListener(this);

        this.activity = activity;
    }

    public void setPhoto(Photo photo) {
        photoView.setImageURI(photo.getThumbnailUri());
    }

    public void setOrder(int order) {
        orderText.setText(String.valueOf(order));
    }

    @Override
    public void onClick(View v) {
        click();
    }

    private void click() {
        setSelected(!isSelected);
    }

    private void setSelected(boolean select) {
        isSelected = select;

        activity.onSelect(this, isSelected);

        int visibility = (isSelected)? View.VISIBLE : View.INVISIBLE;
        square.setVisibility(visibility);
        orderText.setVisibility(visibility);
    }
}