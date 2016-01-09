package io.github.shygiants.gameuniv.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.ui.PhotoAdapter;
import io.github.shygiants.gameuniv.utils.ImageHandler;

public class PhotoPickerActivity extends AppCompatActivity {

    public static final String PICKED_PHOTOS = "Picked Photos";
    public static final String ARG_IS_MULTIPLE = "Is Multiple";

    private PhotoAdapter photoAdapter;
    private MenuItem confirmIcon;

    @Bind(R.id.photo_list)
    RecyclerView photoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        boolean isMultiple = getIntent().getBooleanExtra(ARG_IS_MULTIPLE, false);
        photoAdapter = new PhotoAdapter(this, isMultiple);

        getLoaderManager().initLoader(0, null, photoAdapter);
        photoList.setLayoutManager(new GridLayoutManager(null, 3, LinearLayoutManager.VERTICAL, false));
        photoList.setAdapter(photoAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photo_picker, menu);
        confirmIcon = menu.findItem(R.id.confirm);
        confirmIcon.setEnabled(false);
        Drawable iconDrawable = confirmIcon.getIcon();
        iconDrawable.setTint(ImageHandler.getInstance().getColor(R.color.colorWhite));
        iconDrawable.setAlpha((int) (255 * 0.3));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.confirm) {

            Intent intent = new Intent();
            intent.putExtra(PICKED_PHOTOS, photoAdapter.getPicked());
            setResult(RESULT_OK, intent);
            finish();
            return true;
        } else if (id == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void onSelect(boolean canBeDone) {
        confirmIcon.setEnabled(canBeDone);
        Drawable iconDrawable = confirmIcon.getIcon();
        iconDrawable.setAlpha(canBeDone ? 255 : (int)(255 * 0.3));
    }
}
