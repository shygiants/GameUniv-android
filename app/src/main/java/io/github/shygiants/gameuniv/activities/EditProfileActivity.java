package io.github.shygiants.gameuniv.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.models.User;
import io.github.shygiants.gameuniv.utils.ContentsStore;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQ_GET_IMAGE = 1;

    private User user;

    CircleImageView profilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        user = ContentsStore.getInstance().getUser();

        profilePhoto = (CircleImageView) findViewById(R.id.profile_photo);
        profilePhoto.setOnClickListener(this);
        user.getProfilePhoto(profilePhoto);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_photo:
                // Edit profile photo
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select file to Upload"), REQ_GET_IMAGE);
                break;
            default:
                // DO NOTHING
                break;
        }
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
            Cursor cursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection, select, new String[]{id}, null);

            String filePath = "";
            if (cursor.moveToFirst()) {
                // Get file path from cursor
                filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            }
            cursor.close();

            user.setProfilePhoto(new File(filePath), profilePhoto);
        }
    }
}
