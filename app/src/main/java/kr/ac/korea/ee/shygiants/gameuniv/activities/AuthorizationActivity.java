package kr.ac.korea.ee.shygiants.gameuniv.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import kr.ac.korea.ee.shygiants.gameuniv.R;
import kr.ac.korea.ee.shygiants.gameuniv.models.User;
import kr.ac.korea.ee.shygiants.gameuniv.utils.AuthManager;

public class AuthorizationActivity extends AppCompatActivity implements View.OnClickListener, AuthManager.UserInfoCallback {

    // Incoming intent keys
    private static final String GAME_LOGIN = "gameLogin";
    private static final String GAME_ID = "gameId";

    // Outgoing intent keys
    public static final String AUTH_CODE = "AUTH_CODE";

    private String gameId;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Uri data = intent.getData();
        String authority = data.getAuthority();
        gameId = data.getQueryParameter(GAME_ID);

        if (authority.equals(GAME_LOGIN)
            && !TextUtils.isEmpty(gameId)) {
            // TODO: prompt user
            setContentView(R.layout.activity_authorization);

            authManager = AuthManager.initWithCustomCallback(this, this);
        } else {
            fail();
        }
    }

    @Override
    public void onGettingUserInfo(User user) {
        // TODO: Display user info and prompt user
        findViewById(R.id.button_get_auth_code).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        authManager.getAuthCode(gameId, new AuthManager.AuthCodeCallback() {
            @Override
            public void onGettingAuthCode(String authCode) {
                Intent intent = new Intent();
                intent.putExtra(AUTH_CODE, authCode);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void fail() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
