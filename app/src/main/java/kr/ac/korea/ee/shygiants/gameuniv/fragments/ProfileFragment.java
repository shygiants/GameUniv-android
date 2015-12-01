package kr.ac.korea.ee.shygiants.gameuniv.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;

import kr.ac.korea.ee.shygiants.gameuniv.R;
import kr.ac.korea.ee.shygiants.gameuniv.activities.MainActivity;
import kr.ac.korea.ee.shygiants.gameuniv.apis.Users;
import kr.ac.korea.ee.shygiants.gameuniv.models.User;
import kr.ac.korea.ee.shygiants.gameuniv.ui.TimelineAdapter;
import kr.ac.korea.ee.shygiants.gameuniv.utils.ContentsStore;
import kr.ac.korea.ee.shygiants.gameuniv.utils.RESTAPI;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by SHYBook_Air on 15. 11. 27..
 */
public class ProfileFragment extends Fragment implements TabLayout.OnTabSelectedListener {

    public static final String TIMELINE_USER = "Timeline user";

    public static final int REQ_GET_IMAGE = 1;

    private Users usersAPI = RESTAPI.create(Users.class);

    private User user;

    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        Gson gson = new Gson();
        user = gson.fromJson(arguments.getString(TIMELINE_USER), User.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        MainActivity activity = (MainActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.initNavigationView(toolbar);

        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(user.getUserName());

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getActivity().getString(R.string.profile_tab_timeline)));

        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new TimelineAdapter(user, getChildFragmentManager()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(this);

        TextView userNameText = (TextView) view.findViewById(R.id.user_name_text);
        userNameText.setText(user.getUserName());

        View profileArea = view.findViewById(R.id.profile_area);
        ImageView userProfile = (ImageView) view.findViewById(R.id.user_profile);

        Bitmap iconBitmap = ((BitmapDrawable) userProfile.getDrawable()).getBitmap();
        Palette.Builder builder = Palette.from(iconBitmap);
        // Synchronously generate
        Palette palette = builder.generate();
        int[] colors = { palette.getDarkMutedColor(Color.WHITE), palette.getLightMutedColor(Color.WHITE) };
        GradientDrawable background = new GradientDrawable(GradientDrawable.Orientation.TL_BR, colors);
        profileArea.setBackground(background);

        return view;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

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
