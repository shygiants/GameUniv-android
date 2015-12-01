package kr.ac.korea.ee.shygiants.gameuniv.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;

import kr.ac.korea.ee.shygiants.gameuniv.R;
import kr.ac.korea.ee.shygiants.gameuniv.activities.EditingActivity;
import kr.ac.korea.ee.shygiants.gameuniv.apis.Users;
import kr.ac.korea.ee.shygiants.gameuniv.models.User;
import kr.ac.korea.ee.shygiants.gameuniv.ui.FeedAdapter;
import kr.ac.korea.ee.shygiants.gameuniv.ui.TimelineProfileHolder;
import kr.ac.korea.ee.shygiants.gameuniv.utils.ContentsStore;
import kr.ac.korea.ee.shygiants.gameuniv.utils.RESTAPI;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by SHYBook_Air on 15. 11. 27..
 */
public class TimelineFragment extends Fragment implements TimelineProfileHolder.OnEditButtonClickListener {

    public static final String TIMELINE_USER = "Timeline user";

    public static final int REQ_GET_IMAGE = 1;

    private Users usersAPI = RESTAPI.create(Users.class);

    private RecyclerView timelineView;
    private FeedAdapter timelineAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        Gson gson = new Gson();
        User user = gson.fromJson(arguments.getString(TIMELINE_USER), User.class);

        timelineAdapter = (user.getEmail().equals(ContentsStore.getUser().getEmail()))?
            new FeedAdapter(this) : new FeedAdapter(user);

        ContentsStore.initTimeline(user, timelineAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);

        timelineView = (RecyclerView) view.findViewById(R.id.feed);
        timelineView.setLayoutManager(new LinearLayoutManager(null, LinearLayoutManager.VERTICAL, false));
        timelineView.setAdapter(timelineAdapter);

        return view;
    }

    @Override
    public void onEditButtonClick() {
        startActivity(new Intent(getActivity(), EditingActivity.class));
//        Intent intent = new Intent();
//
//        intent.setType("image/*");
//
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//
//        startActivityForResult(Intent.createChooser(intent,"Select file to upload "), REQ_GET_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQ_GET_IMAGE) {
            Uri selectedImageUri = data.getData();

            // Get id from URI
            String wholeID = DocumentsContract.getDocumentId(selectedImageUri);
            String id = wholeID.split(":")[1];

            // Make query
            String[] projection = { MediaStore.Images.Media.DATA };
            String select = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = getActivity().getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection, select, new String[]{ id }, null);

            String filePath = "";
            if (cursor.moveToFirst()) {
                // Get file path from cursor
                filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            }
            cursor.close();

            File file = new File(filePath);
            RequestBody requestBody =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            User user = ContentsStore.getUser();
            usersAPI.uploadProfilePhoto(user.getEmail(), requestBody, user.getAuthToken())
                    .enqueue(new Callback<String>() {
                @Override
                public void onResponse(Response<String> response, Retrofit retrofit) {
                    // HERE IS MAIN THREAD!
                    switch (response.code()) {
                        // TODO: Error Handling
                        case 401: // Unauthorized
                            break;
                        case 500: // Server Error
                            break;
                        case 404: // Not Found
                            break;
                        case 200:
                            response.body();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e("Upload", t.getMessage());
                }
            });
        }
    }
}
