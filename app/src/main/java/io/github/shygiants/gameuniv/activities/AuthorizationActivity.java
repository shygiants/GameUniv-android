package io.github.shygiants.gameuniv.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.github.shygiants.gameuniv.R;
import io.github.shygiants.gameuniv.models.Game;
import io.github.shygiants.gameuniv.models.User;
import io.github.shygiants.gameuniv.utils.AuthManager;
import io.github.shygiants.gameuniv.utils.Callback;
import io.github.shygiants.gameuniv.utils.NetworkTask;
import io.github.shygiants.gameuniv.utils.RESTAPI;
import retrofit.Call;

public class AuthorizationActivity extends AppCompatActivity implements View.OnClickListener, Callback<User> {

    // Incoming intent keys
    private static final String GAME_LOGIN = "gameLogin";
    private static final String GAME_ID = "gameId";

    // Outgoing intent keys
    public static final String AUTH_CODE = "AUTH_CODE";

    private Game game;
    private String gameId;

    @Bind(R.id.container)
    View container;
    @Bind(R.id.informing_text)
    TextView informingText;
    @Bind(R.id.game_icon)
    ImageView gameIcon;

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
            ButterKnife.bind(this);

            AuthManager.getInstance().registerCallback(this);
        } else {
            fail();
        }
    }

    @Override
    public void pass(User user) {
        Call<Game> task = RESTAPI.Games.getGame(user.getAuthToken(), gameId);

        NetworkTask<Game> getGame = new NetworkTask.Builder<>(task)
                .onSuccess(new NetworkTask.OnSuccessListener<Game>() {
                    @Override
                    public void onSuccess(Game response) {
                        game = response;
                        game.getGameIcon(gameIcon);
                        String msg = String.format(getString(R.string.auth_message), game.getGameName());
                        informingText.setText(msg);
                    }
                })
                .setContainer(container)
                .build();
        getGame.execute();

        CircleImageView userProfile = ButterKnife.findById(this, R.id.user_profile);
        user.getProfilePhoto(userProfile);

        ButterKnife.findById(this, R.id.button_get_auth_code).setOnClickListener(this);
        ButterKnife.findById(this, R.id.button_reject).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_get_auth_code:
                // TODO: Check it ends loading
                AuthManager.getInstance().getAuthCode(gameId, new Callback<String>() {
                    @Override
                    public void pass(String authCode) {
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
