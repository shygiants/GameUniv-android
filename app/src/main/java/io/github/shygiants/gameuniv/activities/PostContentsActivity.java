package io.github.shygiants.gameuniv.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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
import io.github.shygiants.gameuniv.fragments.PostContentsPageFragment;
import io.github.shygiants.gameuniv.fragments.PostContentsTitleFragment;
import io.github.shygiants.gameuniv.ui.PostContentsPageAdapter;
import io.github.shygiants.gameuniv.utils.ImageHandler;
import io.github.shygiants.gameuniv.utils.Photo;
import io.github.shygiants.gameuniv.utils.PhotoPickerResultResolver;
import io.github.shygiants.gameuniv.utils.RESTAPI;

public class PostContentsActivity extends AppCompatActivity {

    public static final int REQ_PICK_TITLE_PHOTO = 1;
    public static final int REQ_PICK_PAGE_PHOTOS = 2;

    private PostContentsPageAdapter adapter;

    @Bind(R.id.container)
    ViewPager viewPager;
    private MenuItem postIcon;
    private MenuItem addIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_contents);
        ButterKnife.bind(this);

        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        adapter = new PostContentsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager.setAdapter(adapter);

        // TODO: Title first and add photos

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_contents, menu);

        int colorWhite = ImageHandler.getInstance().getColor(R.color.colorWhite);

        postIcon = menu.findItem(R.id.post);
        Drawable iconDrawable = postIcon.getIcon();
        iconDrawable.setTint(colorWhite);
        setPostEnabled(false);

        addIcon = menu.findItem(R.id.add_page);
        iconDrawable = addIcon.getIcon();
        iconDrawable.setTint(colorWhite);

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
            case R.id.add_page:
                Intent intent = new Intent(this, PhotoPickerActivity.class);
                intent.putExtra(PhotoPickerActivity.ARG_IS_MULTIPLE, true);
                startActivityForResult(intent, REQ_PICK_PAGE_PHOTOS);
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

        switch (requestCode) {
            case REQ_PICK_TITLE_PHOTO:
                // TODO: Set title photo
                break;
            case REQ_PICK_PAGE_PHOTOS:
                List<Photo> photosPicked = PhotoPickerResultResolver.resolve(data);
                adapter.setPhotos(photosPicked);
                setPostEnabled(false);
                break;
        }
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

    private void setPostEnabled(boolean enabled) {
        postIcon.getIcon().setAlpha(enabled ? 255 : (int) (255 * 0.3));
        postIcon.setEnabled(enabled);
    }

    public void onEdited() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments.size() <= 1) return;
        for (Fragment fragment : fragments) {
            if (fragment instanceof PostContentsPageFragment) {
                if (!((PostContentsPageFragment) fragment).isReadyToBePosted())
                    return;
            } else {
                if (!((PostContentsTitleFragment) fragment).isReadyToBePosted())
                    return;
            }
        }

        setPostEnabled(true);
    }

    public void onEditing() {
        setPostEnabled(false);
    }
}
