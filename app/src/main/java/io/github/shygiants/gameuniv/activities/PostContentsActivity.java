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
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.entity.mime.content.ContentBody;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.fragments.PostContentsPageFragment;
import io.github.shygiants.gameuniv.ui.PostContentsPageAdapter;
import io.github.shygiants.gameuniv.utils.ImageHandler;
import io.github.shygiants.gameuniv.utils.NetworkTask;
import io.github.shygiants.gameuniv.utils.Photo;
import io.github.shygiants.gameuniv.utils.PhotoPickerResultResolver;
import io.github.shygiants.gameuniv.utils.RESTAPI;
import retrofit.Call;
import retrofit.Retrofit;

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

        Intent intent = new Intent(this, PhotoPickerActivity.class);
        startActivityForResult(intent, REQ_PICK_PHOTOS);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_contents, menu);
        postIcon = menu.findItem(R.id.post);
        postIcon.setEnabled(false);
        Drawable iconDrawable = postIcon.getIcon();
        iconDrawable.setTint(ImageHandler.getInstance().getColor(R.color.colorWhite));

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
            // TODO: Post contents
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
                    postIcon.setEnabled(true);
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
}
