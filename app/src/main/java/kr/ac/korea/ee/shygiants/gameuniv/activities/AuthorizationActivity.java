package kr.ac.korea.ee.shygiants.gameuniv.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import kr.ac.korea.ee.shygiants.gameuniv.R;
import kr.ac.korea.ee.shygiants.gameuniv.apis.Games;
import kr.ac.korea.ee.shygiants.gameuniv.models.Game;
import kr.ac.korea.ee.shygiants.gameuniv.models.User;
import kr.ac.korea.ee.shygiants.gameuniv.utils.AuthManager;
import kr.ac.korea.ee.shygiants.gameuniv.utils.RESTAPI;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class AuthorizationActivity extends AppCompatActivity implements View.OnClickListener, AuthManager.UserInfoCallback {

    // Incoming intent keys
    private static final String GAME_LOGIN = "gameLogin";
    private static final String GAME_ID = "gameId";

    // Outgoing intent keys
    public static final String AUTH_CODE = "AUTH_CODE";

    private Games gamesAPI = RESTAPI.create(Games.class);

    private Game game;

    private String gameId;
    private AuthManager authManager;

    private TextView informingText;

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

            informingText = (TextView) findViewById(R.id.informingText);

            authManager = AuthManager.initWithCustomCallback(this, this);
            gamesAPI.getGame(gameId).enqueue(new Callback<Game>() {
                @Override
                public void onResponse(Response<Game> response, Retrofit retrofit) {
                    game = response.body();
                    String msg = String.format(getString(R.string.auth_message), game.getGameName());
                    informingText.setText(msg);
                }

                @Override
                public void onFailure(Throwable t) {
                    // TODO: Error Handling
                    t.printStackTrace();
                }
            });

        } else {
            fail();
        }
    }

    @Override
    public void onGettingUserInfo(User user) {
        // TODO: Display user info and prompt user


        findViewById(R.id.button_get_auth_code).setOnClickListener(this);
        findViewById(R.id.button_reject).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_get_auth_code:
                // TODO: Check it ends loading
                authManager.getAuthCode(gameId, new AuthManager.AuthCodeCallback() {
                    @Override
                    public void onGettingAuthCode(String authCode) {
                        Intent intent = new Intent();
                        intent.putExtra(AUTH_CODE, authCode);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                break;
            case R.id.button_reject:
                fail();
                break;
            default:
                // DO NOTHING
                break;
        }
    }

    private void fail() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
