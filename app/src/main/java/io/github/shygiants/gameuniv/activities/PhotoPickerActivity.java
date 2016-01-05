package io.github.shygiants.gameuniv.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.ui.PhotoAdapter;
import io.github.shygiants.gameuniv.ui.PhotoHolder;

public class PhotoPickerActivity extends AppCompatActivity {

    private PhotoAdapter photoAdapter;
    private List<PhotoHolder> selected;

    @Bind(R.id.photo_list)
    RecyclerView photoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);
        ButterKnife.bind(this);

        photoAdapter = new PhotoAdapter(this);
        // TODO: Consider maximum number of photos to pick
        selected = new ArrayList<>();

        getLoaderManager().initLoader(0, null, photoAdapter);
        photoList.setLayoutManager(new GridLayoutManager(null, 3, LinearLayoutManager.VERTICAL, false));
        photoList.setAdapter(photoAdapter);
    }

    public void onSelect(PhotoHolder holder, boolean isSelected) {
        if (isSelected) {
            selected.add(holder);
            holder.setOrder(selected.size());
        } else {
            int index = selected.indexOf(holder);
            ListIterator<PhotoHolder> iter = selected.listIterator(index);
            for (int i = index; iter.hasNext(); i++) {
                iter.next().setOrder(i);
            }
            selected.remove(index);
        }
    }
}
