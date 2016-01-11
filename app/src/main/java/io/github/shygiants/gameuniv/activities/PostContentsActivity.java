package io.github.shygiants.gameuniv.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.fragments.PostContentsBaseFragment;
import io.github.shygiants.gameuniv.ui.FloatingActionButton;
import io.github.shygiants.gameuniv.ui.PostContentsPageAdapter;
import io.github.shygiants.gameuniv.utils.ImageHandler;
import io.github.shygiants.gameuniv.utils.RESTAPI;
import io.github.shygiants.simplephotopicker.activities.PhotoPickerActivity;
import io.github.shygiants.simplephotopicker.models.Photo;
import io.github.shygiants.simplephotopicker.utils.PhotoPickerResultResolver;

public class PostContentsActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQ_PICK_TITLE_PHOTO = 1;
    public static final int REQ_PICK_PAGE_PHOTOS = 2;

    private PostContentsPageAdapter adapter;

    @Bind(R.id.title_image)
    ImageView titleImage;
    @Bind(R.id.blurred_image)
    ImageView blurredImage;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.add_page_button)
    FloatingActionsMenu addPageButton;
    @Bind(R.id.write_text)
    FloatingActionButton writeTextButton;
    @Bind(R.id.add_photos)
    FloatingActionButton addPhotosButton;
    private MenuItem postIcon;

    private Photo titlePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_contents);
        ButterKnife.bind(this);

        Drawable writeTextIcon = getResources().getDrawable(R.drawable.ic_create, null);
        writeTextButton.setIconDrawable(writeTextIcon);
        writeTextButton.setOnClickListener(this);
        Drawable addPhotosIcon = getResources().getDrawable(R.drawable.ic_add_a_photo, null);
        addPhotosButton.setIconDrawable(addPhotosIcon);
        addPhotosButton.setOnClickListener(this);

        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        adapter = new PostContentsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position != 0) return;

                blurredImage.setAlpha(positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                /* DO NOTHING */
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /* DO NOTHING */
            }
        });

        // TODO: Title first and add photos

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_contents, menu);

        int colorWhite = ImageHandler.getInstance().getColor(R.color.colorWhite);
        int numOfMenuItems = menu.size();
        for(int i = 0; i < numOfMenuItems; i++) {
            Drawable iconDrawable = menu.getItem(i).getIcon();
            iconDrawable.setTint(colorWhite);
        }

        postIcon = menu.findItem(R.id.post);
        setPostEnabled(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.post:
                postContents();
                return true;
            case R.id.set_title_photo:
                startPhotoPickerActivity(false);
                return true;
            case R.id.view_pages:
                return true;
            case android.R.id.home:
                cancel();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) return;

        List<Photo> photosPicked = PhotoPickerResultResolver.resolve(data);
        switch (requestCode) {
            case REQ_PICK_TITLE_PHOTO:
                titlePhoto = photosPicked.get(0);
                initTitlePhoto();
                setPostEnabled(false);
                onEdited();
                break;
            case REQ_PICK_PAGE_PHOTOS:
                viewPager.setCurrentItem(adapter.addPhotos(photosPicked), true);
                setPostEnabled(false);
                break;
        }
    }

    private void initTitlePhoto() {
        Uri imageUri = titlePhoto.getImageUri();
        titleImage.setImageURI(imageUri);
        blurredImage.setImageBitmap(ImageHandler.getInstance().blur(imageUri));
        blurredImage.setAlpha(0.0f);
    }
    private void cancel() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void postContents() {
        List<Photo> photos = adapter.getPhotos();
        RequestParams params = new RequestParams();

        try {
            int i = 1;
            for (Photo photo : photos) {
                File file = new File(photo.getImageUri().getPath());
                params.put("page_photo" + i, file, "multipart/form-data", "page" + i + ".png");
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        AsyncHttpClient client = new AsyncHttpClient();
//            client.addHeader("Authentication");
        params.setForceMultipartEntityContentType(true);
        client.put(RESTAPI.getURL() + "/games/contents", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("CONTENTS", response.toString());
            }
        });
    }

    private void startPhotoPickerActivity(boolean isMultiple) {
        Intent intent = new Intent(this, PhotoPickerActivity.class);
        intent.putExtra(PhotoPickerActivity.ARG_IS_MULTIPLE, isMultiple);
        startActivityForResult(intent, (isMultiple)? REQ_PICK_PAGE_PHOTOS : REQ_PICK_TITLE_PHOTO);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_photos:
                startPhotoPickerActivity(true);
                break;
            case R.id.write_text:
                break;
        }
        addPageButton.collapse();
    }

    private void setPostEnabled(boolean enabled) {
        postIcon.getIcon().setAlpha(enabled ? 255 : (int) (255 * 0.3));
        postIcon.setEnabled(enabled);
    }

    public void onEdited() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments.size() <= 1) return;
        for (Fragment fragment : fragments) {
            if (!((PostContentsBaseFragment) fragment).isReadyToBePosted())
                return;
        }

        setPostEnabled(true);
    }

    public void onEditing() {
        setPostEnabled(false);
    }
}
