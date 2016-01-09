package io.github.shygiants.gameuniv.activities;

import android.app.Activity;
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
import io.github.shygiants.gameuniv.ui.PostContentsPageAdapter;
import io.github.shygiants.gameuniv.utils.ImageHandler;
import io.github.shygiants.gameuniv.utils.Photo;
import io.github.shygiants.gameuniv.utils.PhotoPickerResultResolver;
import io.github.shygiants.gameuniv.utils.RESTAPI;

public class PostContentsActivity extends AppCompatActivity {

    public static final int REQ_PICK_PHOTOS = 1;

    private PostContentsPageAdapter adapter;

    @Bind(R.id.container)
    ViewPager viewPager;
    private MenuItem postIcon;

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
        Intent intent = new Intent(this, PhotoPickerActivity.class);
        startActivityForResult(intent, REQ_PICK_PHOTOS);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_contents, menu);
        postIcon = menu.findItem(R.id.post);
        Drawable iconDrawable = postIcon.getIcon();
        iconDrawable.setTint(ImageHandler.getInstance().getColor(R.color.colorWhite));
        setPostEnabled(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.post) {
            postContents();
            return true;
        } else if (id == android.R.id.home) {
            cancel();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_PICK_PHOTOS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    List<Photo> photosPicked = PhotoPickerResultResolver.resolve(data);
                    adapter.setPhotos(photosPicked);
                    break;
                case Activity.RESULT_CANCELED:
                    cancel();
                    break;
            }
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
        for (Fragment fragment : fragments) {
            if (!((PostContentsPageFragment) fragment).isReadyToBePosted())
                return;
        }

        setPostEnabled(true);
    }

    public void onEditing() {
        setPostEnabled(false);
    }
}
